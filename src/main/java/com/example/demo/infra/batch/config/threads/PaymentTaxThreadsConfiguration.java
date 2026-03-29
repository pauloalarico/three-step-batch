package com.example.demo.infra.batch.config.threads;

import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class PaymentTaxThreadsConfiguration {

    @Bean
    public Step partitionStep(TaskExecutor taskExecutor, JobRepository jobRepository, Partitioner partitioner,
                              Step applyTaxToOverduePayment) {
        return new StepBuilder("thread-partitioner", jobRepository)
                .partitioner("worker-partitioner", partitioner)
                .step(applyTaxToOverduePayment)
                .taskExecutor(taskExecutor)
                .gridSize(4)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("thread-payment-");
        executor.initialize();
        return executor;
    }

    @Bean
    public Partitioner partitioner() {
        return gridSize -> {
            int range = 400/gridSize;
            Map<String, ExecutionContext> partitions = new HashMap<>();

            for (int i = 0; i < gridSize; i++) {
                ExecutionContext context = new ExecutionContext();
                context.putInt("minId", i * range);
                context.putInt("maxId", (i + 1) * range);
                partitions.put("partition-" + i, context);

            }

            return partitions;
        };
    }

}
