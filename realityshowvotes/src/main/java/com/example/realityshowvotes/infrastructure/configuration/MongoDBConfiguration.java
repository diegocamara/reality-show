package com.example.realityshowvotes.infrastructure.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import lombok.AllArgsConstructor;
import org.bson.UuidRepresentation;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import java.util.Collections;

@Configuration
@AllArgsConstructor
public class MongoDBConfiguration extends AbstractMongoClientConfiguration {

  private final MongoProperties mongoProperties;

  @Override
  protected void configureClientSettings(MongoClientSettings.Builder builder) {
    builder
        .uuidRepresentation(UuidRepresentation.STANDARD)
        .applyToClusterSettings(
            clusterSettingsBuilder ->
                clusterSettingsBuilder.hosts(
                    Collections.singletonList(
                        new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort()))));
  }

  @Override
  protected String getDatabaseName() {
    return this.mongoProperties.getDatabase();
  }

  @Bean
  public MongoTransactionManager transactionManager(MongoDatabaseFactory mongoDatabaseFactory) {
    return new MongoTransactionManager(mongoDatabaseFactory);
  }
}
