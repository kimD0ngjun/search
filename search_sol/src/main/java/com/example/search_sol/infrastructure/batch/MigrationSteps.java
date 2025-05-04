package com.example.search_sol.infrastructure.batch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.example.search_sol.application.dto.ElasticsearchDTO;
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
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MigrationSteps {

    private static final int PAGE_SIZE = 1_000;

    private final DataSource dataDbSource;
    private final ElasticsearchClient elasticsearchClient;

    @Bean(name = "koreanItemReader")
    public JdbcPagingItemReader<MySqlDTO> koreanItemReader(PagingQueryProvider koreanQueryProvider) {
        return new JdbcPagingItemReaderBuilder<MySqlDTO>()
                .name("koreanItemReader")
                .dataSource(dataDbSource)
                .queryProvider(koreanQueryProvider)
                .rowMapper(new BeanPropertyRowMapper<>(MySqlDTO.class))
                .pageSize(PAGE_SIZE)
                .build();
    }

    @Bean
    public SqlPagingQueryProviderFactoryBean koreanQueryProvider() {
        SqlPagingQueryProviderFactoryBean provider = new SqlPagingQueryProviderFactoryBean();
        provider.setDataSource(dataDbSource);
        provider.setSelectClause("SELECT id, entry, type, pos, definition");
        provider.setFromClause("FROM koreans");
        // where절 생략 가능: 전체 가져올 경우
        provider.setSortKey("id"); // 페이징에 필수

        return provider;
    }

    @Bean(name = "koreanItemProcessor")
    public ItemProcessor<MySqlDTO, MySqlDTO> koreanItemProcessor() {
        return mysql -> new MySqlDTO(
                mysql.getId(),
                mysql.getEntry(),
                mysql.getType(),
                mysql.getPos(),
                mysql.getDefinition() == null || mysql.getDefinition().isBlank()
                        ? "mysql에서 설명이 존재하지 않음" : mysql.getDefinition());
    }

    @Bean(name = "koreanItemWriter")
    public ItemWriter<MySqlDTO> koreanItemWriter() {
        return items -> {
            BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();
            List<? extends MySqlDTO> dtoList = items.getItems();
            List<BulkOperation> koreans = dtoList.stream().map(
                    dto -> BulkOperation.of(b -> b.update(
                            i -> i.index("koreans")
                                    .action(a -> a
                                            .doc(ElasticsearchDTO.of(dto))
                                            .docAsUpsert(true))
                                    .id(String.valueOf(dto.getId())))) // upsert
            ).toList();
            bulkBuilder.operations(koreans);
            BulkResponse response = elasticsearchClient.bulk(bulkBuilder.build());

            // 현재는 로깅이지만 별개의 저장소에 id를 기억해두고, 해당 id를 바탕으로 이벤트 동기화에서 보상 트랜잭션 활용
            if (response.errors())
                response.items().stream()
                        .filter(i -> i.error() != null)
                        .forEach(i -> log.error("{}번 도큐먼트 인덱싱 실패: {}", i.id(), i.error().reason()));
        };
    }
}
