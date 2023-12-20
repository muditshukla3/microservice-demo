package com.microservice.demo.reactive.elastic.query.service.business.impl;

import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.elastic.query.service.common.model.ElasticQueryServiceResponse;
import com.microservice.demo.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import com.microservice.demo.reactive.elastic.query.service.business.ElasticQueryService;
import com.microservice.demo.reactive.elastic.query.service.business.ReactiveElasticQueryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class TwitterElasticQueryService implements ElasticQueryService {

    private final ReactiveElasticQueryClient<TwitterIndexModel> reactiveElasticQueryClient;
    private final ElasticToResponseModelTransformer transformer;

    public TwitterElasticQueryService(ReactiveElasticQueryClient<TwitterIndexModel> reactiveElasticQueryClient,
                                      ElasticToResponseModelTransformer transformer) {
        this.reactiveElasticQueryClient = reactiveElasticQueryClient;
        this.transformer = transformer;
    }

    @Override
    public Flux<ElasticQueryServiceResponse> getDocumentByText(String text) {
        log.info("Querying reactive elasticsearch for text {}", text);
        return reactiveElasticQueryClient.getIndexModelByText(text)
                .map(transformer::getResponseModel);
    }
}
