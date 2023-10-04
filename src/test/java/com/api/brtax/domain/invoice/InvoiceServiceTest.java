package com.api.brtax.domain.invoice;

import com.api.brtax.domain.invoice.dto.InvoiceDto;
import com.api.brtax.domain.invoice.dto.SaveInvoiceDto;
import com.api.brtax.exception.BusinessException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    var date = LocalDateTime.now();
    var invoice = new Invoice("123", date, new BigDecimal("100"));
    Mockito.when(invoiceRepository.findById(any())).thenReturn(Optional.of(invoice));
    invoiceService.getInvoiceById(randomId);
    Mockito.verify(invoiceRepository, Mockito.times(1)).findById(randomId);
  }

  @Test
  public void shouldSaveInvoice() {
    var today = LocalDateTime.now();
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
    Throwable error = Assertions.assertThrows(BusinessException.class, () -> {
      var today = LocalDateTime.now();
      var saveInvoiceDto = new SaveInvoiceDto(null, today, new BigDecimal("100"));
      invoiceService.save(saveInvoiceDto);
    });

    Assertions.assertEquals(error.getMessage(), "The passed invoice is invalid!");
  }

  @Test
  public void shouldThrowBusinessErrorWhenPeriodIsNull() {
    Throwable error = Assertions.assertThrows(BusinessException.class, () -> {
      var saveInvoiceDto = new SaveInvoiceDto("123", null, new BigDecimal("100"));
      invoiceService.save(saveInvoiceDto);
    });

    Assertions.assertEquals(error.getMessage(), "The passed invoice is invalid!");
  }

  @Test
  public void shouldThrowBusinessErrorWhenValueIsNull() {
    Throwable error = Assertions.assertThrows(BusinessException.class, () -> {
      var saveInvoiceDto = new SaveInvoiceDto("123", LocalDateTime.now(), null);
      invoiceService.save(saveInvoiceDto);
    });

    Assertions.assertEquals(error.getMessage(), "The passed invoice is invalid!");
  }
}
