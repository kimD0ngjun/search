package com.example.search_sol.presentation.dto;

import com.example.search_sol.application.dto.SimpleKoreanUpdateDTO;

public record SimpleKoreanUpdateRequest(
        String entry,
        String definition
) {
    public SimpleKoreanUpdateDTO toDTO() {
        return SimpleKoreanUpdateDTO.create(this);
    }
}
