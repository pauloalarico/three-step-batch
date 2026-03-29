package com.example.demo.infra.batch.config.writer.paymentvalidator;

import com.example.demo.domain.model.ProcessedPayment;
import org.springframework.batch.infrastructure.item.*;

import java.util.List;

public class PaymentCheckerWriter implements ItemWriter<ProcessedPayment> {

    private final ItemWriter<ProcessedPayment> writerSuccess;

    private final ItemWriter<ProcessedPayment> writerError;

    public PaymentCheckerWriter(ItemWriter<ProcessedPayment> writerSuccess, ItemWriter<ProcessedPayment> writerError) {
        this.writerSuccess = writerSuccess;
        this.writerError = writerError;
    }

    @Override
    public void write(Chunk<? extends ProcessedPayment> chunk) throws Exception {
        List<? extends ProcessedPayment> success = chunk.getItems().stream()
                .filter(ProcessedPayment::processed)
                .toList();

        List<? extends ProcessedPayment> errors = chunk.getItems().stream()
                .filter(p-> !p.processed())
                .toList();

        if(!success.isEmpty()) {
            writerSuccess.write(new Chunk<>(success));
        }

        if(!errors.isEmpty()) {
            writerError.write(new Chunk<>(errors));
        }
    }

}
