package com.example.search_sol.application.dto;

import com.example.search_sol.presentation.dto.KoreanUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record KoreanUpdateDTO(
        String entry,
        String type,
        String pos,
        String definition
) {
    public static KoreanUpdateDTO create(KoreanUpdateRequest request) {
        return KoreanUpdateDTO.builder()
                .entry(request.entry())
                .type(request.type())
                .pos(request.pos())
                .definition(request.definition())
                .build();
    }
}
