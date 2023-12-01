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

### ElasticSearch

Use the following http request to create an index named twitter-index

METHOD - PUT

URL - http://localhost:9200/twitter-index
```json
{
    "mappings": {
        "properties": {
            "userId": {
                "type": "long"
            },
            "id": {
                "type": "text",
                "fields": {
                    "keyword": {
                        "type": "keyword",
                        "ignore_above": 256
                    }
                }
            },
            "createdAt": {
                "type": "date",
		"format": "yyyy-MM-dd'T'HH:mm:ssZZ"
            },
            "text": {
                "type": "text",
                "fields": {
                    "keyword": {
                        "type": "keyword",
                        "ignore_above": 256
                    }
                }
            }
        }
    }
}
```
To create a document

Method - POST

URL - http://localhost:9200/twitter-index/_doc/1

```json
{
    "userId": "1",
    "id": "1",
    "createdAt":"2023-12-01T23:00:50+0000",
    "text": "test multi word"

}
```

To search a document

Method - GET
URL - http://localhost:9200/twitter-index/_search?q=id:1


