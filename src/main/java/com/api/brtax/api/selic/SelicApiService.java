package com.api.brtax.api.selic;

import com.api.brtax.domain.selic.Selic;
import com.api.brtax.domain.selic.SelicApiResponse;
import com.api.brtax.infra.http.RestTemplateHttp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelicApiService {
  private final RestTemplateHttp restTemplateHttp;

  private final static String SELIC_API_URL = "https://brasilapi.com.br/api/taxas/v1/SELIC";

  public Selic getSelicValue() {
    var request = restTemplateHttp.request(SELIC_API_URL, HttpMethod.GET, SelicApiResponse.class);
    return new Selic(request.nome(), request.valor());
  }
}
