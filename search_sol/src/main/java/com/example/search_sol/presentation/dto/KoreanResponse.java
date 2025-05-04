package com.example.search_sol.presentation.dto;

import com.example.search_sol.domain.entity.Korean;

public record KoreanResponse(
        Long id,
        String entry,
        String type,
        String pos,
        String definition
) {
    public static KoreanResponse of(Korean korean) {
        return new KoreanResponse(
                korean.getId(), korean.getEntry(), korean.getType(), korean.getPos(), korean.getDefinition());
    }
}
