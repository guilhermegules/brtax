package com.api.brtax.domain.selic;

import com.api.brtax.api.selic.SelicApiService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SelicServiceTest {

  @InjectMocks SelicService selicService;

  @Mock SelicApiService selicApiService;

  @BeforeEach
  public void init() {
    selicService = new SelicService(selicApiService);
  }

  @Test
  public void shouldGetSelicValueAndDivideBy12() {
    Mockito.when(selicApiService.getSelicValue())
        .thenReturn(new Selic("SELIC", new BigDecimal("10")));

    Assertions.assertEquals(
        selicService.getSelicValue(),
        new BigDecimal("10").divide(new BigDecimal("12"), RoundingMode.FLOOR));
  }
}
