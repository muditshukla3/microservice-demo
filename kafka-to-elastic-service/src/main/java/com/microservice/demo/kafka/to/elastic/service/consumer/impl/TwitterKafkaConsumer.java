package com.microservice.demo.kafka.to.elastic.service.consumer.impl;

import com.microservice.demo.config.KafkaConfigData;
import com.microservice.demo.config.KafkaConsumerConfigData;
import com.microservice.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.kafka.admin.client.KafkaAdminClient;
import com.microservice.demo.kafka.avro.model.TwitterAvroModel;
import com.microservice.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
import com.microservice.demo.kafka.to.elastic.service.transformer.AvroToElasticModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {

    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaConsumer.class);

    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final KafkaConfigData kafkaConfigData;

    private final AvroToElasticModelTransformer transformer;

    private final ElasticIndexClient<TwitterIndexModel> elasticIndexClient;

    private final KafkaConsumerConfigData consumerConfigData;

    public TwitterKafkaConsumer(KafkaAdminClient kafkaAdminClient,
                                KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                KafkaConfigData kafkaConfigData,
                                AvroToElasticModelTransformer transformer,
                                ElasticIndexClient<TwitterIndexModel> elasticIndexClient,
                                KafkaConsumerConfigData consumerConfigData) {
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaConfigData = kafkaConfigData;
        this.transformer = transformer;
        this.elasticIndexClient = elasticIndexClient;
        this.consumerConfigData = consumerConfigData;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent applicationStartedEvent){
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics with the name {} is ready for operation:: ", kafkaConfigData.getTopicName());
        Objects.requireNonNull(kafkaListenerEndpointRegistry
                .getListenerContainer(consumerConfigData.getConsumerGroupId())).start();
    }
    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<TwitterAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of message received with key {}, partitions {} and offset {}, "+
                "sending it to elastic: Thread id {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());

        List<TwitterIndexModel> elasticModels = transformer.getElasticModels(messages);
        List<String> documentIds = elasticIndexClient.save(elasticModels);
        log.info("Documents saved to elastic search with ids {}",documentIds.toArray());
    }
}
