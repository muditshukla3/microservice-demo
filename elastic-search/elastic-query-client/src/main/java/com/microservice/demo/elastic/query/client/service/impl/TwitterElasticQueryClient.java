package com.microservice.demo.elastic.query.client.service.impl;

import com.microservice.demo.config.ElasticConfigData;
import com.microservice.demo.config.ElasticQueryConfigData;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.elastic.query.client.ElasticQueryClientException;
import com.microservice.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservice.demo.elastic.query.client.util.ElasticQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TwitterElasticQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private static final Logger log = LoggerFactory.getLogger(TwitterElasticQueryClient.class);

    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil;

    public TwitterElasticQueryClient(ElasticConfigData configData,
                                     ElasticQueryConfigData queryConfigData,
                                     ElasticsearchOperations elasticsearchOperations,
                                     ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil) {
        this.elasticConfigData = configData;
        this.elasticQueryConfigData = queryConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticQueryUtil = elasticQueryUtil;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Query query = elasticQueryUtil.getSearchQueryById(id);
        SearchHit<TwitterIndexModel> searchHit = elasticsearchOperations.searchOne(query, TwitterIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        if(searchHit == null){
            throw new ElasticQueryClientException("No document found at elasticsearch with id "+id);
        }
        log.info("Document with id {} retreived successfully", searchHit.getId());
        return searchHit.getContent();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        Query query = elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(), text);
        return search(query, "{} of documents with text {} retrieved successfully",  text);
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Query query = elasticQueryUtil.getSearchQueryForAll();
        return search(query, "{} number of documents retrieved successfully");
    }

    private List<TwitterIndexModel> search(Query query, String logMessage, Object... logParams){
        SearchHits<TwitterIndexModel> searchHits = elasticsearchOperations.search(query, TwitterIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));
        log.info(logMessage, searchHits.getTotalHits(), logParams);

        return searchHits
                .get()
                .map(SearchHit::getContent)
                .toList();
    }
}
