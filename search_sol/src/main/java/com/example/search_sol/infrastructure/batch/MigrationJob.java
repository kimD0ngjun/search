package com.example.search_sol.infrastructure.batch;

import com.example.search_sol.application.dto.MigrationDTO;
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
public class MigrationJob {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final JdbcPagingItemReader<MySqlDTO> koreanItemReader;
    private final ItemProcessor<MySqlDTO, MigrationDTO> koreanItemProcessor;
    private final ItemWriter<MigrationDTO> koreanItemWriter;

    @Bean
    public Job migrationJobToEs() {
        return new JobBuilder("migrationToEsJob", jobRepository)
                .start(migrationToEs()).build();
    }

    @Bean
    public Step migrationToEs() {
        return new StepBuilder("migrationToEsStep", jobRepository)
                .<MySqlDTO, MigrationDTO>chunk(1_000, transactionManager)
                .reader(koreanItemReader)
                .processor(koreanItemProcessor)
                .writer(koreanItemWriter).build();
    }
}
