package com.example.search_sol.application.event;

import com.example.search_sol.application.dto.KoreanSimpleUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleUpdateEvent {
    private Long id;
    private KoreanSimpleUpdateDTO dto;
}
