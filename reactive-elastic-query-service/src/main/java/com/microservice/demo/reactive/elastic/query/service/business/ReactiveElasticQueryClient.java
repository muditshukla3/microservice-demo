package com.microservice.demo.reactive.elastic.query.service.business;

import com.microservice.demo.elastic.model.index.IndexModel;
import com.microservice.demo.elastic.model.index.impl.TwitterIndexModel;
import reactor.core.publisher.Flux;

public interface ReactiveElasticQueryClient<T extends IndexModel> {

    Flux<TwitterIndexModel> getIndexModelByText(String text);
}
