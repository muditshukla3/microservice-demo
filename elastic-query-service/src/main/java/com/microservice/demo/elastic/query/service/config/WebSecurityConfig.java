package com.microservice.demo.elastic.query.service.config;

import com.microservice.demo.config.UserConfigData;
import com.microservice.demo.elastic.query.service.security.TwitterQueryUserDetailsService;
import com.microservice.demo.elastic.query.service.security.TwitterQueryUserJwtConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TwitterQueryUserDetailsService userDetailsService;
    private final OAuth2ResourceServerProperties resourceServerProperties;

    @Value("${security.paths-to-ignore}")
    private String[] pathsToIgnore;

    public WebSecurityConfig(TwitterQueryUserDetailsService userDetailsService,
                             OAuth2ResourceServerProperties resourceServerProperties) {
        this.userDetailsService = userDetailsService;
        this.resourceServerProperties = resourceServerProperties;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .anyRequest()
                .fullyAuthenticated()
                .and()
                .oauth2ResourceServer()
                .jwt()
                .jwtAuthenticationConverter(twitterQueryUserJwtConverter());
    }

    private Converter<Jwt,? extends AbstractAuthenticationToken> twitterQueryUserJwtConverter() {
        return new TwitterQueryUserJwtConverter(userDetailsService);
    }

    @Bean
    JwtDecoder jwtDecoder(@Qualifier("elastic-query-service-audience-validator")
                          OAuth2TokenValidator<Jwt> audienceValidator){
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(
                resourceServerProperties.getJwt().getIssuerUri());
        OAuth2TokenValidator<Jwt> withIssuer =
                JwtValidators.createDefaultWithIssuer(
                        resourceServerProperties.getJwt().getIssuerUri()
                );
        OAuth2TokenValidator<Jwt> withAudience =
                new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(pathsToIgnore);
    }


    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
