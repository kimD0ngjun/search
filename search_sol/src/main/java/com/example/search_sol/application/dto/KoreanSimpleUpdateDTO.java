package com.example.search_sol.application.dto;

import com.example.search_sol.presentation.dto.SimpleKoreanUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record KoreanSimpleUpdateDTO(
        String entry,
        String definition
) {
    public static KoreanSimpleUpdateDTO create(SimpleKoreanUpdateRequest request) {
        return KoreanSimpleUpdateDTO.builder()
                .entry(request.entry())
                .definition(request.definition())
                .build();
    }
}
