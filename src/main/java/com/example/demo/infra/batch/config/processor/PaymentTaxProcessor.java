package com.example.demo.infra.batch.config.processor;

import com.example.demo.domain.model.Payment;
import com.example.demo.domain.model.TaxedPayment;
import com.example.demo.domain.servjce.TaxPolicy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class PaymentTaxProcessor implements ItemProcessor<Payment, TaxedPayment> {

    private static final Logger log = LoggerFactory.getLogger(PaymentTaxProcessor.class);

    private final TaxPolicy taxPolicy;

    private final RedisTemplate<String, Object> redisTemplate;

    private final static String PREFIX_REDIS_KEY = "paymentWithTax:";

    @Override
    public TaxedPayment process(Payment item) {

        var taxProcessed = taxPolicy.calculateTax(item.getValue(), item.getDueDate());

        var taxedPayment = new TaxedPayment(item.getId(), item.getValue(), taxProcessed);

        try {
            persistAtRedis(taxedPayment);
        } catch (Exception e) {
            log.error("Unable connect to redis, for id: {}, cause of: {}", item.getId(), e.getMessage());
        }

        return taxedPayment;
    }

    private void persistAtRedis(TaxedPayment taxedPayment) {

        String key = PREFIX_REDIS_KEY + taxedPayment.paymentId();
        Duration hours = Duration.ofHours(2);

        redisTemplate.opsForValue().set(key, taxedPayment, hours);
    }

}
