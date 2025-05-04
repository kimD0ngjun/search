package com.example.search_sol.application.dto;

import com.example.search_sol.presentation.dto.SimpleKoreanUpdateRequest;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record SimpleKoreanUpdateDTO(
        String entry,
        String definition
) {
    public static SimpleKoreanUpdateDTO create(SimpleKoreanUpdateRequest request) {
        return SimpleKoreanUpdateDTO.builder()
                .entry(request.entry())
                .definition(request.definition())
                .build();
    }
}
