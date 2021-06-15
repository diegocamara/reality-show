package com.example.realityshowweb;

import com.example.realityshow.feature.reactive.CreateParticipant;
import com.example.realityshow.feature.reactive.CreateRealityShow;
import com.example.realityshow.feature.reactive.CreateVotingDay;
import com.example.realityshowweb.infrastructure.repository.springdata.mongodb.reactiverepository.ReactiveMongoRealityShowRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Objects;

public class ApplicationContextIntegratedTests {

  protected static final MongoDBContainer mongoDBContainer =
      new MongoDBContainer(DockerImageName.parse("mongo:4.4.4"));
  protected static final RabbitMQContainer rabbitMQContainer =
      new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.8.16-management"));
  protected static final String MONGODB_HOST_KEY = "MONGODB_HOST";
  protected static final String MONGODB_PORT_KEY = "MONGODB_PORT";
  protected static final String RABBITMQ_HOST_KEY = "RABBITMQ_HOST";
  protected static final String RABBITMQ_PORT_KEY = "RABBITMQ_PORT";

  static {
    mongoDBContainer.start();
    rabbitMQContainer.start();
    System.setProperty(MONGODB_HOST_KEY, mongoDBContainer.getHost());
    System.setProperty(MONGODB_PORT_KEY, mongoDBContainer.getMappedPort(27017).toString());
    System.setProperty(RABBITMQ_HOST_KEY, rabbitMQContainer.getHost());
    System.setProperty(RABBITMQ_PORT_KEY, rabbitMQContainer.getMappedPort(5672).toString());
  }

  protected static final ConfigurableApplicationContext configurableApplicationContext =
      SpringApplication.run(RealityShowWebApplication.class);
  protected static final ObjectMapper objectMapper =
      configurableApplicationContext.getBean(ObjectMapper.class);
  protected static final ReactiveMongoRealityShowRepository reactiveMongoRealityShowRepository =
      configurableApplicationContext.getBean(ReactiveMongoRealityShowRepository.class);
  protected static final ReactiveMongoTemplate reactiveMongoTemplate =
      configurableApplicationContext.getBean(ReactiveMongoTemplate.class);
  protected static final RabbitAdmin rabbitAdmin =
      configurableApplicationContext.getBean(RabbitAdmin.class);
  protected static final Environment environment =
      configurableApplicationContext.getBean(Environment.class);
  protected static final Queue votesQueue =
      new Queue(Objects.requireNonNull(environment.getProperty("queue.votes")), true, false, false);
  protected static final CreateRealityShow createRealityShow =
      configurableApplicationContext.getBean(CreateRealityShow.class);
  protected static final CreateParticipant createParticipant =
      configurableApplicationContext.getBean(CreateParticipant.class);
  protected static final CreateVotingDay createVotingDay =
      configurableApplicationContext.getBean(CreateVotingDay.class);

  static {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  //                  configurableApplicationContext.stop();
                  //                  mongoDBContainer.stop();
                  //                  rabbitMQContainer.stop();
                  System.clearProperty(MONGODB_HOST_KEY);
                  System.clearProperty(MONGODB_PORT_KEY);
                  System.clearProperty(RABBITMQ_HOST_KEY);
                  System.clearProperty(RABBITMQ_PORT_KEY);
                }));
  }

  @BeforeAll
  public static void beforeAll() {
    createQueues();
  }

  private static void createQueues() {
    rabbitAdmin.declareQueue(votesQueue);
  }

  @AfterEach
  public void afterEach() {
    clearDatabase();
    clearQueues();
  }

  private void clearDatabase() {
    final var collections = reactiveMongoTemplate.getCollectionNames().collectList().block();
    collections.forEach(collection -> reactiveMongoTemplate.dropCollection(collection).block());
  }

  private void clearQueues() {
    rabbitAdmin.purgeQueue(votesQueue.getName());
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
