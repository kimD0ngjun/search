package com.example.search_sol.infrastructure.batch;

import com.example.search_sol.application.dto.MySqlDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchIndexJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager jdbcTransactionManager;
    private final JdbcPagingItemReader<MySqlDTO> koreanItemReader;
    private final ItemProcessor<MySqlDTO, MySqlDTO> koreanItemProcessor;
    private final ItemWriter<MySqlDTO> koreanItemWriter;

    @Bean
    public Job migrationJobToEs() {
        return new JobBuilder("migrationToEsJob", jobRepository)
                .start(migrationToEs()).build();
    }

    @Bean
    public Step migrationToEs() {
        return new StepBuilder("migrationToEsStep", jobRepository)
                .<MySqlDTO, MySqlDTO>chunk(1_000, jdbcTransactionManager)
                .reader(koreanItemReader)
                .processor(koreanItemProcessor)
                .writer(koreanItemWriter).build();
    }
}
