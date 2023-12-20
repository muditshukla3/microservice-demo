package com.microservice.demo.elastic.query.service.model.assembler;

import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.elastic.query.service.api.ElasticDocumentController;
import com.microservice.demo.elastic.query.service.common.model.ElasticQueryServiceResponse;
import com.microservice.demo.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ElasticQueryServiceResponseModelAssembler
        extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponse> {

    private ElasticToResponseModelTransformer transformer;

    public ElasticQueryServiceResponseModelAssembler(ElasticToResponseModelTransformer transformer) {
        super(ElasticDocumentController.class, ElasticQueryServiceResponse.class);
        this.transformer = transformer;
    }

    @Override
    public ElasticQueryServiceResponse toModel(TwitterIndexModel twitterIndexModel) {
        ElasticQueryServiceResponse responseModel = transformer.getResponseModel(twitterIndexModel);
        responseModel
                .add(linkTo(methodOn(ElasticDocumentController.class).getDocumentById(twitterIndexModel.getId())).withSelfRel());

        responseModel.add(linkTo(ElasticDocumentController.class)
                .withRel("documents"));
        return responseModel;
    }

    public List<ElasticQueryServiceResponse> toModels(List<TwitterIndexModel> twitterIndexModels){
        return twitterIndexModels.stream().map(this::toModel).toList();
    }
}
