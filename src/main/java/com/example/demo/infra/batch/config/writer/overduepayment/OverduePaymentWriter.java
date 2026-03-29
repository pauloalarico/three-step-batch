package com.example.demo.infra.batch.config.writer.overduepayment;

import com.example.demo.domain.model.Payment;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class OverduePaymentWriter {

    @Bean
    public JdbcBatchItemWriter<Payment> writerOverduePayment(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Payment>()
                .dataSource(dataSource)
                .sql("""
                        UPDATE dead_payments SET value = ? WHERE id = ?
                        """)
                .itemPreparedStatementSetter(((payment, ps) -> {
                    ps.setObject(1, payment.getValue());
                    ps.setObject(2, payment.getId());
                }))
                .build();
    }

}
