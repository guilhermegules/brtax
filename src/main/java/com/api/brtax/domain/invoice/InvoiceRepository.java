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
  @Query("SELECT * FROM invoice WHERE invoice.period BETWEEN :start_period and :final_period")
  List<Invoice> getAllInvoicesInPeriod(
      @Param("start_period") LocalDate startPeriod,
      @Param("final_period") LocalDate finalPeriod);
}
