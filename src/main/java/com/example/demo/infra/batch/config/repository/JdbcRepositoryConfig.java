package com.example.demo.infra.batch.config.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JdbcJobRepositoryFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Getter
@Setter
public class JdbcRepositoryConfig {

    private String username;

    private String password;

    private String jdbcUrl;


    @Bean
    @Primary
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JdbcJobRepositoryFactoryBean factory = new JdbcJobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setDatabaseType("POSTGRES");
        factory.setTransactionManager(transactionManager);
        return factory.getObject();
    }

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

}
