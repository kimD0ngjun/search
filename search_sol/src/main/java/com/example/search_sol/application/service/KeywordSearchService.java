package com.example.search_sol.application.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.search_sol.application.dto.ElasticsearchDTO;
import com.example.search_sol.presentation.dto.KeywordSearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
    public List<KeywordSearchResponse> searchKeyword(String keyword) {

        // 쿼리 할당
        List<Query> queries = new ArrayList<>();
        for (Map.Entry<String, Float> entry : matchFieldBoostMap.entrySet()) {
            queries.add(createMatchQuery(keyword, entry.getKey(), entry.getValue())._toQuery());
        }
        for (Map.Entry<String, Float> entry : matchPhraseFieldBoostMap.entrySet()) {
            queries.add(createMatchPhraseQuery(keyword, entry.getKey(), entry.getValue())._toQuery());
        }

        DisMaxQuery esQuery = new DisMaxQuery.Builder().queries(queries).build();

        // 필드 하이라이팅
        Highlight highlight = createHighlightFieldMap(List.of("entry", "definition"));

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index("koreans")
                .size(10)
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
        return mapSearchResult(response.hits().hits());
    }

    /**
     * match query
     */
    private MatchQuery createMatchQuery(
            String keyword, String fieldName, Float boostValue) {
        return new MatchQuery.Builder()
                .query(keyword)
                .field(fieldName)
                .boost(boostValue)
                .build();
    }

    /**
     * 단어 순서 고려 match query
     */
    private MatchPhraseQuery createMatchPhraseQuery(
            String keyword, String fieldName, Float boostValue) {
        return new MatchPhraseQuery.Builder()
                .query(keyword)
                .field(fieldName)
                .boost(boostValue)
                .build();
    }

    /**
     * 하이라이트 생성기
     */
    private Highlight createHighlightFieldMap(List<String> fieldNames) {
        Map<String, HighlightField> highlightFieldMap = new HashMap<>();
        fieldNames.forEach(e -> highlightFieldMap
                .put(e, new HighlightField.Builder()
                        .preTags("<strong>")
                        .postTags("</strong>")
                        .build()));

        return new Highlight.Builder().fields(highlightFieldMap).build();
    }

    /**
     * 하이라이트 매핑 DTO 처리
     */
    public List<KeywordSearchResponse> mapSearchResult(List<Hit<ElasticsearchDTO>> hits) {
        return hits.stream().map(hit -> {
            assert hit.id() != null;
            Long id = Long.parseLong(hit.id());

//            log.info("id: {}", id);
//            log.info("추천점수: {}", hit.score());
            ElasticsearchDTO source = hit.source();
            Map<String, List<String>> highlights = hit.highlight();

            assert source != null;
            String entry = source.entry(), definition = source.definition();

            if (highlights.containsKey("entry")) {
                entry = highlights.get("entry").getFirst();
            }

            if (highlights.containsKey("definition")) {
                definition = highlights.get("definition").getFirst();
            }

            return new KeywordSearchResponse(
                    id, hit.score(), entry, source.type(), source.pos(), definition);
        }).toList();
    }
}
