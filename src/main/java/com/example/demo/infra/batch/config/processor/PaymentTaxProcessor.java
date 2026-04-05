package com.example.demo.infra.batch.config.processor;

import com.example.demo.domain.model.Payment;
import com.example.demo.domain.model.TaxedPayment;
import com.example.demo.domain.servjce.TaxPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentTaxProcessor implements ItemProcessor<Payment, TaxedPayment> {

    private final TaxPolicy taxPolicy;

    private final RedisTemplate<String, Object> redisTemplate;

    private final static String PREFIX_REDIS_KEY = "paymentWithTax:";

    @Override
    public TaxedPayment process(Payment item) throws Exception {

        var taxProcessed = taxPolicy.calculateTax(item.getValue(), item.getDueDate());

        var taxedPayment = new TaxedPayment(item.getId(), item.getValue(), taxProcessed);
        persistAtRedis(taxedPayment);

        return taxedPayment;
    }

    private void persistAtRedis(TaxedPayment taxedPayment) {
        String key = PREFIX_REDIS_KEY + taxedPayment.paymentId();
        redisTemplate.opsForList().rightPush(key, taxedPayment);
    }

}
