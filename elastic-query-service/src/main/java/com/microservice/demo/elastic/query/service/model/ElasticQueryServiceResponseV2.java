package com.microservice.demo.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElasticQueryServiceResponseV2 extends RepresentationModel<ElasticQueryServiceResponseV2> {

    private Long id;
    private Long userId;
    private String text;
}
