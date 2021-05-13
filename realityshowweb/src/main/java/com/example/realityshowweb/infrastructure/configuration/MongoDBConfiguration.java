package com.example.realityshowweb.infrastructure.configuration;

import com.mongodb.MongoClientSettings;
import lombok.AllArgsConstructor;
import org.bson.UuidRepresentation;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

@Configuration
@AllArgsConstructor
public class MongoDBConfiguration extends AbstractReactiveMongoConfiguration {

  private final MongoProperties mongoProperties;

  @Override
  protected void configureClientSettings(MongoClientSettings.Builder builder) {
    builder.uuidRepresentation(UuidRepresentation.STANDARD);
  }

  @Override
  protected String getDatabaseName() {
    return mongoProperties.getDatabase();
  }

  @Bean
  public ReactiveMongoTransactionManager transactionManager(
      ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory) {
    return new ReactiveMongoTransactionManager(reactiveMongoDatabaseFactory);
  }
}
