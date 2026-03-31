package com.example.demo.infra.batch.config.processor;

import com.example.demo.domain.model.FinalReport;
import com.example.demo.domain.model.Payment;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FinalReportProcessor implements ItemProcessor<Payment, FinalReport> {

    @Override
    public @Nullable FinalReport process(Payment item) throws Exception {

        return new FinalReport(
                item.getClientId(),
                item.getClientName(),
                item.getValue(),
                item.getValueWithTax(),
                item.getStatus()
        );
    }

}
