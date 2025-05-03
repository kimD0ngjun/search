package com.example.search_sol.infrastructure.batch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import com.example.search_sol.application.dto.ElasticsearchDTO;
import com.example.search_sol.application.dto.MigrationDTO;
import com.example.search_sol.application.dto.MySqlDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MigrationSteps {

    private static final int PAGE_SIZE = 1_000;

    private final DataSource dataSource;
    private final ElasticsearchClient elasticsearchClient;

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

    @Bean
    public ItemProcessor<MySqlDTO, MigrationDTO> koreanItemProcessor() {
        return mysql -> new MigrationDTO(
                mysql.id(),
                mysql.entry(),
                mysql.type(),
                mysql.pos(),
                mysql.definition() == null || mysql.definition().isBlank()
                        ? "mysql에서 설명이 존재하지 않음" : mysql.definition());
    }

    @Bean
    public ItemWriter<MigrationDTO> koreanItemWriter() {
        return items -> {
            BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();

            for (MigrationDTO item : items) {
                ElasticsearchDTO dto = ElasticsearchDTO.of(item);
                bulkBuilder.operations(op -> op
                        .index(idx -> idx
                                .index("koreans")
                                .id(String.valueOf(item.id()))
                                .document(dto)
                        )
                );
            }

            var response = elasticsearchClient.bulk(bulkBuilder.build());

            if (response.errors()) {
                response.items().forEach(item -> {
                    if (item.error() != null) {
                        log.error("인덱싱 에러: {}", item.error().reason());
                    }
                });

                throw new IllegalStateException("도큐먼트가 인덱싱에 실패함");
            }
        };
    }
}
