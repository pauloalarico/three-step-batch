package com.example.demo.domain.model;

public record ProcessedPayment (
        Payment payment,
        Boolean processed
) {
}
