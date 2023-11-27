package com.microservice.demo.kafka.to.elastic.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.microservice.demo")
public class KafkaToElasticServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaToElasticServiceApplication.class, args);
    }
}
