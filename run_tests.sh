#!/bin/bash
mvn clean install -f domain/
mvn clean package -DskipTests=true -f realityshowweb/
mvn clean package -DskipTests=true -f realityshowvotes/
mv realityshowweb/target/reality-show-web-api.jar integrated-tests/src/test/resources/realityshowweb/
mv realityshowvotes/target/reality-show-votes-consumer.jar integrated-tests/src/test/resources/realityshowvotes/
mvn clean test -f integrated-tests/