package com.api.brtax.domain.taxcalculation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("tax_calculation")
@Getter
public class TaxCalculation {
  @Id @Setter private UUID id;

  @Column("user_id")
  private final UUID userId;

  @Column("calculated_value")
  private final BigDecimal calculatedValue;

  @Column("tax_calculation_period")
  private final LocalDate taxCalculationPeriod;

  @Column("tax_calculation_group_id")
  private final UUID taxCalculationGroupId;

  public TaxCalculation(
      UUID userId,
      BigDecimal calculatedValue,
      LocalDate taxCalculationPeriod,
      UUID taxCalculationGroupId) {
    this.calculatedValue = calculatedValue;
    this.userId = userId;
    this.taxCalculationPeriod = taxCalculationPeriod;
    this.taxCalculationGroupId = taxCalculationGroupId;
  }
}
