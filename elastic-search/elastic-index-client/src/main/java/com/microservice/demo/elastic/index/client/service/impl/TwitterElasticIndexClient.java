package com.microservice.demo.elastic.index.client.service.impl;

import com.microservice.demo.config.ElasticConfigData;
import com.microservice.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservice.demo.elastic.index.client.util.ElasticIndexUtil;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true")
public class TwitterElasticIndexClient implements ElasticIndexClient<TwitterIndexModel> {

    private static final Logger log = LoggerFactory.getLogger(TwitterElasticIndexClient.class);

    private final ElasticConfigData elasticConfigData;

    private final ElasticsearchOperations elasticsearchOperations;

    private final ElasticIndexUtil<TwitterIndexModel> elasticIndexUtil;

    public TwitterElasticIndexClient(ElasticConfigData elasticConfigData,
                                     ElasticsearchOperations elasticsearchOperations,
                                     ElasticIndexUtil<TwitterIndexModel> elasticIndexUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticIndexUtil = elasticIndexUtil;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<IndexQuery> indexQueries = elasticIndexUtil.getIndexQueries(documents);
        List<IndexedObjectInformation> ioInformation = elasticsearchOperations.bulkIndex(
                indexQueries,
                IndexCoordinates.of(elasticConfigData.getIndexName())
        );
        List<String> documentIds = ioInformation
                                        .stream()
                                        .map(IndexedObjectInformation::getId)
                                        .toList();
        log.info("Documents indexed successfully with type : {} and ids: {}", TwitterIndexModel.class.getName(),
                documentIds);
        return documentIds;
    }
}
