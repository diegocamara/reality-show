package com.example.realityshowweb;

import com.example.realityshowweb.infrastructure.repository.springdata.mongodb.reactiverepository.ReactiveMongoRealityShowRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class IntegratedTests {

  protected static final MongoDBContainer mongoDBContainer =
      new MongoDBContainer(DockerImageName.parse("mongo:4.4.4"));

  protected static final String MONGODB_HOST_KEY = "MONGODB_HOST";
  protected static final String MONGODB_PORT_KEY = "MONGODB_PORT";

  static {
    mongoDBContainer.start();
    System.setProperty(MONGODB_HOST_KEY, mongoDBContainer.getHost());
    System.setProperty(MONGODB_PORT_KEY, mongoDBContainer.getMappedPort(27017).toString());
  }

  protected static final ConfigurableApplicationContext configurableApplicationContext =
      SpringApplication.run(RealityShowWebApplication.class);
  protected static final ObjectMapper objectMapper =
      configurableApplicationContext.getBean(ObjectMapper.class);
  protected static final ReactiveMongoRealityShowRepository reactiveMongoRealityShowRepository =
      configurableApplicationContext.getBean(ReactiveMongoRealityShowRepository.class);
  protected static final ReactiveMongoTemplate reactiveMongoTemplate =
      configurableApplicationContext.getBean(ReactiveMongoTemplate.class);

  static {
    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  configurableApplicationContext.stop();
                  mongoDBContainer.stop();
                  System.clearProperty(MONGODB_HOST_KEY);
                  System.clearProperty(MONGODB_PORT_KEY);
                }));
  }

  @AfterEach
  public void afterEach() {
    clearDatabase();
  }

  private void clearDatabase() {
    final var collections = reactiveMongoTemplate.getCollectionNames().collectList().block();
    collections.forEach(collection -> reactiveMongoTemplate.dropCollection(collection).block());
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
