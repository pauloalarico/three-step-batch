package com.example.demo.infra.batch.config.writer.overduepayment;

import com.example.demo.domain.model.Payment;
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
    public JdbcBatchItemWriter<Payment> writerOverduePayment(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Payment>()
                .dataSource(dataSource)
                .sql("""
                        INSERT INTO dead_payments (id, total_value)
                        VALUES (?, ?)
                        ON CONFLICT (id)
                        DO UPDATE SET total_value = EXCLUDED.total_value;
                        """)
                .itemPreparedStatementSetter(((payment, ps) -> {
                    ps.setObject(1, payment.getId());
                    ps.setObject(2, payment.getValueWithTax());
                    log.info("ID: {}, VALUE: {}", payment.getId(), payment.getValueWithTax());;

                }))
                .build();
    }

}