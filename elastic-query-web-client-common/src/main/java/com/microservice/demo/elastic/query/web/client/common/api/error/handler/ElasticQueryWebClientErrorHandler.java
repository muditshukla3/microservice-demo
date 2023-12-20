package com.microservice.demo.elastic.query.web.client.common.api.error.handler;

import com.microservice.demo.elastic.query.web.client.common.model.ElasticQueryWebClientRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class ElasticQueryWebClientErrorHandler {

    private static String ERROR = "error";
    private static String ERROR_DESCRIPTION = "error_description";
    @ExceptionHandler(AccessDeniedException.class)
    public String handler(AccessDeniedException e, Model model){
        model.addAttribute(ERROR, HttpStatus.UNAUTHORIZED.getReasonPhrase());
        model.addAttribute(ERROR_DESCRIPTION, "You are not authorized to access this resource");
        return ERROR;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handler(IllegalArgumentException e, Model model){
        model.addAttribute(ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute(ERROR_DESCRIPTION, "Illegal argument exception");
        return ERROR;
    }

    @ExceptionHandler(Exception.class)
    public String handler(Exception e, Model model){
        model.addAttribute(ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        model.addAttribute(ERROR_DESCRIPTION, "A server error occurred");
        return ERROR;
    }

    @ExceptionHandler(RuntimeException.class)
    public String handler(RuntimeException e, Model model){
        model.addAttribute("elasticQueryWebClientRequestModel",
                ElasticQueryWebClientRequest.builder().build());
        model.addAttribute(ERROR, "Could not get response");
        model.addAttribute(ERROR_DESCRIPTION, "Service runtime exception");
        return "home";
    }

    @ExceptionHandler(BindException.class)
    public String handle(BindException e, Model model){
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error ->
                errors.put(((FieldError)error).getField(), error.getDefaultMessage()));
        model.addAttribute("elasticQueryWebClientRequestModel",
                ElasticQueryWebClientRequest.builder().build());
        model.addAttribute(ERROR, HttpStatus.BAD_REQUEST.getReasonPhrase());
        model.addAttribute(ERROR_DESCRIPTION, errors);
        return "home";
    }
}
