package com.microservice.demo.elastic.query.service.service;

import com.microservice.demo.elastic.query.service.model.ElasticQueryServiceResponse;

import java.util.List;

public interface ElasticQueryService {

    ElasticQueryServiceResponse getDocumentById(String id);

    List<ElasticQueryServiceResponse> getDocumentByText(String text);

    List<ElasticQueryServiceResponse> getAllDocuments();
}
