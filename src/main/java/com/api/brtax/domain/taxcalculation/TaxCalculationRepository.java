package com.api.brtax.domain.taxcalculation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxCalculationRepository extends CrudRepository<TaxCalculation, UUID> {
  Optional<List<TaxCalculation>> findAllTaxCalculationByTaxCalculationGroupId(
      UUID taxCalculationGroupId);
}
