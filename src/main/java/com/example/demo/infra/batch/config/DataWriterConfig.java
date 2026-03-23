package com.example.demo.infra.batch.config;

import com.example.demo.domain.model.Payment;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataWriterConfig {

    @Bean
    public ItemWriter<Payment> writerSuccess(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Payment>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO payments (id, client_id, client_name, value, due_date, status) 
                        VALUES (:id, :clientId, :clientName, :value:, :dueDate, :status)
                        """)
                .beanMapped()
                .build();
    }

    @Bean
    public ItemWriter<Payment> writerError(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Payment>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO dead_payments_table (id, client_id, client_name, value, due_date, status) 
                        VALUES (:id, :clientId, :clientName, :value:, :dueDate, :status)
                        """)
                .beanMapped()
                .build();
    }

}
