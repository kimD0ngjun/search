package com.example.search_sol.presentation.dto;

import com.example.search_sol.application.dto.KoreanUpdateDTO;

public record KoreanUpdateRequest(
        String entry,
        String type,
        String pos,
        String definition
) {
    public KoreanUpdateDTO toDTO() {
        return KoreanUpdateDTO.create(this);
    }
}
