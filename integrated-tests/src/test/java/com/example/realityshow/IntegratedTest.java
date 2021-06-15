package com.example.realityshow;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.util.Collections;

public class IntegratedTest {

  protected static final Logger logger = LoggerFactory.getLogger(IntegratedTest.class);

  public static final String MONGO_SERVICE_NAME = "mongo_1";
  public static final int MONGO_SERVICE_PORT = 27017;
  public static final String REALITY_SHOW_WEB_SERVICE_NAME = "realityshowweb_1";
  public static final int REALITY_SHOW_WEB_SERVICE_PORT = 8080;
  public static final String RABBITMQ_SERVICE_NAME = "rabbitmq_1";
  public static final int RABBITMQ_SERVICE_PORT = 5672;
  public static final int RABBITMQ_MANAGEMENT_SERVICE_PORT = 15672;
  public static final String REALITY_SHOW_VOTES_SERVICE_NAME = "realityshowvotes_1";
  protected static final DockerComposeContainer<?> realityShowEnvironment =
      new DockerComposeContainer<>(new File("src/test/resources/docker-compose.yml"))
          .withExposedService(MONGO_SERVICE_NAME, MONGO_SERVICE_PORT, Wait.forListeningPort())
          .withExposedService(RABBITMQ_SERVICE_NAME, RABBITMQ_SERVICE_PORT, Wait.forListeningPort())
          .withExposedService(
              RABBITMQ_SERVICE_NAME, RABBITMQ_MANAGEMENT_SERVICE_PORT, Wait.forListeningPort())
          .withExposedService(
              REALITY_SHOW_WEB_SERVICE_NAME,
              REALITY_SHOW_WEB_SERVICE_PORT,
              Wait.forLogMessage(".*Netty started on port.*", 1))
          .withExposedService(
              REALITY_SHOW_VOTES_SERVICE_NAME,
              0,
              Wait.forLogMessage(".*Created new connection: rabbitConnectionFactory.*", 1));

  static {
    realityShowEnvironment.start();
  }

  protected static final String REALITY_SHOW_WEB_API_URL =
      "http://"
          .concat(
              realityShowEnvironment.getServiceHost(
                  REALITY_SHOW_WEB_SERVICE_NAME, REALITY_SHOW_WEB_SERVICE_PORT))
          .concat(":")
          .concat(
              String.valueOf(
                  realityShowEnvironment.getServicePort(
                      REALITY_SHOW_WEB_SERVICE_NAME, REALITY_SHOW_WEB_SERVICE_PORT)))
          .concat("/api/v1/reality-shows");

  public static final String REALITY_SHOW_DATABASE_NAME = "realityshowdb";
  public static final String REALITY_SHOWS_COLLECTIONS_NAME = "realityshows";
  public static final String VOTES_COLLECTIONS_NAME = "votes";
  protected static final MongoClient mongoClient = mongoClient();
  protected static final MongoDatabase realityShowDataBase = realityShowDataBase();
  protected static final MongoCollection<Document> realityShowsCollection =
      realityShowsCollection();
  protected static final MongoCollection<Document> votesCollection = votesCollection();

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

  private static MongoDatabase realityShowDataBase() {
    return mongoClient.getDatabase(REALITY_SHOW_DATABASE_NAME);
  }

  private static MongoCollection<Document> realityShowsCollection() {
    return realityShowDataBase.getCollection(REALITY_SHOWS_COLLECTIONS_NAME);
  }

  private static MongoCollection<Document> votesCollection() {
    return realityShowDataBase.getCollection(VOTES_COLLECTIONS_NAME);
  }
}
