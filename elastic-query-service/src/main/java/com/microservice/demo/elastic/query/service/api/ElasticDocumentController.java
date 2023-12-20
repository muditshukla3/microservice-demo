package com.microservice.demo.elastic.query.service.api;

import com.microservice.demo.elastic.query.service.common.model.ElasticQueryServiceRequest;
import com.microservice.demo.elastic.query.service.common.model.ElasticQueryServiceResponse;
import com.microservice.demo.elastic.query.service.model.ElasticQueryServiceResponseV2;
import com.microservice.demo.elastic.query.service.service.ElasticQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
@Slf4j
public class ElasticDocumentController {

    private final ElasticQueryService queryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.queryService = elasticQueryService;
    }

    @Operation(summary = "Get all elastic documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                    schema = @Schema(implementation = ElasticQueryServiceResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @GetMapping("/")
    public ResponseEntity<List<ElasticQueryServiceResponse>> getAllDocument(){
        List<ElasticQueryServiceResponse> response = queryService.getAllDocuments();
        log.info("ElasticSearch returned {} of documents ", response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get elastic document by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ElasticQueryServiceResponse> getDocumentById(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponse response = queryService.getDocumentById(id);
        log.info("ElasticSearch returned document with id {}", id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get elastic document by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/vnd.api.v2+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseV2.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public ResponseEntity<ElasticQueryServiceResponseV2> getDocumentByIdV2(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponse response = queryService.getDocumentById(id);
        ElasticQueryServiceResponseV2 responseV2 = getV2Model(response);
        log.info("V2 ElasticSearch returned document with id {} user id {}", id, response.getUserId());
        return ResponseEntity.ok(responseV2);
    }

    private ElasticQueryServiceResponseV2 getV2Model(ElasticQueryServiceResponse response) {
        ElasticQueryServiceResponseV2 responseV2 = ElasticQueryServiceResponseV2.builder()
                .id(Long.parseLong(response.getId()))
                .userId(response.getUserId())
                .text(response.getText())
                .build();
        responseV2
                .add(linkTo(methodOn(ElasticDocumentController.class).getDocumentById(response.getId())).withSelfRel());

        responseV2.add(linkTo(ElasticDocumentController.class)
                .withRel("documents"));
        return responseV2;
    }

    @Operation(summary = "Get elastic document by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {
                    @Content(mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
    })
    @PostMapping("/get-documents-by-text")
    public ResponseEntity<List<ElasticQueryServiceResponse>> getDocumentsByText(@RequestBody @Valid ElasticQueryServiceRequest request){
        List<ElasticQueryServiceResponse> response = queryService.getDocumentByText(request.getText());
        log.info("ElasticSearch returned {} of documents ", response.size());
        return ResponseEntity.ok(response);
    }
}
