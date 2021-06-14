package com.example.realityshowweb.infrastructure.repository;

import com.example.realityshow.model.Vote;
import com.example.realityshow.model.reactive.VotesRepository;
import com.example.realityshowweb.infrastructure.repository.rabbitmq.message.VoteMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class BatchingRabbitMQVotesRepository implements VotesRepository {

  private final BatchingRabbitTemplate batchingRabbitTemplate;
  private final ObjectMapper objectMapper;
  private final String votesQueue;

  public BatchingRabbitMQVotesRepository(
      BatchingRabbitTemplate batchingRabbitTemplate,
      ObjectMapper objectMapper,
      @Value("${queue.votes}") String votesQueue) {
    this.batchingRabbitTemplate = batchingRabbitTemplate;
    this.objectMapper = objectMapper;
    this.votesQueue = votesQueue;
  }

  @Override
  public Mono<Void> save(Vote vote) {
    return Mono.fromRunnable(
        () -> batchingRabbitTemplate.convertAndSend(votesQueue, voteMessage(vote)));
  }

  @SneakyThrows
  private Message voteMessage(Vote vote) {
    return MessageBuilder.withBody(this.objectMapper.writeValueAsBytes(new VoteMessage(vote)))
        .setContentType(MessageProperties.CONTENT_TYPE_JSON)
        .setContentEncoding("utf-8")
        .build();
  }
}
