package com.api.brtax.domain.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "invoice")
@Getter
@Setter
public class Invoice {
  @Id
  private UUID id;
  private final String invoiceNumber;
  private final LocalDateTime period;
  private final BigDecimal value;

  public Invoice(String invoiceNumber, LocalDateTime period, BigDecimal value) {
    this.invoiceNumber = invoiceNumber;
    this.period = period;
    this.value = value;
  }
}
