package com.microservice.demo.elastic.query.client;

public class ElasticQueryClientException extends RuntimeException{

    public ElasticQueryClientException() {
        super();
    }

    public ElasticQueryClientException(String message) {
        super(message);
    }
}
