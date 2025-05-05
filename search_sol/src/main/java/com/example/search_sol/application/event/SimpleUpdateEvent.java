package com.example.search_sol.application.event;

import com.example.search_sol.application.dto.SimpleKoreanUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleUpdateEvent {
    private SimpleKoreanUpdateDTO dto;
}
