package com.microservice.demo.reactive.elastic.query.service.business;

import com.microservice.demo.elastic.query.service.common.model.ElasticQueryServiceResponse;
import reactor.core.publisher.Flux;

public interface ElasticQueryService {
    Flux<ElasticQueryServiceResponse> getDocumentByText(String text);
}
