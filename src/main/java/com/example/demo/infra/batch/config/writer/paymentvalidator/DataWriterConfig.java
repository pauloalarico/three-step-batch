package com.example.demo.infra.batch.config.writer.paymentvalidator;

import com.example.demo.domain.model.Payment;
import com.example.demo.domain.model.ProcessedPayment;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataWriterConfig {

    @Bean
    public PaymentCheckerWriter compositeItemWriter(JdbcBatchItemWriter<ProcessedPayment> writerSuccess,
                                                                               JdbcBatchItemWriter<ProcessedPayment> writerError) {
        return new PaymentCheckerWriter(writerSuccess, writerError);
    }

    @Bean
    public JdbcBatchItemWriter<ProcessedPayment> writerSuccess(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<ProcessedPayment>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO payments (id, client_id, client_name, value, total_value, due_date, status)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """)
                .itemPreparedStatementSetter((processedPayment, ps) -> {
                    Payment p = processedPayment.payment();
                    ps.setObject(1, p.getId());
                    ps.setObject(2, p.getClientId());
                    ps.setObject(3, p.getClientName());
                    ps.setObject(4, p.getValue());
                    ps.setObject(5, p.getValue());
                    ps.setObject(6, p.getDueDate());
                    ps.setObject(7, p.getStatus().toString());
        })
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<ProcessedPayment> writerError(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<ProcessedPayment>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO dead_payments (id, client_id, client_name, value, total_value, due_date, status)
                         VALUES (?, ?, ?, ?, ?, ?, ?)
                        """)
                .beanMapped()
                .itemPreparedStatementSetter((processedPayment, ps) -> {
                    Payment p = processedPayment.payment();
                    ps.setObject(1, p.getId());
                    ps.setObject(2, p.getClientId());
                    ps.setObject(3, p.getClientName());
                    ps.setObject(4, p.getValue());
                    ps.setObject(5, p.getValueWithTax());
                    ps.setObject(6, p.getDueDate());
                    ps.setObject(7, p.getStatus().toString());
                })
                .build();

    }

}
