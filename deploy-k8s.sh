#!/bin/bash
mvn clean install -f domain/ -DskipTests
mvn clean package -f realityshowweb/ -DskipTests
mvn clean package -f realityshowvotes/ -DskipTests
eval $(minikube docker-env)
cd realityshowweb
docker build -t realityshowweb:0.0.1 .
cd ../realityshowvotes
docker build -t realityshowvotes:0.0.1 .
cd ../integrated-tests/src/test/resources/rabbitmq
docker build -t rabbitmq-with-definitions .
cd ../../../../../
kubectl apply -f k8s/