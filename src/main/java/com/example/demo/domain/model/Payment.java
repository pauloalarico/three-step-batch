package com.example.demo.domain.model;

import com.example.demo.domain.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Payment {

    @Id
    private Long id;

    private Long clientId;

    private String clientName;

    private BigDecimal value;

    private LocalDate dueDate;

    private PaymentStatus status;


    public static Payment create(Long id, Long clientId, String clientName, BigDecimal value, LocalDate dueDate, PaymentStatus status) {
        Payment payment = new Payment();
        payment.id = id;
        payment.clientId = clientId;
        payment.clientName = clientName;
        payment.value = value;
        payment.dueDate = dueDate;
        payment.status = status;
        return payment;
    }
}
