package com.api.brtax.domain.invoice;

import com.api.brtax.domain.invoice.dto.InvoiceDetails;
import com.api.brtax.domain.invoice.dto.InvoiceDto;
import com.api.brtax.domain.invoice.dto.SaveInvoiceDto;
import com.api.brtax.exception.BusinessException;
import com.api.brtax.exception.NotFoundException;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class InvoiceService {
  private InvoiceRepository invoiceRepository;

  public InvoiceDto save(SaveInvoiceDto saveInvoice) {
    if (Objects.isNull(saveInvoice.invoiceNumber())
        || Objects.isNull(saveInvoice.period())
        || saveInvoice.value() == null) {
      throw new BusinessException("The passed invoice is invalid!");
    }

    var invoice =
        new Invoice(saveInvoice.invoiceNumber(), saveInvoice.period(), saveInvoice.value());

    var savedInvoice = invoiceRepository.save(invoice);

    return new InvoiceDto(
        savedInvoice.getId(),
        saveInvoice.invoiceNumber(),
        saveInvoice.period(),
        savedInvoice.getValue());
  }

  public InvoiceDetails getInvoiceById(UUID id) {
    return invoiceRepository
        .findById(id)
        .map(i -> new InvoiceDetails(i.getInvoiceNumber(), i.getPeriod(), i.getValue()))
        .orElseThrow(() -> new NotFoundException("Invoice not found with the given ID: " + id));
  }
}
