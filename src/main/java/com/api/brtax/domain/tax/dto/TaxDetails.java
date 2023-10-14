package com.api.brtax.domain.tax.dto;

import com.api.brtax.domain.tax.Tax;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;

@Getter
public class TaxDetails {
  private final UUID id;
  private final String name;
  private final BigDecimal aliquot;

    public TaxDetails(Tax tax) {
      this.aliquot = tax.getAliquot();
      this.id = tax.getId();
      this.name = tax.getName();
    }
}
