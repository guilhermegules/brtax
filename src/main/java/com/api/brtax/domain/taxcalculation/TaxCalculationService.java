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
  public List<TaxCalculationResponseDto> calculate(StartTaxCalculationDto startTaxCalculationDto) {
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

    final var taxCalculationGroupId = UUID.randomUUID();

    return invoices.stream()
        .map(
            invoice -> {
              final var invoiceCalculated =
                  invoice.value().add(taxesReducer(invoice, taxes, selicValue));
              final var taxCalculation =
                  new TaxCalculation(
                      startTaxCalculationDto.userId(),
                      invoiceCalculated,
                      invoice.period(),
                      taxCalculationGroupId);
              final var taxCalculationSaved = taxCalculationRepository.save(taxCalculation);
              return new TaxCalculationResponseDto(
                  taxCalculationSaved.getId(),
                  taxCalculationSaved.getUserId(),
                  taxCalculationSaved.getTaxCalculationPeriod(),
                  taxCalculationSaved.getCalculatedValue(),
                  taxCalculation.getTaxCalculationGroupId());
            })
        .collect(Collectors.toList());
  }

  public List<TaxCalculationResponseDto> getByTaxCalculationGroupId(UUID taxCalculationGroupId) {
    return taxCalculationRepository
        .findAllTaxCalculationByTaxCalculationGroupId(taxCalculationGroupId)
        .map(
            taxCalculation ->
                taxCalculation.stream()
                    .map(
                        calculation ->
                            new TaxCalculationResponseDto(
                                calculation.getId(),
                                calculation.getUserId(),
                                calculation.getTaxCalculationPeriod(),
                                calculation.getCalculatedValue(),
                                calculation.getTaxCalculationGroupId())))
        .orElseThrow(
            () ->
                new NotFoundException(
                    "Tax calculation not found with given group ID: " + taxCalculationGroupId))
        .collect(Collectors.toList());
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
