package com.example.demo.domain.model;

import com.example.demo.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private Long id;

    private Long clientId;

    private String clientName;

    private BigDecimal value;

    private LocalDate dueDate;

    private PaymentStatus status;

}
