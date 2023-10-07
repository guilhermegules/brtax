package com.api.brtax.controller;

import com.api.brtax.domain.taxcalculation.TaxCalculationService;
import com.api.brtax.domain.taxcalculation.dto.StartTaxCalculationDto;
import com.api.brtax.domain.taxcalculation.dto.TaxCalculationResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("tax-calculation")
@RequiredArgsConstructor
public class TaxCalculationController {
  private final TaxCalculationService taxCalculationService;

  @PostMapping
  public ResponseEntity<TaxCalculationResponseDto> startTaxCalculation(
      @RequestBody StartTaxCalculationDto startTaxCalculationDto, UriComponentsBuilder uriBuilder) {
    final var taxCalculation = taxCalculationService.calculate(startTaxCalculationDto);
    final var uri =
        uriBuilder
            .path("/tax-calculation/{taxCalculationId}")
            .buildAndExpand(taxCalculation.taxCalculationId())
            .toUri();
    return ResponseEntity.created(uri).body(taxCalculation);
  }

  @GetMapping("/{taxCalculationId}")
  public ResponseEntity<TaxCalculationResponseDto> getCalculationById(
      @PathVariable UUID taxCalculationId) {
    final var taxCalculation = taxCalculationService.getById(taxCalculationId);
    return ResponseEntity.ok(taxCalculation);
  }
}
