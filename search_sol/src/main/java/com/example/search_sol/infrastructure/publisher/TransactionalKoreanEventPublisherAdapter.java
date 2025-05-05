package com.example.search_sol.infrastructure.publisher;

import com.example.search_sol.application.dto.KoreanCreateDTO;
import com.example.search_sol.application.dto.KoreanUpdateDTO;
import com.example.search_sol.application.dto.KoreanSimpleUpdateDTO;
import com.example.search_sol.application.event.CreateEvent;
import com.example.search_sol.application.event.DeleteEvent;
import com.example.search_sol.application.event.SimpleUpdateEvent;
import com.example.search_sol.application.event.UpdateEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionalKoreanEventPublisherAdapter implements KoreanEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishCreateEvent(Long id, KoreanCreateDTO dto) {
        eventPublisher.publishEvent(new CreateEvent(id, dto));
    }

    @Override
    public void publishUpdateEvent(Long id, KoreanUpdateDTO dto) {
        eventPublisher.publishEvent(new UpdateEvent(id, dto));
    }

    @Override
    public void publishSimpleUpdateEvent(Long id, KoreanSimpleUpdateDTO dto) {
        eventPublisher.publishEvent(new SimpleUpdateEvent(id, dto));
    }

    @Override
    public void publishDeleteEvent(Long id) {
        eventPublisher.publishEvent(new DeleteEvent(id));
    }
}
