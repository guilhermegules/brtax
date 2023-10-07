package com.api.brtax.domain.invoice;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "invoice")
@Getter
@Setter
public class Invoice {
  @Id
  private UUID id;
  @Column("invoice_number")
  private final String invoiceNumber;
  private final LocalDate period;
  private final BigDecimal value;

  public Invoice(String invoiceNumber, LocalDate period, BigDecimal value) {
    this.invoiceNumber = invoiceNumber;
    this.period = period;
    this.value = value;
  }
}
