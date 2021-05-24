package com.example.realityshowvotes;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.batch.BatchingStrategy;
import org.springframework.amqp.rabbit.batch.MessageBatch;

import java.util.Collection;
import java.util.Date;

public class CustomBatchStrategy implements BatchingStrategy {
  @Override
  public MessageBatch addToBatch(String s, String s1, Message message) {
    return null;
  }

  @Override
  public Date nextRelease() {
    return null;
  }

  @Override
  public Collection<MessageBatch> releaseBatches() {
    return null;
  }
}
