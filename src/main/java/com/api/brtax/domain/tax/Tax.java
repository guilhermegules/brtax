package com.api.brtax.domain.tax;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Table(name = "tax")
@Getter
@RequiredArgsConstructor
public class Tax {
    @Id
    private final UUID id;
    private final String name;
    private final BigDecimal aliquot;
}
