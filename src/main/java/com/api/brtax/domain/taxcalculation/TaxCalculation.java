package com.api.brtax.domain.taxcalculation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tax_calculation")
@Getter
public class TaxCalculation {
  @Id
  public UUID id;

  @Column("tax_id")
  private final UUID taxId;

  @Column("user_id")
  private final UUID userId;

  @Column("calculated_value")
  private final BigDecimal calculatedValue;

  // TODO: create tax calculation period field
  public TaxCalculation(UUID taxId, UUID userId, BigDecimal calculatedValue,
      LocalDate taxCalculationPeriod) {
    this.calculatedValue = calculatedValue;
    this.userId = userId;
    this.taxId = taxId;
  }
}
