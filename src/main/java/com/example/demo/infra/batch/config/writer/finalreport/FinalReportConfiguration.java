package com.example.demo.infra.batch.config.writer.finalreport;

import com.example.demo.domain.model.FinalReport;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class FinalReportConfiguration {

    @Value("${app.output}")
    private String outputResource;

    @Bean
    public FlatFileItemWriter<FinalReport> writer() {
        return new FlatFileItemWriterBuilder<FinalReport>()
                .name("final-report-file")
                .resource(new FileSystemResource(outputResource + "final_report.csv"))
                .delimited()
                .delimiter(",")
                .names(new String[] {"clientId", "clientName", "originalValue", "totalWithTax", "status"})
                .headerCallback(w -> w.write("client_id, nm_client, original_payment_value, total_payment_value, payment_status"))
                .build();

    }
}
