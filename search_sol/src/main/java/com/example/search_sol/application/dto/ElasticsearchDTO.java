package com.example.search_sol.application.dto;

public record ElasticsearchDTO(
        String entry,
        String type,
        String pos,
        String definition) {
    public static ElasticsearchDTO of(MySqlDTO dto) {
        return new ElasticsearchDTO(dto.getEntry(), dto.getType(), dto.getPos(), dto.getDefinition());
    }
}
