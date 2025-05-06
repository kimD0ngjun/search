package com.example.search_sol.presentation.dto;

public record KeywordSearchResponse(
        Long id,
        String entry,
        String type,
        String pos,
        String definition
) {
}
