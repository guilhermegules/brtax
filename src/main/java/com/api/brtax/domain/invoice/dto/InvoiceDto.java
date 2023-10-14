package com.api.brtax.domain.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record InvoiceDto(UUID id, String invoiceNumber, LocalDate period, BigDecimal value) {}
