package com.api.brtax.infra.http;

import lombok.AllArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
public class RestTemplateHttp implements HttpAdapter {

  RestTemplateBuilder restTemplateBuilder;

  @Override
  public <T> T request(String url, HttpMethod httpMethod, Class<T> classType) {
    return restTemplate().exchange(url, httpMethod, null, classType).getBody();
  }

  @Bean
  public RestTemplate restTemplate() {
    return restTemplateBuilder.errorHandler(new RestTemplateResponseErrorHandler()).build();
  }
}
