package com.microservice.demo.elastic.query.service.security;

import com.microservice.demo.config.ElasticQueryServiceConfigData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Qualifier("elastic-query-service-audience-validator")
@Component
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final ElasticQueryServiceConfigData configData;

    public AudienceValidator(ElasticQueryServiceConfigData configData) {
        this.configData = configData;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        if(token.getAudience().contains(configData.getCustomAudience())){
            return OAuth2TokenValidatorResult.success();
        }else{
            OAuth2Error audienceError =
                    new OAuth2Error("invalid-token", "The required audience "
                            +configData.getCustomAudience()+" is missing", null);
            return OAuth2TokenValidatorResult.failure(audienceError);
        }
    }
}
