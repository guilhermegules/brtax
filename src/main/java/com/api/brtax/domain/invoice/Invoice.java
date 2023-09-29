package com.api.brtax.domain.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "invoice")
public class Invoice {
  @Id
  private UUID id;
  private String invoiceNumber;
  private LocalDateTime period;
  private BigDecimal value;
}
