package com.microservice.demo.elastic.index.client.service.impl;

import com.microservice.demo.elastic.index.client.repository.TwitterElasticSearchIndexRepository;
import com.microservice.demo.elastic.index.client.service.ElasticIndexClient;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@ConditionalOnProperty(name = "elastic-config.is-repository", havingValue = "true", matchIfMissing = true)
public class TwitterElasticRepositoryIndexClient implements ElasticIndexClient<TwitterIndexModel> {
    private static final Logger log = LoggerFactory.getLogger(TwitterElasticRepositoryIndexClient.class);

    private final TwitterElasticSearchIndexRepository twitterElasticSearchIndexRepository;

    public TwitterElasticRepositoryIndexClient(TwitterElasticSearchIndexRepository twitterElasticSearchIndexRepository) {
        this.twitterElasticSearchIndexRepository = twitterElasticSearchIndexRepository;
    }

    @Override
    public List<String> save(List<TwitterIndexModel> documents) {
        List<TwitterIndexModel> repositoryResponse = (List<TwitterIndexModel>) twitterElasticSearchIndexRepository.saveAll(documents);
        List<String> documentIds = repositoryResponse.stream().map(TwitterIndexModel::getId).toList();
        log.info("Documents indexed successfully with type : {} and ids: {}", TwitterIndexModel.class.getName(),
                documentIds);
        return documentIds;
    }
}
