package com.example.search_sol.application.dto;

public record ElasticsearchDTO(
        String entry,
        String type,
        String pos,
        String definition) {
    public static ElasticsearchDTO of(MigrationDTO dto) {
        return new ElasticsearchDTO(dto.entry(), dto.type(), dto.pos(), dto.definition());
    }
}
