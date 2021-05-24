package com.example.realityshowvotes;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.springframework.amqp.rabbit.batch.SimpleBatchingStrategy;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.BatchingRabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public class ApplicationContextIntegratedTests {

  protected static final MongoDBContainer mongoDBContainer =
      new MongoDBContainer(DockerImageName.parse("mongo:4.4.4"));
  protected static final String RABBITMQ_VOTES_QUEUE_NAME = "votes";
  protected static final RabbitMQContainer rabbitMQContainer =
      new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.8.16-management"))
          .withQueue(RABBITMQ_VOTES_QUEUE_NAME);
  protected static final String MONGODB_HOST_KEY = "MONGODB_HOST";
  protected static final String MONGODB_PORT_KEY = "MONGODB_PORT";
  protected static final String RABBITMQ_HOST_KEY = "RABBITMQ_HOST";
  protected static final String RABBITMQ_PORT_KEY = "RABBITMQ_PORT";
  protected static final String RABBITMQ_VOTES_QUEUE_NAME_KEY = "RABBITMQ_VOTES_QUEUE_NAME";

  static {
    mongoDBContainer.start();
    rabbitMQContainer.start();
    System.setProperty(MONGODB_HOST_KEY, mongoDBContainer.getHost());
    System.setProperty(MONGODB_PORT_KEY, mongoDBContainer.getMappedPort(27017).toString());
    System.setProperty(RABBITMQ_HOST_KEY, rabbitMQContainer.getHost());
    System.setProperty(RABBITMQ_PORT_KEY, rabbitMQContainer.getMappedPort(5672).toString());
    System.setProperty(RABBITMQ_VOTES_QUEUE_NAME_KEY, RABBITMQ_VOTES_QUEUE_NAME);
  }

  protected static final ConfigurableApplicationContext configurableApplicationContext =
      SpringApplication.run(RealityShowVotesApplication.class);
  protected static final ObjectMapper objectMapper =
      configurableApplicationContext.getBean(ObjectMapper.class);
  protected static final MongoTemplate mongoTemplate =
      configurableApplicationContext.getBean(MongoTemplate.class);
  protected static final RabbitAdmin rabbitAdmin =
      configurableApplicationContext.getBean(RabbitAdmin.class);
  protected static final RabbitTemplate rabbitTemplate =
      configurableApplicationContext.getBean(RabbitTemplate.class);
  protected static final ConnectionFactory connectionFactory =
      configurableApplicationContext.getBean(ConnectionFactory.class);
  protected static final BatchingRabbitTemplate batchingRabbitTemplate = batchingRabbitTemplate();

  static {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  configurableApplicationContext.stop();
                  mongoDBContainer.stop();
                  rabbitMQContainer.stop();
                  System.clearProperty(MONGODB_HOST_KEY);
                  System.clearProperty(MONGODB_PORT_KEY);
                  System.clearProperty(RABBITMQ_HOST_KEY);
                  System.clearProperty(RABBITMQ_PORT_KEY);
                }));
  }

  @AfterEach
  public void afterEach() {
    clearDatabase();
  }

  private void clearDatabase() {
    final var collections = mongoTemplate.getCollectionNames();
    collections.forEach(mongoTemplate::dropCollection);
  }

  private static BatchingRabbitTemplate batchingRabbitTemplate() {
    final var simpleBatchingStrategy = new SimpleBatchingStrategy(10, Integer.MAX_VALUE, 30000);
    final var concurrentTaskScheduler = new ConcurrentTaskScheduler();
    return new BatchingRabbitTemplate(
        connectionFactory, simpleBatchingStrategy, concurrentTaskScheduler);
  }

  @SneakyThrows
  protected String writeValueAsString(Object value) {
    return objectMapper.writeValueAsString(value);
  }

  @SneakyThrows
  protected <T> T readValue(String value, Class<T> clazz) {
    return objectMapper.readValue(value, clazz);
  }
}
