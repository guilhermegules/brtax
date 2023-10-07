package com.api.brtax.domain.taxcalculation;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxCalculationRepository extends CrudRepository<TaxCalculation, UUID> {}
