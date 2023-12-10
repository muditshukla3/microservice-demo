package com.microservice.demo.elastic.query.service.transformer;

import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import com.microservice.demo.elastic.query.service.model.ElasticQueryServiceResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponse getResponseModel(TwitterIndexModel twitterIndexModel){
        return ElasticQueryServiceResponse.builder()
                .id(twitterIndexModel.getId())
                .userId(twitterIndexModel.getUserId())
                .text(twitterIndexModel.getText())
                .createdAt(twitterIndexModel.getCreatedAt().toLocalDateTime())
                .build();
    }

    public List<ElasticQueryServiceResponse> getResponseModel(List<TwitterIndexModel> twitterIndexModels){
        return twitterIndexModels
                        .stream()
                        .map(this::getResponseModel)
                        .collect(Collectors.toList());
    }
}
