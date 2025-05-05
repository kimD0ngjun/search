package com.example.search_sol.application.service;

import com.example.search_sol.application.dto.KoreanCreateDTO;
import com.example.search_sol.application.dto.KoreanUpdateDTO;
import com.example.search_sol.application.dto.SimpleKoreanUpdateDTO;
import com.example.search_sol.application.event.CreateEvent;
import com.example.search_sol.application.event.DeleteEvent;
import com.example.search_sol.application.event.SimpleUpdateEvent;
import com.example.search_sol.application.event.UpdateEvent;
import com.example.search_sol.domain.entity.Korean;
import com.example.search_sol.infrastructure.repository.KoreanRepository;
import com.example.search_sol.presentation.dto.KoreanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자 CRUD 애플리케이션 서비스
 */
@Service
@RequiredArgsConstructor
public class KoreanService {

    private final KoreanRepository koreanRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public KoreanResponse getKorean(Long id) {
        return koreanRepository.findById(id)
                .map(KoreanResponse::of)
                .orElseThrow(
                        // 어떤 예외처리? 보상 처리?
                );
    }

    @Transactional
    public KoreanResponse createKorean(KoreanCreateDTO dto) {
        Korean korean = Korean.of(dto);
        korean = koreanRepository.save(korean);

        // 단어 추가 이벤트 발행
        eventPublisher.publishEvent(new CreateEvent(korean.getId(), dto));
        return KoreanResponse.of(korean);
    }

    @Transactional
    public KoreanResponse updateKorean(Long id, KoreanUpdateDTO dto) {
        return koreanRepository.findById(id).map(k -> {
            k.update(dto);
            // 단어 전체 수정 이벤트 발행
            eventPublisher.publishEvent(new UpdateEvent(id, dto));
            return KoreanResponse.of(k);
        }).orElseThrow(
                // 어떤 예외처리? 보상 처리?
        );
    }

    @Transactional
    public KoreanResponse simpleUpdateKorean(Long id, SimpleKoreanUpdateDTO dto) {
        return koreanRepository.findById(id).map(k -> {
            k.update(dto.entry(), dto.definition());
            // 단어 일부 수정(entry, 설명) 이벤트 발행
            eventPublisher.publishEvent(new SimpleUpdateEvent(id, dto));
            return KoreanResponse.of(k);
        }).orElseThrow(
                // 어떤 예외처리? 보상 처리?
        );
    }

    @Transactional
    public KoreanResponse deleteKorean(Long id) {
        KoreanResponse response = koreanRepository.findById(id)
                .map(KoreanResponse::of)
                .orElseThrow(
                        // 어떤 예외처리? 보상 처리?
                );

        koreanRepository.deleteById(id);
        // 단어 삭제 이벤트 발행
        eventPublisher.publishEvent(new DeleteEvent(id));
        return response;
    }
}
