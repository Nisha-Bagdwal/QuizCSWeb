package com.cs.quiz.service.impl;

import com.cs.quiz.service.JDoodleAPIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JDoodleAPIServiceImpl implements JDoodleAPIService {

    private final String clientId;
    private final String clientSecret;
    private final RestTemplate restTemplate;

    public JDoodleAPIServiceImpl(
            @Value("${jdoodle.api.client-id}") String clientId,
            @Value("${jdoodle.api.client-secret}") String clientSecret,
            RestTemplate restTemplate) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.restTemplate = restTemplate;
    }

    public String executeCode(String code, String stdin, String language, String versionIndex) {
        String url = "https://api.jdoodle.com/v1/execute";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        code  = code.replaceAll("[\n\r\t]+", " ").replaceAll("\"", "\\\\\"");

        // Create the request body with your code and JDoodle API credentials.
        String requestBody = String.format(
                        "{\"clientId\": \"%s\", " +
                        "\"clientSecret\": \"%s\", " +
                        "\"script\": \"%s\", " +
                        "\"stdin\": \"%s\", " +
                        "\"language\": \"%s\", " +
                        "\"versionIndex\": \"%s\"}",
                clientId,
                clientSecret,
                code,
                stdin,
                language,
                versionIndex
        );

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            // Handle error cases here
            return "Error: " + response.getStatusCode();
        }
    }
}