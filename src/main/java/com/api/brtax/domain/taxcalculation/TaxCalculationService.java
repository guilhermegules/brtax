package com.api.brtax.domain.taxcalculation;

import com.api.brtax.domain.invoice.InvoiceService;
import com.api.brtax.domain.invoice.dto.InvoiceDetails;
import com.api.brtax.domain.selic.SelicService;
import com.api.brtax.domain.tax.TaxService;
import com.api.brtax.domain.tax.dto.TaxDetails;
import com.api.brtax.domain.taxcalculation.dto.StartTaxCalculationDto;
import com.api.brtax.domain.taxcalculation.dto.TaxCalculationResponseDto;
import com.api.brtax.domain.user.UserService;
import com.api.brtax.domain.user.UserType;
import com.api.brtax.exception.BusinessException;
import com.api.brtax.exception.NotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaxCalculationService {
  private final TaxCalculationRepository taxCalculationRepository;
  private final UserService userService;
  private final TaxService taxService;
  private final InvoiceService invoiceService;
  private final SelicService selicService;

  @Transactional
  public TaxCalculationResponseDto calculate(StartTaxCalculationDto startTaxCalculationDto) {
    final var user = userService.getUserById(startTaxCalculationDto.userId());

    if (user.type() != UserType.MANAGER) {
      final var message =
          String.format(
              "Tax calculation only should be trigger by users with type: %s!", UserType.MANAGER);
      throw new BusinessException(message);
    }

    final var taxes = taxService.getAll();

    final var invoices =
        invoiceService.getInvoicesInRange(
            startTaxCalculationDto.startPeriod(), startTaxCalculationDto.finalPeriod());

    if (invoices.isEmpty()) {
      final var message =
          String.format(
              "Tax calculation cannot be triggered without invoices in range! start period: %s, final period: %s",
              startTaxCalculationDto.startPeriod(), startTaxCalculationDto.finalPeriod());
      throw new BusinessException(message);
    }

    final var selicValue = selicService.getSelicValue();

    final var totalCalculation =
        invoices.stream()
            .reduce(
                BigDecimal.ZERO,
                (taxTotalCalculation, invoice) ->
                    taxTotalCalculation.add(taxesReducer(invoice, taxes, selicValue)),
                BigDecimal::add);

    final var taxCalculation =
        new TaxCalculation(startTaxCalculationDto.userId(), totalCalculation, LocalDate.now());

    final var taxCalculationSaved = taxCalculationRepository.save(taxCalculation);

    return new TaxCalculationResponseDto(
        taxCalculationSaved.getId(),
        taxCalculationSaved.getUserId(),
        taxCalculationSaved.getTaxCalculationPeriod(),
        taxCalculationSaved.getCalculatedValue());
  }

  public TaxCalculationResponseDto getById(UUID taxCalculationId) {
    return taxCalculationRepository
        .findById(taxCalculationId)
        .map(
            taxCalculation ->
                new TaxCalculationResponseDto(
                    taxCalculation.getId(),
                    taxCalculation.getUserId(),
                    taxCalculation.getTaxCalculationPeriod(),
                    taxCalculation.getCalculatedValue()))
        .orElseThrow(
            () ->
                new NotFoundException(
                    "Tax calculation not found with given ID: " + taxCalculationId));
  }

  private BigDecimal taxesReducer(
      InvoiceDetails invoice, List<TaxDetails> taxes, BigDecimal selicValue) {
    return taxes.stream()
        .reduce(
            BigDecimal.ZERO,
            (taxValueAccumulator, tax) ->
                taxValueAccumulator.add(totalTaxCalculationReducer(invoice, selicValue, tax)),
            BigDecimal::add);
  }

  private BigDecimal totalTaxCalculationReducer(
      InvoiceDetails invoice, BigDecimal selicValue, TaxDetails tax) {
    return invoice.value().multiply(tax.getAliquot().add(selicValue));
  }
}
