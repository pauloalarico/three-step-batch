package com.example.demo.infra.batch.config.reader.paymentvalidator;

import com.example.demo.domain.enums.PaymentStatus;
import com.example.demo.domain.model.Payment;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.time.LocalDate;

public class PaymentSetFieldMapper implements FieldSetMapper<Payment> {

    @Override
    public Payment mapFieldSet(FieldSet fieldSet) throws BindException {
        return Payment.builder()
                .id(fieldSet.readLong("id"))
                .clientId(fieldSet.readLong("clientId"))
                .clientName(fieldSet.readString("clientName"))
                .value(fieldSet.readBigDecimal("value"))
                .dueDate(toLocalDate(fieldSet.readString("dueDate")))
                .status(PaymentStatus.valueOf(fieldSet.readString("status")))
                .build();
    }

    private LocalDate toLocalDate(String localDate) {
        return LocalDate.parse(localDate);
    }
}
