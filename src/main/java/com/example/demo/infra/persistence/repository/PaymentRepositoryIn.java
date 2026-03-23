package com.example.demo.infra.persistence.repository;

import com.example.demo.domain.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositoryIn extends JpaRepository<Payment, Long> {
}
