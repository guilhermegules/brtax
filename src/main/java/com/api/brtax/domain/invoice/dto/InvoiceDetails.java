package com.api.brtax.domain.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InvoiceDetails(String invoiceNumber, LocalDateTime period, BigDecimal value) {}
