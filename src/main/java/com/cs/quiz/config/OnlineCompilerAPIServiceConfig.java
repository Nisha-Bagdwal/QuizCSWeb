package com.cs.quiz.config;

import com.cs.quiz.service.OnlineCompilerAPIService;
import com.cs.quiz.service.impl.OnlineCompilerAPIServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OnlineCompilerAPIServiceConfig {

    @Bean
    public OnlineCompilerAPIService onlineCompilerAPIService(
            RestTemplate restTemplate) {
        return new OnlineCompilerAPIServiceImpl(restTemplate);
    }
}
