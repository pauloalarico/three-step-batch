package com.example.demo.infra.batch.config.reader.overduepayment;

import com.example.demo.domain.model.Payment;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.batch.infrastructure.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class PastDuePaymentReader {

    @Bean
    @StepScope
    public JdbcCursorItemReader<Payment> overduePaymentReader(DataSource dataSource,
                                                              @Value("#{stepExecutionContext['minId']}") int minId,
                                                              @Value("#{stepExecutionContext['maxId']}") int maxId) {
        return new JdbcCursorItemReaderBuilder<Payment>()
                .dataSource(dataSource)
                .name("overduePaymentReader")
                .sql("SELECT * FROM dead_payments WHERE id >= ? AND id <= ?")
                .rowMapper(new OverduePaymentRowMapper())
                .queryArguments(minId, maxId)
                .build();
    }

}