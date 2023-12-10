package com.microservice.demo.elastic.query.service.service.impl;

import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.elastic.query.client.service.ElasticQueryClient;
import com.microservice.demo.elastic.query.service.model.ElasticQueryServiceResponse;
import com.microservice.demo.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.microservice.demo.elastic.query.service.service.ElasticQueryService;
import com.microservice.demo.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TwitterElasticQueryService implements ElasticQueryService {

    private final ElasticQueryServiceResponseModelAssembler assembler;

    private final ElasticQueryClient<TwitterIndexModel> queryClient;

    public TwitterElasticQueryService(ElasticQueryServiceResponseModelAssembler assembler,
                                      ElasticQueryClient<TwitterIndexModel> queryClient) {
        this.assembler = assembler;
        this.queryClient = queryClient;
    }

    @Override
    public ElasticQueryServiceResponse getDocumentById(String id) {
        log.info("Querying elastic search by id {}", id);
        return assembler.toModel(queryClient.getIndexModelById(id));
    }

    @Override
    public List<ElasticQueryServiceResponse> getDocumentByText(String text) {
        log.info("Querying elastic search by text {}", text);
        return assembler.toModels(queryClient.getIndexModelByText(text));
    }

    @Override
    public List<ElasticQueryServiceResponse> getAllDocuments() {
        log.info("Querying elastic search for all documents");
        return assembler.toModels(queryClient.getAllIndexModels());
    }
}
