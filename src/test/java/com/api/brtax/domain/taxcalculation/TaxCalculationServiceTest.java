package com.api.brtax.domain.taxcalculation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.api.brtax.domain.invoice.InvoiceService;
import com.api.brtax.domain.invoice.dto.InvoiceDetails;
import com.api.brtax.domain.selic.SelicService;
import com.api.brtax.domain.tax.Tax;
import com.api.brtax.domain.tax.TaxService;
import com.api.brtax.domain.tax.dto.TaxDetails;
import com.api.brtax.domain.taxcalculation.dto.StartTaxCalculationDto;
import com.api.brtax.domain.taxcalculation.dto.TaxCalculationResponseDto;
import com.api.brtax.domain.user.UserService;
import com.api.brtax.domain.user.UserType;
import com.api.brtax.domain.user.dto.UserDetails;
import com.api.brtax.exception.BusinessException;
import com.api.brtax.exception.NotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TaxCalculationServiceTest {
  @InjectMocks TaxCalculationService taxCalculationService;
  @Mock TaxCalculationRepository taxCalculationRepository;
  @Mock UserService userService;
  @Mock TaxService taxService;
  @Mock InvoiceService invoiceService;
  @Mock SelicService selicService;

  @BeforeEach
  public void init() {
    taxCalculationService =
        new TaxCalculationService(
            taxCalculationRepository, userService, taxService, invoiceService, selicService);
  }

  @Test
  public void shouldReturnAnMappedListOfTaxCalculationResponseDto() {
    var taxCalculation =
        new TaxCalculation(
            UUID.randomUUID(), new BigDecimal("100"), LocalDate.now(), UUID.randomUUID());
    taxCalculation.setId(UUID.randomUUID());
    Mockito.when(taxCalculationRepository.findAllTaxCalculationByTaxCalculationGroupId(any()))
        .thenReturn(Optional.of(List.of(taxCalculation)));

    var taxCalculationResponses =
        taxCalculationService.getByTaxCalculationGroupId(UUID.randomUUID());

    Assertions.assertEquals(
        taxCalculationResponses,
        List.of(
            new TaxCalculationResponseDto(
                taxCalculation.getId(),
                taxCalculation.getUserId(),
                taxCalculation.getTaxCalculationPeriod(),
                taxCalculation.getCalculatedValue(),
                taxCalculation.getTaxCalculationGroupId())));
  }

  @Test
  public void shouldThrowNotFoundExceptionWhenNotFoundId() {
    Mockito.when(taxCalculationRepository.findAllTaxCalculationByTaxCalculationGroupId(any()))
        .thenReturn(Optional.empty());
    var uuid = UUID.randomUUID();

    NotFoundException notFoundException =
        Assertions.assertThrows(
            NotFoundException.class,
            () -> {
              taxCalculationService.getByTaxCalculationGroupId(uuid);
            });

    Assertions.assertEquals(
        notFoundException.getMessage(), "Tax calculation not found with given group ID: " + uuid);
  }

  @Test
  public void shouldThrowBusinessExceptionWhenUserTypeIsNotManager() {
    Mockito.when(userService.getUserById(any()))
        .thenReturn(new UserDetails(UUID.randomUUID(), "mock user", "123", UserType.ACCOUNTANT));

    BusinessException businessException =
        Assertions.assertThrows(
            BusinessException.class,
            () -> {
              taxCalculationService.calculate(
                  new StartTaxCalculationDto(UUID.randomUUID(), LocalDate.now(), LocalDate.now()));
            });

    Assertions.assertEquals(
        businessException.getMessage(),
        String.format(
            "Tax calculation only should be trigger by users with type: %s!", UserType.MANAGER));
  }

  @Test
  public void shouldThrowBusinessExceptionWhenNotHaveInvoicesInRange() {
    Mockito.when(userService.getUserById(any()))
        .thenReturn(new UserDetails(UUID.randomUUID(), "mock user", "123", UserType.MANAGER));

    Mockito.when(invoiceService.getInvoicesInRange(any(), any())).thenReturn(List.of());

    var startTaxCalculationDto =
        new StartTaxCalculationDto(UUID.randomUUID(), LocalDate.now(), LocalDate.now());

    BusinessException businessException =
        Assertions.assertThrows(
            BusinessException.class,
            () -> {
              taxCalculationService.calculate(startTaxCalculationDto);
            });

    Assertions.assertEquals(
        businessException.getMessage(),
        String.format(
            "Tax calculation cannot be triggered without invoices in range! start period: %s, final period: %s",
            startTaxCalculationDto.startPeriod(), startTaxCalculationDto.finalPeriod()));
  }

  @Test
  public void shouldCalculateAllInvoicesValues() {
    Mockito.when(userService.getUserById(any()))
        .thenReturn(new UserDetails(UUID.randomUUID(), "mock user", "123", UserType.MANAGER));

    var invoiceInRange1 = new InvoiceDetails("123", LocalDate.now(), new BigDecimal("100"));

    Mockito.when(invoiceService.getInvoicesInRange(any(), any()))
        .thenReturn(List.of(invoiceInRange1));

    Mockito.when(selicService.getSelicValue()).thenReturn(new BigDecimal("1.70"));

    Mockito.when(taxService.getAll())
        .thenReturn(
            List.of(
                new TaxDetails(new Tax(UUID.randomUUID(), "SIMPLESN", new BigDecimal("2.5"))),
                new TaxDetails(new Tax(UUID.randomUUID(), "ISS", new BigDecimal("1.5")))));

    var taxCalculationsSaved =
        List.of(
            new TaxCalculation(
                UUID.randomUUID(), new BigDecimal("100"), LocalDate.now(), UUID.randomUUID()));
    Mockito.when(taxCalculationRepository.saveAll(any())).thenReturn(taxCalculationsSaved);

    var startTaxCalculationDto =
        new StartTaxCalculationDto(UUID.randomUUID(), LocalDate.now(), LocalDate.now());

    var taxCalculations = taxCalculationService.calculate(startTaxCalculationDto);

    Mockito.verify(taxCalculationRepository, Mockito.times(1))
        .saveAll(
            List.of(
                new TaxCalculation(
                    startTaxCalculationDto.userId(),
                    new BigDecimal("740"),
                    invoiceInRange1.period(),
                    any())));
    Assertions.assertEquals(taxCalculations.size(), 1);
  }
}
