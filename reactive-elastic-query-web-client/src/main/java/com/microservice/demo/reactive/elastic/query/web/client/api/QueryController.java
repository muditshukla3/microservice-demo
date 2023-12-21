package com.microservice.demo.reactive.elastic.query.web.client.api;

import com.microservice.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequest;
import com.microservice.demo.elastic.query.web.client.common.model.ElasticQueryWebClientResponse;
import com.microservice.demo.reactive.elastic.query.web.client.service.ElasticQueryWebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class QueryController {

    private final ElasticQueryWebClient elasticQueryWebClient;

    public QueryController(ElasticQueryWebClient elasticQueryWebClient) {
        this.elasticQueryWebClient = elasticQueryWebClient;
    }

    @GetMapping
    public String index(){
        return "index";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("elasticQueryWebClientRequestModel",
                ElasticQueryWebClientRequest.builder().build());
        return "home";
    }

    @PostMapping("/query-by-text")
    public String queryByText(@Valid ElasticQueryWebClientRequest request, Model model){
        log.info("Querying with text {}", request.getText());
        Flux<ElasticQueryWebClientResponse> responseModels =elasticQueryWebClient.getDataByText(request);
        responseModels = responseModels.log();
        IReactiveDataDriverContextVariable reactiveData =
                new ReactiveDataDriverContextVariable(responseModels, 1);

        model.addAttribute("elasticQueryWebClientResponseModels", reactiveData);
        model.addAttribute("searchText",
                request.getText());
        model.addAttribute("elasticQueryWebClientRequestModel",
                ElasticQueryWebClientRequest.builder().build());
        return "home";
    }
}
