package com.example.demo.infra.batch.config.reader.finalreport;

import com.example.demo.domain.model.Payment;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemStream;
import org.springframework.batch.infrastructure.item.ItemStreamException;
import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;

public class FinalReportReader implements ItemReader<Payment>, ItemStream {

    private final JdbcCursorItemReader<Payment> paymentsReader;

    private final JdbcCursorItemReader<Payment> deadPaymentsReader;

    private boolean nextDeadPayment = false;

    public FinalReportReader(JdbcCursorItemReader<Payment> paymentsReader, JdbcCursorItemReader<Payment> deadPaymentsReader) {
        this.paymentsReader = paymentsReader;
        this.deadPaymentsReader = deadPaymentsReader;

    }

    @Override
    public @Nullable Payment read() throws Exception {

        if (!nextDeadPayment) {
            Payment payment = deadPaymentsReader.read();

            if (payment != null) {
                return payment;
            }

            nextDeadPayment = true;
        }

        return paymentsReader.read();

    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        paymentsReader.open(executionContext);
        deadPaymentsReader.open(executionContext);

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        paymentsReader.update(executionContext);
        deadPaymentsReader.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        paymentsReader.close();
        deadPaymentsReader.close();
    }
}
