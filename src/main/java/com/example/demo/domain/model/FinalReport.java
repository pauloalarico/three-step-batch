package com.example.demo.domain.model;

import java.math.BigDecimal;

public record FinalReport(
        Long clientId,
        String clientName,
        BigDecimal originalValue,
        BigDecimal totalWithTax
) {
}
