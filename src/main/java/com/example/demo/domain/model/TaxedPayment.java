package com.example.demo.domain.model;

import java.math.BigDecimal;

public record TaxedPayment(
        Long paymentId,
        BigDecimal originalValue,
        BigDecimal taxedValue
) {
}
