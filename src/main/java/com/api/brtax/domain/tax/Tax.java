package com.api.brtax.domain.tax;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "tax")
@Getter
public class Tax {
    @Id
    private UUID id;
    private String name;
    private BigDecimal aliquot;
}
