package com.api.brtax.controller;

import com.api.brtax.domain.invoice.InvoiceService;
import com.api.brtax.domain.invoice.dto.InvoiceDetails;
import com.api.brtax.domain.invoice.dto.InvoiceDto;
import com.api.brtax.domain.invoice.dto.SaveInvoiceDto;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("invoice")
@AllArgsConstructor
public class InvoiceController {
  private InvoiceService invoiceService;

  @PostMapping
  public ResponseEntity<InvoiceDto> save(@RequestBody SaveInvoiceDto saveInvoice, UriComponentsBuilder uriBuilder) {
    var savedInvoice = invoiceService.save(saveInvoice);
    var uri = uriBuilder.path("/invoice/{invoiceId}").buildAndExpand(savedInvoice.id()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @GetMapping("/{invoiceId}")
  public ResponseEntity<InvoiceDetails> getInvoiceById(@PathVariable UUID invoiceId) {
    var invoice = invoiceService.getInvoiceById(invoiceId);
    return ResponseEntity.ok(invoice);
  }
}
