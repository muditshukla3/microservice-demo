### Introduction
This is microservices demo project to demonstrate various microservices implementation patterns.

### Running kafka

Run the following command to start the kafka infrastructure

```
cd docker-compose
```
```
docker-compose -f common.yml -f zookeeper.yml -f kafka-cluster.yml up -d
```

To bring down kafka run the following command

```
docker-compose -f common.yml -f zookeeper.yml -f kafka-cluster.yml down
```

### KafkaCat

Run the following command to inspect the kafka cluster

```
kcat -L -b localhost:19092
```
Run the following command to consume the messages from topic

```
kcat -C -b localhost:19092 -t twitter-topic
```