package com.example.realityshowweb.infrastructure.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "batching-strategy")
public class BatchingStrategyProperties {
  private int batchSize;
  private int bufferLimit = Integer.MAX_VALUE;
  private long timeout;
}
