package com.example.demo.infra.batch.config.processor;

import com.example.demo.application.service.TaxCalculator;
import com.example.demo.domain.model.Payment;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PaymentTaxProcessor implements ItemProcessor<Payment, Payment> {

    @Override
    public Payment process(Payment item) throws Exception {
        var taxProcessed = TaxCalculator.calculateTax(item.getValue(), item.getDueDate());
        item.applyValue(taxProcessed);

        return item;

    }

}
