package com.microservice.demo.elastic.config;

import com.microservice.demo.config.ElasticConfigData;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.microservice.demo.elastic.index.client.repository")
public class ElasticSearchConfig {

    private final ElasticConfigData elasticConfigData;

    public ElasticSearchConfig(ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }


    @Bean
    public RestHighLevelClient elasticsearchClient() {
        UriComponents serverUri = UriComponentsBuilder
                                        .fromHttpUrl(elasticConfigData.getConnectionUrl())
                                        .build();
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(
                                   Objects.requireNonNull(serverUri.getHost()),
                                   serverUri.getPort(),
                                   serverUri.getScheme()))
                           .setRequestConfigCallback(
                                requestConfigBuilder ->
                                        requestConfigBuilder
                                                .setConnectTimeout(elasticConfigData.getConnectionTimeoutMs())
                                                .setSocketTimeout(elasticConfigData.getSocketTimeoutMs())
                ));
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(){
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }

}
