package com.example.search_sol.application.event;

import com.example.search_sol.application.dto.KoreanCreateDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateEvent {
    private KoreanCreateDTO dto;
}
