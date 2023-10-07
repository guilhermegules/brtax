package com.api.brtax.domain.taxcalculation;

import com.api.brtax.domain.invoice.InvoiceService;
import com.api.brtax.domain.invoice.dto.InvoiceDetails;
import com.api.brtax.domain.selic.SelicService;
import com.api.brtax.domain.tax.TaxService;
import com.api.brtax.domain.tax.dto.TaxDetails;
import com.api.brtax.domain.taxcalculation.dto.StartTaxCalculationDto;
import com.api.brtax.domain.user.UserService;
import com.api.brtax.domain.user.UserType;
import com.api.brtax.exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
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
  public void calculate(StartTaxCalculationDto startTaxCalculationDto) {
    final var user = userService.getUserById(startTaxCalculationDto.userId());

    if (user.type() != UserType.MANAGER) {
      throw new BusinessException("Tax calculation only should be trigger by managers!");
    }

    final var taxes = taxService.getAll();

    final var invoices =
        invoiceService.getInvoicesInRange(
            startTaxCalculationDto.startPeriod(), startTaxCalculationDto.finalPeriod());

    final var selicValue = selicService.getSelicValue();

    var totalCalculation =
        invoices.stream()
            .reduce(
                BigDecimal.ZERO,
                (taxTotalCalculation, invoice) ->
                    taxTotalCalculation.add(taxesReducer(invoice, taxes, selicValue)),
                BigDecimal::add);

    var taxCalculation =
        new TaxCalculation(
            UUID.fromString("2c4f09eb-81e3-4275-9613-d0e920b30b8e"), startTaxCalculationDto.userId(), totalCalculation, LocalDate.now());

    taxCalculationRepository.save(taxCalculation);
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
