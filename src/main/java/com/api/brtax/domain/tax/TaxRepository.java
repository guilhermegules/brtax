package com.api.brtax.domain.tax;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaxRepository extends CrudRepository<Tax, UUID> {
}
