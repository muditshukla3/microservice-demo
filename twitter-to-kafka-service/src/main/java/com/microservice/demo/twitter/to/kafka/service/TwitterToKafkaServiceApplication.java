package com.microservice.demo.twitter.to.kafka.service;

import com.microservice.demo.config.TwitterToKafkaConfigData;
import com.microservice.demo.twitter.to.kafka.service.runner.StreamRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "com.microservice.demo")
public class TwitterToKafkaServiceApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(TwitterToKafkaServiceApplication.class);
    private final TwitterToKafkaConfigData twitterToKafkaConfigData;
    private final StreamRunner streamRunner;

    public TwitterToKafkaServiceApplication(TwitterToKafkaConfigData twitterToKafkaConfigData,
                                            StreamRunner streamRunner) {
        this.twitterToKafkaConfigData = twitterToKafkaConfigData;
        this.streamRunner = streamRunner;
    }

    public static void main(String[] args) {
        SpringApplication.run(TwitterToKafkaServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOG.info("App starts...");
        LOG.info(Arrays.toString(twitterToKafkaConfigData.getTwitterKeywords().toArray(new String[] {})));
        LOG.info(twitterToKafkaConfigData.getWelcomeMessage());
        streamRunner.start();
    }
}
