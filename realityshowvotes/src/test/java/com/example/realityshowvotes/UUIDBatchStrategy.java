package com.example.realityshowvotes;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.MessageBatch;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.util.Assert;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Consumer;

public class UUIDBatchStrategy implements BatchingStrategy {
  private final int batchSize;
  private final int bufferLimit;
  private final long timeout;
  private final List<Message> messages = new ArrayList();
  private String exchange;
  private String routingKey;
  private int currentSize;

  public UUIDBatchStrategy(int batchSize, int bufferLimit, long timeout) {
    this.batchSize = batchSize;
    this.bufferLimit = bufferLimit;
    this.timeout = timeout;
  }

  public MessageBatch addToBatch(String exch, String routKey, Message message) {
    if (this.exchange != null) {
      Assert.isTrue(
          this.exchange.equals(exch), "Cannot send to different exchanges in the same batch");
    } else {
      this.exchange = exch;
    }

    if (this.routingKey != null) {
      Assert.isTrue(
          this.routingKey.equals(routKey),
          "Cannot send with different routing keys in the same batch");
    } else {
      this.routingKey = routKey;
    }

    int bufferUse = 4 + message.getBody().length;
    MessageBatch batch = null;
    if (this.messages.size() > 0 && this.currentSize + bufferUse > this.bufferLimit) {
      batch = this.doReleaseBatch();
      this.exchange = exch;
      this.routingKey = routKey;
    }

    this.currentSize += bufferUse;
    this.messages.add(message);
    if (batch == null
        && (this.messages.size() >= this.batchSize || this.currentSize >= this.bufferLimit)) {
      batch = this.doReleaseBatch();
    }

    return batch;
  }

  public Date nextRelease() {
    if (this.messages.size() != 0 && this.timeout > 0L) {
      return this.currentSize >= this.bufferLimit
          ? new Date()
          : new Date(System.currentTimeMillis() + this.timeout);
    } else {
      return null;
    }
  }

  public Collection<MessageBatch> releaseBatches() {
    MessageBatch batch = this.doReleaseBatch();
    return batch == null ? Collections.emptyList() : Collections.singletonList(batch);
  }

  private MessageBatch doReleaseBatch() {
    if (this.messages.size() < 1) {
      return null;
    } else {
      Message message = this.assembleMessage();
      MessageBatch messageBatch = new MessageBatch(this.exchange, this.routingKey, message);
      this.messages.clear();
      this.currentSize = 0;
      this.exchange = null;
      this.routingKey = null;
      return messageBatch;
    }
  }

  private Message assembleMessage() {
    if (this.messages.size() == 1) {
      return (Message) this.messages.get(0);
    } else {
      MessageProperties messageProperties = ((Message) this.messages.get(0)).getMessageProperties();
      byte[] body = new byte[this.currentSize];
      ByteBuffer bytes = ByteBuffer.wrap(body);
      Iterator var4 = this.messages.iterator();

      while (var4.hasNext()) {
        Message message = (Message) var4.next();
        bytes.putInt(message.getBody().length);
        bytes.put(message.getBody());
      }

      messageProperties.getHeaders().put("batch_id", UUID.randomUUID().toString());
      messageProperties.getHeaders().put("springBatchFormat", "lengthHeader4");
      messageProperties.getHeaders().put("amqp_batchSize", this.messages.size());
      return new Message(body, messageProperties);
    }
  }

  public boolean canDebatch(MessageProperties properties) {
    return "lengthHeader4".equals(properties.getHeaders().get("springBatchFormat"));
  }

  public void deBatch(Message message, Consumer<Message> fragmentConsumer) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBody());
    MessageProperties messageProperties = message.getMessageProperties();
    messageProperties.getHeaders().remove("springBatchFormat");

    Message fragment;
    for (; byteBuffer.hasRemaining(); fragmentConsumer.accept(fragment)) {
      int length = byteBuffer.getInt();
      if (length < 0 || length > byteBuffer.remaining()) {
        throw new ListenerExecutionFailedException(
            "Bad batched message received",
            new MessageConversionException(
                "Insufficient batch data at offset " + byteBuffer.position()),
            new Message[] {message});
      }

      byte[] body = new byte[length];
      byteBuffer.get(body);
      messageProperties.setContentLength((long) length);
      fragment = new Message(body, messageProperties);
      if (!byteBuffer.hasRemaining()) {
        messageProperties.setLastInBatch(true);
      }
    }
  }
}
