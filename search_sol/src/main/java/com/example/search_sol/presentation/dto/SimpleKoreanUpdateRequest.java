package com.example.search_sol.presentation.dto;

import com.example.search_sol.application.dto.KoreanSimpleUpdateDTO;

public record SimpleKoreanUpdateRequest(
        String entry,
        String definition
) {
    public KoreanSimpleUpdateDTO toDTO() {
        return KoreanSimpleUpdateDTO.create(this);
    }
}
