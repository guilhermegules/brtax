package com.api.brtax.domain.invoice;

import com.api.brtax.domain.invoice.dto.InvoiceDetails;
import com.api.brtax.domain.invoice.dto.InvoiceDto;
import com.api.brtax.domain.invoice.dto.SaveInvoiceDto;
import com.api.brtax.domain.invoice.dto.UpdateInvoiceDto;
import com.api.brtax.exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InvoiceServiceTest {
  @InjectMocks InvoiceService invoiceService;

  @Mock InvoiceRepository invoiceRepository;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    invoiceService = new InvoiceService(invoiceRepository);
  }

  @Test
  public void shouldGetInvoiceById() {
    var randomId = UUID.randomUUID();
    var date = LocalDate.now();
    var invoice = new Invoice("123", date, new BigDecimal("100"));
    Mockito.when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));
    invoiceService.getInvoiceById(randomId);
    Mockito.verify(invoiceRepository, Mockito.times(1)).findById(randomId);
  }

  @Test
  public void shouldSaveInvoice() {
    var today = LocalDate.now();
    var saveInvoiceDto = new SaveInvoiceDto("123", today, new BigDecimal("100"));
    var invoice = new Invoice(saveInvoiceDto.invoiceNumber(), today, new BigDecimal("100"));
    var id = UUID.randomUUID();
    invoice.setId(id);
    Mockito.when(invoiceRepository.save(any())).thenReturn(invoice);
    var savedInvoice = invoiceService.save(saveInvoiceDto);

    Assertions.assertEquals(
        savedInvoice,
        new InvoiceDto(
            savedInvoice.id(),
            savedInvoice.invoiceNumber(),
            savedInvoice.period(),
            savedInvoice.value()));
  }

  @Test
  public void shouldThrowBusinessErrorWhenInvoiceNumberIsNull() {
    Throwable error =
        Assertions.assertThrows(
            BusinessException.class,
            () -> {
              var today = LocalDate.now();
              var saveInvoiceDto = new SaveInvoiceDto(null, today, new BigDecimal("100"));
              invoiceService.save(saveInvoiceDto);
            });

    Assertions.assertEquals(error.getMessage(), "The passed invoice is invalid!");
  }

  @Test
  public void shouldThrowBusinessErrorWhenPeriodIsNull() {
    Throwable error =
        Assertions.assertThrows(
            BusinessException.class,
            () -> {
              var saveInvoiceDto = new SaveInvoiceDto("123", null, new BigDecimal("100"));
              invoiceService.save(saveInvoiceDto);
            });

    Assertions.assertEquals(error.getMessage(), "The passed invoice is invalid!");
  }

  @Test
  public void shouldThrowBusinessErrorWhenValueIsNull() {
    Throwable error =
        Assertions.assertThrows(
            BusinessException.class,
            () -> {
              var saveInvoiceDto = new SaveInvoiceDto("123", LocalDate.now(), null);
              invoiceService.save(saveInvoiceDto);
            });

    Assertions.assertEquals(error.getMessage(), "The passed invoice is invalid!");
  }

  @Test
  public void shouldUpdateInvoiceWithAllBodyValues() {
    var id = UUID.randomUUID();
    var invoice = new Invoice("123", LocalDate.now(), new BigDecimal("100"));
    invoice.setId(id);
    var newDate = LocalDate.now();
    var updateInvoice = new UpdateInvoiceDto("222", newDate, new BigDecimal("200"));
    Mockito.when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoice));

    var updatedInvoice = invoiceService.update(id, updateInvoice);

    Assertions.assertEquals(
        updatedInvoice,
        new InvoiceDetails(
            updateInvoice.invoiceNumber(), updateInvoice.period(), updateInvoice.value()));
  }

  @Test
  public void shouldUpdateInvoiceWithInvoiceNumberNull() {
    var id = UUID.randomUUID();
    var invoice = new Invoice("123", LocalDate.now(), new BigDecimal("100"));
    invoice.setId(id);
    var newDate = LocalDate.now();
    var updateInvoice = new UpdateInvoiceDto(null, newDate, new BigDecimal("200"));
    Mockito.when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoice));

    var updatedInvoice = invoiceService.update(id, updateInvoice);

    Assertions.assertEquals(
        updatedInvoice,
        new InvoiceDetails(
            invoice.getInvoiceNumber(), updateInvoice.period(), updateInvoice.value()));
  }

  @Test
  public void shouldUpdateInvoiceWithPeriodAndValueNull() {
    var id = UUID.randomUUID();
    var invoice = new Invoice("123", LocalDate.now(), new BigDecimal("100"));
    invoice.setId(id);
    var updateInvoice = new UpdateInvoiceDto("222", null, null);
    Mockito.when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoice));

    var updatedInvoice = invoiceService.update(id, updateInvoice);

    Assertions.assertEquals(
        updatedInvoice,
        new InvoiceDetails(
            updateInvoice.invoiceNumber(), invoice.getPeriod(), invoice.getValue()));
  }

  @Test
  public void shouldThrowBusinessExceptionWhenAllDtoFieldsAreNull() {
    Throwable error = Assertions.assertThrows(BusinessException.class, () -> {
      invoiceService.update(null, new UpdateInvoiceDto(null, null, null));
    });

    Assertions.assertEquals(error.getMessage(), "Body need to have at least one field with value!");
  }

  @Test
  public void shouldDeleteInvoice() {
    var id = UUID.randomUUID();

    invoiceService.delete(id);

    Mockito.verify(invoiceRepository, Mockito.times(1)).deleteById(id);
  }
}
