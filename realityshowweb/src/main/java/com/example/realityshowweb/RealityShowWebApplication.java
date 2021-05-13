package com.example.realityshowweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.realityshowweb", "com.example.realityshow"})
public class RealityShowWebApplication {

  public static void main(String[] args) {
    SpringApplication.run(RealityShowWebApplication.class, args);
  }
}
