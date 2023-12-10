package com.microservice.demo.elastic.query.service.service.impl;

import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservice.demo.elastic.query.service.model.ElasticQueryServiceResponse;
import com.microservice.demo.elastic.query.service.service.ElasticQueryService;
import com.microservice.demo.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TwitterElasticQueryService implements ElasticQueryService {

    private final ElasticToResponseModelTransformer transformer;

    private final ElasticQueryClient<TwitterIndexModel> queryClient;

    public TwitterElasticQueryService(ElasticToResponseModelTransformer transformer,
                                      ElasticQueryClient<TwitterIndexModel> queryClient) {
        this.transformer = transformer;
        this.queryClient = queryClient;
    }

    @Override
    public ElasticQueryServiceResponse getDocumentById(String id) {
        log.info("Querying elastic search by id {}", id);
        return transformer.getResponseModel(queryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryServiceResponse> getDocumentByText(String text) {
        log.info("Querying elastic search by text {}", text);
        return transformer.getResponseModel(queryClient.getIndexModelByText(text));
    }

    @Override
    public List<ElasticQueryServiceResponse> getAllDocuments() {
        log.info("Querying elastic search for all documents");
        return transformer.getResponseModel(queryClient.getAllIndexModels());
    }
}
