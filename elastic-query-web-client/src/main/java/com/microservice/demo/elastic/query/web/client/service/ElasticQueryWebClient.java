package com.microservice.demo.elastic.query.web.client.service;

import com.microservice.demo.elastic.query.web.client.model.ElasticQueryWebClientRequest;
import com.microservice.demo.elastic.query.web.client.model.ElasticQueryWebClientResponse;

import java.util.List;

public interface ElasticQueryWebClient {

    List<ElasticQueryWebClientResponse> getDataByText(ElasticQueryWebClientRequest request);
}
