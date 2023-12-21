package com.microservice.demo.reactive.elastic.query.web.client.service.impl;

import com.microservice.demo.config.ElasticQueryWebClientConfigData;
import com.microservice.demo.elastic.query.web.client.common.exception.ElasticQueryClientException;
import com.microservice.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequest;
import com.microservice.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponse;
import com.microservice.demo.reactive.elastic.query.web.client.service.ElasticQueryWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class TwitterElasticWebQueryClient implements ElasticQueryWebClient {

    private final WebClient webClient;

    private final ElasticQueryWebClientConfigData clientConfigData;

    public TwitterElasticWebQueryClient(@Qualifier("webClient") WebClient webClient,
                                        ElasticQueryWebClientConfigData clientConfigData) {
        this.webClient = webClient;
        this.clientConfigData = clientConfigData;
    }

    @Override
    public Flux<ElasticQueryWebClientResponse> getDataByText(ElasticQueryWebClientRequest request) {
        log.info("Querying by text {}", request.getText());
        return getWebClient(request)
                .bodyToFlux(ElasticQueryWebClientResponse.class);
    }
    private WebClient.ResponseSpec getWebClient(ElasticQueryWebClientRequest request){
        return webClient
                .method(HttpMethod.valueOf(clientConfigData.getQueryByText().getMethod()))
                .uri(clientConfigData.getQueryByText().getUri())
                .accept(MediaType.valueOf(clientConfigData.getQueryByText().getAccept()))
                .body(BodyInserters.fromPublisher(Mono.just(request),createParameterizedTypeReference()))
                .retrieve()
                .onStatus(
                        httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not Authorized"))
                )
                .onStatus(
                        HttpStatus::is4xxClientError,
                        cr -> Mono.just(new ElasticQueryClientException(cr.statusCode().getReasonPhrase()))
                )
                .onStatus(
                        HttpStatus::is5xxServerError,
                        cr -> Mono.just(new Exception(cr.statusCode().getReasonPhrase()))
                );
    }

    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<T>() {
        };
    }
}
