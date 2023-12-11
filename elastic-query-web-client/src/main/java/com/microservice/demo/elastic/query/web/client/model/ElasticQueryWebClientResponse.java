package com.microservice.demo.elastic.query.web.client.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElasticQueryWebClientResponse {
    private String id;
    private Long userId;
    private String text;
    private LocalDateTime createdAt;
}
