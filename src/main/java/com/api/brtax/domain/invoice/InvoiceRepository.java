package com.api.brtax.domain.invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, UUID> {
  @Query("SELECT * FROM invoice WHERE invoice.period BETWEEN :startPeriod and :finalPeriod")
  List<Invoice> getAllInvoicesInPeriod(
      @Param("startPeriod") LocalDate startPeriod, @Param("finalPeriod") LocalDate finalPeriod);
}
