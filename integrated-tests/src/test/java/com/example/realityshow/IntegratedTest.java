package com.example.realityshow;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.UuidRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.util.Collections;

public class IntegratedTest {

  protected static final Logger logger = LoggerFactory.getLogger(IntegratedTest.class);

  public static final String MONGO_SERVICE_NAME = "mongo_1";
  public static final int MONGO_SERVICE_PORT = 27017;
  protected static final DockerComposeContainer<?> realityShowEnvironment =
      new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
          .withExposedService(MONGO_SERVICE_NAME, MONGO_SERVICE_PORT, Wait.forListeningPort())
          .withExposedService("rabbitmq_1", 5672, Wait.forListeningPort())
          .withExposedService(
              "realityshowweb_1", 8080, Wait.forLogMessage(".*Netty started on port 8080.*", 1))
          .withLogConsumer("realityshowweb_1", new Slf4jLogConsumer(logger));

  static {
    realityShowEnvironment.start();
    Runtime.getRuntime().addShutdownHook(new Thread(realityShowEnvironment::stop));
  }

  protected static final MongoClient mongoClient = mongoClient();

  private static MongoClient mongoClient() {

    final var serverAddress =
        new ServerAddress(
            realityShowEnvironment.getServiceHost(MONGO_SERVICE_NAME, MONGO_SERVICE_PORT),
            realityShowEnvironment.getServicePort(MONGO_SERVICE_NAME, MONGO_SERVICE_PORT));

    final var mongoClientSettings =
        MongoClientSettings.builder()
            .uuidRepresentation(UuidRepresentation.STANDARD)
            .applyToClusterSettings(
                clusterSettingsBuilder ->
                    clusterSettingsBuilder.hosts(Collections.singletonList(serverAddress)))
            .build();
    return MongoClients.create(mongoClientSettings);
  }
}
