package com.example.demo.domain.model;

import com.example.demo.domain.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Payment {

    @Id
    private Long id;

    private Long clientId;

    private String clientName;

    private BigDecimal value;

    private LocalDate dueDate;

    private PaymentStatus status;

}
