package com.example.demo.infra.batch.config.threads;

import org.jspecify.annotations.NonNull;
import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.Collections;
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
    public Partitioner partitioner(DataSource dataSource) {
        return gridSize -> {

            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            Integer min = jdbcTemplate.queryForObject("SELECT MIN(id) FROM dead_payments", Integer.class);
            Integer max = jdbcTemplate.queryForObject("SELECT MAX(id) FROM dead_payments", Integer.class);

            if (min == null || max == null) {
                return Collections.emptyMap();
            }

            return getStringExecutionContextMap(gridSize, max, min);
        };
    }

    private static @NonNull Map<String, ExecutionContext> getStringExecutionContextMap(int gridSize, Integer max, Integer min) {
        int range = (max - min) / gridSize + 1;


        Map<String, ExecutionContext> partitions = new HashMap<>();

        for (int i = 0; i < gridSize; i++) {
            ExecutionContext context = new ExecutionContext();

            int start = min + i * range;
            int end = Math.min(start + range - 1, max);

            context.putInt("minId", start);
            context.putInt("maxId", end);
            partitions.put("partition-" + i, context);

        }
        return partitions;
    }

}
