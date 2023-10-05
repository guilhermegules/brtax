package com.api.brtax.infra.http;

import org.springframework.http.HttpMethod;

public interface HttpAdapter {
  <T> T request(String url, HttpMethod method, Class<T> classType);
}
