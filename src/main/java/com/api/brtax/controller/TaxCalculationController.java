package com.api.brtax.controller;

import com.api.brtax.domain.taxcalculation.TaxCalculationService;
import com.api.brtax.domain.taxcalculation.dto.StartTaxCalculationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tax-calculation")
@RequiredArgsConstructor
public class TaxCalculationController {
  private final TaxCalculationService taxCalculationService;
  @PostMapping
  public ResponseEntity<Object> startTaxCalculation(@RequestBody StartTaxCalculationDto startTaxCalculationDto) {
    taxCalculationService.calculate(startTaxCalculationDto);
    // TODO: add a properly response
    return ResponseEntity.ok(null);
  }
}
