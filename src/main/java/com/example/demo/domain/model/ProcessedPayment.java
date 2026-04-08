package com.example.demo.domain.model;

import com.example.demo.domain.enums.PaymentValidator;

public record ProcessedPayment (
        Payment payment,
        PaymentValidator status
) {
}
