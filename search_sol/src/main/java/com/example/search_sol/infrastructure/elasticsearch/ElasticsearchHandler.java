package com.example.search_sol.infrastructure.elasticsearch;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticsearchHandler {
    /**
     * match query
     */
    public static MatchQuery createMatchQuery(
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
    public static MatchPhraseQuery createMatchPhraseQuery(
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
    public static Highlight createHighlightFieldMap(List<String> fieldNames) {
        Map<String, HighlightField> highlightFieldMap = new HashMap<>();
        fieldNames.forEach(e -> highlightFieldMap
                .put(e, new HighlightField.Builder()
                        .preTags("<strong>")
                        .postTags("</strong>")
                        .build()));

        return new Highlight.Builder().fields(highlightFieldMap).build();
    }
}
