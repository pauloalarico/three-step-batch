package com.example.demo.infra.batch.config.job;

import com.example.demo.domain.model.FinalReport;
import com.example.demo.domain.model.Payment;
import com.example.demo.infra.batch.config.processor.FinalReportProcessor;
import com.example.demo.infra.batch.config.reader.finalreport.FinalReportReader;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FinalReportConfig {

    @Bean
    public Step finalReportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                FinalReportReader finalReportReader, FinalReportProcessor processor, ItemWriter<FinalReport> writer) {

        return new StepBuilder("final-report", jobRepository)
                .<Payment, FinalReport>chunk(100)
                .transactionManager(transactionManager)
                .reader(finalReportReader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
