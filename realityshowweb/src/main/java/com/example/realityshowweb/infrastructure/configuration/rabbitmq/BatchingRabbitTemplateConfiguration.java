package com.example.realityshowweb.infrastructure.configuration.rabbitmq;

import com.example.realityshowweb.infrastructure.configuration.properties.BatchingStrategyProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

@Configuration
public class BatchingRabbitTemplateConfiguration {

  @Bean
  public BatchingRabbitTemplate batchingRabbitTemplate(
      ConnectionFactory connectionFactory, BatchingStrategyProperties batchingStrategyProperties) {
    final var simpleBatchingStrategy =
        new UUIDBatchStrategy(
            batchingStrategyProperties.getBatchSize(),
            batchingStrategyProperties.getBufferLimit(),
            batchingStrategyProperties.getTimeout());
    final var concurrentTaskScheduler = new ConcurrentTaskScheduler();
    return new BatchingRabbitTemplate(
        connectionFactory, simpleBatchingStrategy, concurrentTaskScheduler);
  }
}
