package org.maksim.training.mtapp;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SimpleRestClient {
    private static final String RESOURCE_BASE_URL = "http://localhost:8080/rest";

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SimpleRestClient client = new SimpleRestClient();
        ResponseEntity<String> responseEntity = client.get("/users", String.class);
        System.out.println(responseEntity);

        responseEntity = client.get("/events", String.class);
        System.out.println(responseEntity);
    }

    private <T> ResponseEntity<T> get(String url, Class<T> clazz) {
        return restTemplate.getForEntity(RESOURCE_BASE_URL + url, clazz);
    }
}