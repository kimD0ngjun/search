package com.example.search_sol.application.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.search_sol.application.dto.ElasticsearchDTO;
import com.example.search_sol.infrastructure.elasticsearch.ElasticsearchHandler;
import com.example.search_sol.presentation.dto.KeywordSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordSearchService {

    private static final Map<String, Float> matchFieldBoostMap = Map.of(
            "entry", 2.0f,
            "entry.ngram", 1.0f,
            "entry.autocomplete", 0.2f,
            "definition", 0.5f,
            "definition.ngram", 0.3f,
            "definition.autocomplete", 0.1f);
    private static final Map<String, Float> matchPhraseFieldBoostMap = Map.of(
            "entry", 1.5f,
            "definition", 1.2f);

    private final ElasticsearchClient elasticsearchClient;

    @Transactional
    public Page<KeywordSearchResponse> searchKeyword(String keyword, Pageable pageable) {

        // 쿼리 할당
        List<Query> queries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : matchFieldBoostMap.entrySet()) {
            queries.add(ElasticsearchHandler.createMatchQuery(
                    keyword, entry.getKey(), entry.getValue())._toQuery());
        }
        for (Map.Entry<String, Float> entry : matchPhraseFieldBoostMap.entrySet()) {
            queries.add(ElasticsearchHandler.createMatchPhraseQuery(
                    keyword, entry.getKey(), entry.getValue())._toQuery());
        }

        DisMaxQuery esQuery = new DisMaxQuery.Builder().queries(queries).build();

        // 필드 하이라이팅
        Highlight highlight =
                ElasticsearchHandler.createHighlightFieldMap(List.of("entry", "definition"));

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("koreans")
                .from((int) pageable.getOffset())
                .size(pageable.getPageSize())
                .query(q -> q.disMax(esQuery))
                .highlight(highlight)
                .build();

        SearchResponse<ElasticsearchDTO> response = null;

        try {
            response = elasticsearchClient.search(searchRequest, ElasticsearchDTO.class);
        } catch (IOException e) {
            log.error("ES 검색 입출력 에러 발생");
        }

        assert response != null;
        List<KeywordSearchResponse> searchResult =
                ElasticsearchHandler.mapSearchResult(response.hits().hits());

        long totalHits = response.hits().total() ==
                null ? searchResult.size() : response.hits().total().value();

        return new PageImpl<>(searchResult, pageable, totalHits);
    }
}
