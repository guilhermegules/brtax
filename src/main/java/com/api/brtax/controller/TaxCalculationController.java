package com.api.brtax.controller;

import com.api.brtax.domain.taxcalculation.TaxCalculationService;
import com.api.brtax.domain.taxcalculation.dto.StartTaxCalculationDto;
import com.api.brtax.domain.taxcalculation.dto.TaxCalculationResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
  public ResponseEntity<List<TaxCalculationResponseDto>> startTaxCalculation(
      @RequestBody StartTaxCalculationDto startTaxCalculationDto, UriComponentsBuilder uriBuilder) {
    final var taxCalculations = taxCalculationService.calculate(startTaxCalculationDto);
    final var groupId = taxCalculations.get(0).taxCalculationGroupId();
    final var uri =
        uriBuilder.path("/tax-calculation/{taxCalculationGroupId}").buildAndExpand(groupId).toUri();
    return ResponseEntity.created(uri).body(taxCalculations);
  }

  @GetMapping("/{taxCalculationGroupId}")
  public ResponseEntity<List<TaxCalculationResponseDto>> getCalculationById(
      @PathVariable UUID taxCalculationGroupId) {
    final var taxCalculations =
        taxCalculationService.getByTaxCalculationGroupId(taxCalculationGroupId);
    return ResponseEntity.ok(taxCalculations);
  }
}
