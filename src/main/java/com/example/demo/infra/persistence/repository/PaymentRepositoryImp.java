package com.example.demo.infra.persistence.repository;

import com.example.demo.domain.model.Payment;
import com.example.demo.domain.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImp implements PaymentRepository {

    private final PaymentRepositoryIn repository;


    @Override
    public Payment save(Payment payment) {
        return repository.save(payment);
    }

}
