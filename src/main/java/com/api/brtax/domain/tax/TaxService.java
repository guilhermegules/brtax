package com.api.brtax.domain.tax;

import com.api.brtax.domain.tax.dto.TaxDetails;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxService {
  private final TaxRepository taxRepository;

  public List<TaxDetails> getAll() {
    return StreamSupport.stream(taxRepository.findAll().spliterator(), false)
        .map(TaxDetails::new)
        .collect(Collectors.toList());
  }
}
