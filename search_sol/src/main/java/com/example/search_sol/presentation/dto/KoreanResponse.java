package com.example.search_sol.presentation.dto;

import com.example.search_sol.domain.entity.Korean;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record KoreanResponse(
        Long id,
        String entry,
        String type,
        String pos,
        String definition
) {
    public static KoreanResponse of(Korean korean) {
        return KoreanResponse.builder()
                .id(korean.getId())
                .entry(korean.getEntry())
                .type(korean.getType())
                .pos(korean.getPos())
                .definition(korean.getDefinition())
                .build();
    }
}
