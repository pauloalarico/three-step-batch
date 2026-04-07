package com.example.demo.infra.batch.config.reader.paymentvalidator;

import com.example.demo.domain.model.Payment;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class CsvFileReaderConfig {

    @Value("${app.resource}")
    private String inputResource;

    @Value("${app.fileName}")
    private String fileName;

    @Bean
    public ItemReader<Payment> reader() {
        return new FlatFileItemReaderBuilder<Payment>()
                .name("fileReader")
                .resource(new FileSystemResource(inputResource + fileName))
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("id", "clientId", "clientName", "value", "dueDate", "status")
                .fieldSetMapper(new PaymentSetFieldMapper())
                .build();
    }
}
