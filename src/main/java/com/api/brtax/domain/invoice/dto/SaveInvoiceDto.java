package com.api.brtax.domain.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SaveInvoiceDto(String invoiceNumber, LocalDate period, BigDecimal value) {}
