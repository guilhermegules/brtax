package com.api.brtax.domain.taxcalculation.dto;

import java.time.LocalDate;
import java.util.UUID;

public record StartTaxCalculationDto(UUID userId, LocalDate startPeriod, LocalDate finalPeriod) {}
