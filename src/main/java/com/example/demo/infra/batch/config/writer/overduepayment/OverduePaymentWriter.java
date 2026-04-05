package com.example.demo.infra.batch.config.writer.overduepayment;

import com.example.demo.domain.model.Payment;
import com.example.demo.domain.model.TaxedPayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Slf4j
@Component
public class OverduePaymentWriter {

    @Bean
    public JdbcBatchItemWriter<TaxedPayment> writerOverduePayment(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<TaxedPayment>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO dead_payments (id, total_value)
                        VALUES (?, ?)
                        ON CONFLICT (id)
                        DO UPDATE SET total_value = EXCLUDED.total_value;
                        """)
                .itemPreparedStatementSetter(((taxedPayment, ps) -> {
                    ps.setObject(1, taxedPayment.paymentId());
                    ps.setObject(2, taxedPayment.taxedValue());
                    log.info("ID: {}, VALUE: {}", taxedPayment.paymentId(), taxedPayment.taxedValue());;

                }))
                .build();
    }

}