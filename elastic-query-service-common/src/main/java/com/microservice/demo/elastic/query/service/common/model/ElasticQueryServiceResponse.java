package com.microservice.demo.elastic.query.service.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElasticQueryServiceResponse extends RepresentationModel<ElasticQueryServiceResponse> {

    private String id;
    private Long userId;
    private String text;
    private LocalDateTime createdAt;
}
