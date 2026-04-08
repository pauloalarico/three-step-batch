package com.example.demo.infra.batch.config.job;

import com.example.demo.domain.model.Payment;
import com.example.demo.domain.model.ProcessedPayment;
import com.example.demo.domain.model.TaxedPayment;
import com.example.demo.infra.batch.config.processor.PaymentTaxProcessor;
import com.example.demo.infra.batch.config.processor.PaymentValidator;
import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@EnableJdbcJobRepository
public class PaymentValidatorConfig {

    @Bean
    public Job job(JobRepository jobRepository, Step initialStep, Step createTableIfNecessary, Step partitionStep,
                   Step finalReportStep) {
        return new JobBuilder("verify-payment", jobRepository)
                .start(createTableIfNecessary)
                .next(initialStep)
                .next(partitionStep)
                .next(finalReportStep)
                .build();
    }

    @Bean
    public Step initialStep(JobRepository jobRepository,
                            ItemReader<Payment> reader,
                            PaymentValidator processor,
                            ItemWriter<ProcessedPayment> compositeItemWriter,
                            PlatformTransactionManager transactionManager) {

        return new StepBuilder("first-step", jobRepository)
                .<Payment, ProcessedPayment>chunk(100)
                .transactionManager(transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(compositeItemWriter)
                .build();
    }

    @Bean
    public Step createTableIfNecessary(JobRepository jobRepository,
                                       PlatformTransactionManager transactionManager,
                                       DataSource dataSource) {

        return new StepBuilder("create-table", jobRepository)
                .allowStartIfComplete(true)
                .tasklet(((_, _) -> {

                    Resource resource = new ClassPathResource("initialize.sql");
                    ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator(resource);
                    resourceDatabasePopulator.setContinueOnError(true);
                    resourceDatabasePopulator.execute(dataSource);
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

    @Bean
    public Step applyTaxToOverduePayment(JobRepository jobRepository,
                                         PlatformTransactionManager transactionManager,
                                         ItemReader<Payment> overduePaymentReader,
                                         PaymentTaxProcessor processor,
                                         ItemWriter<TaxedPayment> writerOverduePayment) {

        return new StepBuilder("tax-processor", jobRepository)
                .allowStartIfComplete(true)
                .<Payment, TaxedPayment>chunk(100)
                .transactionManager(transactionManager)
                .reader(overduePaymentReader)
                .processor(processor)
                .writer(writerOverduePayment)
                .build();
    }

}
