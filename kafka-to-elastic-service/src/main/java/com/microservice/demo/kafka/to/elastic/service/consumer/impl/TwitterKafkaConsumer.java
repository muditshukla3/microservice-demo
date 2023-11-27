package com.microservice.demo.kafka.to.elastic.service.consumer.impl;

import com.microservice.demo.config.KafkaConfigData;
import com.microservice.demo.kafka.admin.client.KafkaAdminClient;
import com.microservice.demo.kafka.avro.model.TwitterAvroModel;
import com.microservice.demo.kafka.to.elastic.service.consumer.KafkaConsumer;
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

@Component
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {

    private static final Logger log = LoggerFactory.getLogger(TwitterKafkaConsumer.class);

    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    private final KafkaConfigData kafkaConfigData;

    public TwitterKafkaConsumer(KafkaAdminClient kafkaAdminClient,
                                KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                KafkaConfigData kafkaConfigData) {
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaConfigData = kafkaConfigData;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent applicationStartedEvent){
        kafkaAdminClient.checkTopicsCreated();
        log.info("Topics with the name {} is ready for operation:: ", kafkaConfigData.getTopicName());
        kafkaListenerEndpointRegistry.getListenerContainer("twitterTopicListener").start();
    }
    @Override
    @KafkaListener(id = "twitterTopicListener", topics = "${kafka-config.topic-name}")
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
    }
}
