package com.example.search_sol.infrastructure.batch;

import com.example.search_sol.application.dto.MySqlDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class MigrationJobConfig {

    private static final int PAGE_SIZE = 1_000;

    private final DataSource dataSource;

    @Bean
    public JdbcPagingItemReader<MySqlDTO> koreanItemReader(PagingQueryProvider koreanQueryProvider) {
        return new JdbcPagingItemReaderBuilder<MySqlDTO>()
                .name("koreanItemReader")
                .dataSource(dataSource)
                .queryProvider(koreanQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(MySqlDTO.class))
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean koreanQueryProvider() {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(dataSource);
        provider.setSelectClause("SELECT id, entry, type, pos, definition");
        provider.setFromClause("FROM koreans");
        // where절 생략 가능: 전체 가져올 경우
        provider.setSortKey("id"); // 페이징에 필수

        return provider;
    }

}
