package com.example.search_sol.presentation.dto;

import com.example.search_sol.application.dto.KoreanCreateDTO;

public record KoreanCreateRequest(
        String entry,
        String type,
        String pos,
        String definition
) {
    public KoreanCreateDTO toDTO() {
        return KoreanCreateDTO.create(this);
    }
}
