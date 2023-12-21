package com.microservice.demo.reactive.elastic.query.web.client.service;

import com.microservice.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequest;
import com.microservice.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponse;
import reactor.core.publisher.Flux;

public interface ElasticQueryWebClient {

    Flux<ElasticQueryWebClientResponse> getDataByText(ElasticQueryWebClientRequest request);
}
