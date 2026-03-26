package com.example.demo.domain.model;

import com.example.demo.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    private Long id;

    private Long clientId;

    private String clientName;

    private BigDecimal value;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

}
