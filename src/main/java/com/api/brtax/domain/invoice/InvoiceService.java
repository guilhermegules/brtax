package com.api.brtax.domain.invoice;

import com.api.brtax.domain.invoice.dto.InvoiceDetails;
import com.api.brtax.domain.invoice.dto.InvoiceDto;
import com.api.brtax.domain.invoice.dto.SaveInvoiceDto;
import com.api.brtax.domain.invoice.dto.UpdateInvoiceDto;
import com.api.brtax.exception.BusinessException;
import com.api.brtax.exception.NotFoundException;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class InvoiceService {
  private InvoiceRepository invoiceRepository;

  @Transactional
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

  @Transactional
  public InvoiceDetails update(UUID id, UpdateInvoiceDto updateInvoice) {
    if (!isValidPayload(id, updateInvoice)) {
      throw new BusinessException("Body need to have at least one field with value!");
    }

    var invoice =
        invoiceRepository
            .findById(id)
            .map((i) -> invoiceUpdateMapper(i, updateInvoice))
            .orElseThrow(() -> new NotFoundException("Invoice not found with the given ID: " + id));

    invoiceRepository.save(invoice);

    return new InvoiceDetails(invoice.getInvoiceNumber(), invoice.getPeriod(), invoice.getValue());
  }

  private boolean isValidPayload(UUID invoiceId, UpdateInvoiceDto updateInvoice) {
    return (updateInvoice.invoiceNumber() != null
            || updateInvoice.period() != null
            || updateInvoice.value() != null)
        && invoiceId != null;
  }

  private Invoice invoiceUpdateMapper(Invoice invoice, UpdateInvoiceDto updateInvoiceDto) {
    return new Invoice(
        updateInvoiceDto.invoiceNumber() == null
            ? invoice.getInvoiceNumber()
            : updateInvoiceDto.invoiceNumber(),
        updateInvoiceDto.period() == null ? invoice.getPeriod() : updateInvoiceDto.period(),
        updateInvoiceDto.value() == null ? invoice.getValue() : updateInvoiceDto.value());
  }
}
