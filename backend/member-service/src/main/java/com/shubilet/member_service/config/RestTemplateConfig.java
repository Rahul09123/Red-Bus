package com.shubilet.member_service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Override default error handling
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                // Return true only for status you want to treat as error
                // or false to let controller handle all responses
                return false; // never throw exception, handle all manually
            }

            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                // We won’t throw, because hasError=false
            }
        });

        return restTemplate;
    }


}
