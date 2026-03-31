package com.example.demo.domain.model;

import com.example.demo.domain.enums.PaymentStatus;

import java.math.BigDecimal;

public record FinalReport(
        Long clientId,
        String clientName,
        BigDecimal originalValue,
        BigDecimal totalWithTax,
        PaymentStatus status
) {
}
