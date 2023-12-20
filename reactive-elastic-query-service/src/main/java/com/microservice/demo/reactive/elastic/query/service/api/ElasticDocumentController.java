package com.microservice.demo.reactive.elastic.query.service.api;

import com.microservice.demo.elastic.query.service.common.model.ElasticQueryServiceRequest;
import com.microservice.demo.elastic.query.service.common.model.ElasticQueryServiceResponse;
import com.microservice.demo.reactive.elastic.query.service.business.ElasticQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/documents")
@Slf4j
public class ElasticDocumentController {

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @PostMapping(value = "/get-doc-by-text",
                produces = MediaType.TEXT_EVENT_STREAM_VALUE,
                consumes = MediaType.APPLICATION_JSON_VALUE)
    public Flux<ElasticQueryServiceResponse> getDocumentByText(
            @RequestBody @Valid ElasticQueryServiceRequest request){

        Flux<ElasticQueryServiceResponse> response =
                elasticQueryService.getDocumentByText(request.getText());
        response = response.log();
        log.info("Returning from query reactive service for text {}", request.getText());
        return response;

    }
}
