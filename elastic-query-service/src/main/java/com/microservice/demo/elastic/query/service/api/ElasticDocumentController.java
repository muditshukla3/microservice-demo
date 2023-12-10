package com.microservice.demo.elastic.query.service.api;

import com.microservice.demo.elastic.query.service.model.ElasticQueryServiceRequest;
import com.microservice.demo.elastic.query.service.model.ElasticQueryServiceResponse;
import com.microservice.demo.elastic.query.service.service.ElasticQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping(value = "/documents")
@Slf4j
public class ElasticDocumentController {

    private final ElasticQueryService queryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.queryService = elasticQueryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ElasticQueryServiceResponse>> getAllDocument(){
        List<ElasticQueryServiceResponse> response = queryService.getAllDocuments();
        log.info("ElasticSearch returned {} of documents ", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponse> getDocumentById(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponse response = queryService.getDocumentById(id);
        log.info("ElasticSearch returned document with id {}", id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/get-documents-by-text")
    public ResponseEntity<List<ElasticQueryServiceResponse>> getDocumentsByText(@RequestBody @Valid ElasticQueryServiceRequest request){
        List<ElasticQueryServiceResponse> response = queryService.getDocumentByText(request.getText());
        ElasticQueryServiceResponse elasticQueryServiceResponse = ElasticQueryServiceResponse
                                                                        .builder()
                                                                        .text(request.getText())
                                                                        .build();
        response.add(elasticQueryServiceResponse);
        log.info("ElasticSearch returned {} of documents ", response.size());
        return ResponseEntity.ok(response);
    }
}
