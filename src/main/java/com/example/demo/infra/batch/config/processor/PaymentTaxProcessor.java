package com.example.demo.infra.batch.config.processor;

import com.example.demo.domain.model.Payment;
import com.example.demo.domain.model.TaxedPayment;
import com.example.demo.domain.servjce.TaxPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentTaxProcessor implements ItemProcessor<Payment, TaxedPayment> {

    private final TaxPolicy taxPolicy;

    @Override
    public TaxedPayment process(Payment item) throws Exception {

        var taxProcessed = taxPolicy.calculateTax(item.getValue(), item.getDueDate());

        return new TaxedPayment(item.getId(), item.getValue(), taxProcessed);
    }

}
