#!/bin/bash
mvn clean install -f domain/
mvn clean package -f realityshowweb/
mvn clean package -f realityshowvotes/
mv realityshowweb/target/reality-show-web-api.jar integrated-tests/src/test/resources/realityshowweb/
mv realityshowvotes/target/reality-show-votes-consumer.jar integrated-tests/src/test/resources/realityshowvotes/
mvn clean test -f integrated-tests/