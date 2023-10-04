package com.api.brtax.domain.invoice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record InvoiceDto(UUID id, String invoiceNumber, LocalDateTime period, BigDecimal value) {}
