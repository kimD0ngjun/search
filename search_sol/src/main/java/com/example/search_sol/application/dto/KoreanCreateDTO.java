package com.example.search_sol.application.dto;

import com.example.search_sol.presentation.dto.KoreanCreateRequest;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record KoreanCreateDTO(
        String entry,
        String type,
        String pos,
        String definition
) {
    public static KoreanCreateDTO create(KoreanCreateRequest request) {
        return KoreanCreateDTO.builder()
                .entry(request.entry())
                .type(request.type())
                .pos(request.pos())
                .definition(request.definition())
                .build();
    }
}
