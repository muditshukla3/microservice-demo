package com.microservice.demo.elastic.query.service.common.api.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ElasticQueryErrorHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handle(AccessDeniedException e){
        log.error("Access denied exception occurred {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to access this resource.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handle(IllegalArgumentException e){
        log.error("IllegalArgument exception occurred {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("IllegalArgument exception "+e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException e){
        log.error("Runtime exception occurred {}", e.getMessage());
        return ResponseEntity.badRequest().body("Service Runtime exception" +e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handle(Exception e){
        e.printStackTrace();
        log.error("Exception occurred {}", e.getMessage());
        return ResponseEntity.internalServerError().body("Server error occurred");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException e){
        log.error("Method argument validation exception {}", e.getMessage());
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error ->
               errors.put(((FieldError)error).getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
