package com.api.brtax.infra.http;

import com.api.brtax.exception.ExternalApiClientException;
import com.api.brtax.exception.ExternalApiServerException;
import java.io.IOException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

@Component
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
    return (httpResponse.getStatusCode().is4xxClientError()
        || httpResponse.getStatusCode().is5xxServerError());
  }

  @Override
  public void handleError(ClientHttpResponse httpResponse) throws IOException {
    if (httpResponse.getStatusCode().is5xxServerError()) {
      throw new ExternalApiServerException(
          String.format(
              "API error: %d, Origin: %s, Error status text: %s",
              httpResponse.getStatusCode().value(),
              httpResponse.getHeaders().getOrigin(),
              httpResponse.getStatusText()));
    }

    if (httpResponse.getStatusCode().is4xxClientError()) {
      throw new ExternalApiClientException(
          String.format(
              "API client error: %d, Origin: %s, Error status text: %s",
              httpResponse.getStatusCode().value(),
              httpResponse.getHeaders().getOrigin(),
              httpResponse.getStatusText()));
    }
  }
}
