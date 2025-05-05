package com.example.search_sol.infrastructure.publisher;

import com.example.search_sol.application.dto.KoreanCreateDTO;
import com.example.search_sol.application.dto.KoreanUpdateDTO;
import com.example.search_sol.application.dto.KoreanSimpleUpdateDTO;

public interface KoreanEventPublisher {
    void publishCreateEvent(Long id, KoreanCreateDTO dto);
    void publishUpdateEvent(Long id, KoreanUpdateDTO dto);
    void publishSimpleUpdateEvent(Long id, KoreanSimpleUpdateDTO dto);
    void publishDeleteEvent(Long id);
}
