package com.example.search_sol.application.event;

import com.example.search_sol.application.dto.KoreanUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateEvent {
    private KoreanUpdateDTO dto;
}
