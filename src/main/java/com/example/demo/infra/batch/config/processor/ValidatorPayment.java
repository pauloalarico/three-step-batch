package com.example.demo.infra.batch.config.processor;

import com.example.demo.domain.enums.PaymentStatus;
import com.example.demo.domain.model.Payment;
import com.example.demo.domain.model.ProcessedPayment;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorPayment implements ItemProcessor<Payment, ProcessedPayment> {


    @Override
    public @Nullable ProcessedPayment process(Payment item) throws Exception {
        if(item.getStatus().equals(PaymentStatus.VENCIDA)) {
            return new ProcessedPayment(item, Boolean.FALSE);
        }

        return new ProcessedPayment(item, Boolean.TRUE);
    }
}
