package com.example.demo.infra.batch.config;

import com.example.demo.domain.model.Payment;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataWriterConfig {

    @Bean
    public ItemWriter<Payment> writer(EntityManagerFactory factory) {
        return new JpaItemWriter<>(factory);
    }
}
