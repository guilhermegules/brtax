package com.api.brtax.domain.selic;

import com.api.brtax.api.selic.SelicApiService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelicService {
  private final SelicApiService selicApiService;
  private static final BigDecimal SELIC_DIVIDE_VALUE = new BigDecimal("12");

  public BigDecimal getSelicValue() {
    var selicApiValue = selicApiService.getSelicValue().value();

    return selicApiValue.divide(SELIC_DIVIDE_VALUE, RoundingMode.FLOOR);
  }
}
