package com.api.brtax.domain.taxcalculation.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TaxCalculationResponseDto(
    UUID taxCalculationId,
    UUID userRequesterId,
    LocalDate taxCalculationDate,
    BigDecimal taxCalculationValue,
    UUID taxCalculationGroupId) {}
