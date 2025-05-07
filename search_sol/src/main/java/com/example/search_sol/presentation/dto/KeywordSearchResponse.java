package com.example.search_sol.presentation.dto;

public record KeywordSearchResponse(
        Long id,
        Double score,
        String entry,
        String type,
        String pos,
        String definition
) {
}
