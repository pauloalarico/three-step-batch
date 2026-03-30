package com.example.demo.infra.batch.config.reader.finalreport;

import com.example.demo.domain.model.Payment;
import com.example.demo.infra.batch.config.reader.utils.PaymentRowMapper;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FinalReportReaderConfig {

    @Bean
    public FinalReportReader finalReportReader(JdbcCursorItemReader<Payment> paymentsReader,
                                               JdbcCursorItemReader<Payment> deadPaymentsReader) {
        return new FinalReportReader(paymentsReader, deadPaymentsReader);

    }

    @Bean
    public JdbcCursorItemReader<Payment> paymentsReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Payment>()
                .dataSource(dataSource)
                .name("valid-payment-reader")
                .sql("SELECT * FROM payments")
                .rowMapper(new PaymentRowMapper())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<Payment> deadPaymentsReader(DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<Payment>()
                .dataSource(dataSource)
                .name("dead-payments-reader")
                .sql("SELECT * FROM dead_payments")
                .rowMapper(new PaymentRowMapper())
                .build();
    }

}
