package com.example.search_sol.application.service;

import com.example.search_sol.application.dto.KoreanCreateDTO;
import com.example.search_sol.application.dto.KoreanUpdateDTO;
import com.example.search_sol.application.dto.KoreanSimpleUpdateDTO;
import com.example.search_sol.domain.entity.Korean;
import com.example.search_sol.infrastructure.publisher.KoreanEventPublisher;
import com.example.search_sol.infrastructure.repository.KoreanRepository;
import com.example.search_sol.presentation.dto.KoreanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 관리자 CRUD 애플리케이션 서비스
 */
@Service
@RequiredArgsConstructor
public class KoreanService {

    private final KoreanRepository koreanRepository;
    private final KoreanEventPublisher eventPublisher;

    @Transactional(
            readOnly = true,
            transactionManager = "dataTransactionManager"
    )
    public KoreanResponse getKorean(Long id) {
        return koreanRepository.findById(id)
                .map(KoreanResponse::of)
                .orElseThrow(
                        // 어떤 예외처리? 보상 처리?
                );
    }

    @Transactional(transactionManager = "dataTransactionManager")
    public KoreanResponse createKorean(KoreanCreateDTO dto) {
        Korean korean = Korean.of(dto);
        korean = koreanRepository.save(korean);

        // 단어 추가 이벤트 발행
        eventPublisher.publishCreateEvent(korean.getId(), dto);
        return KoreanResponse.of(korean);
    }

    @Transactional(transactionManager = "dataTransactionManager")
    public KoreanResponse updateKorean(Long id, KoreanUpdateDTO dto) {
        return koreanRepository.findById(id).map(k -> {
            k.update(dto);
            // 단어 전체 수정 이벤트 발행
            eventPublisher.publishUpdateEvent(id, dto);
            return KoreanResponse.of(k);
        }).orElseThrow(
                // 어떤 예외처리? 보상 처리?
        );
    }

    @Transactional(transactionManager = "dataTransactionManager")
    public KoreanResponse simpleUpdateKorean(Long id, KoreanSimpleUpdateDTO dto) {
        return koreanRepository.findById(id).map(k -> {
            k.update(dto.entry(), dto.definition());
            // 단어 일부 수정(entry, 설명) 이벤트 발행
            eventPublisher.publishSimpleUpdateEvent(id, dto);
            return KoreanResponse.of(k);
        }).orElseThrow(
                // 어떤 예외처리? 보상 처리?
        );
    }

    @Transactional(transactionManager = "dataTransactionManager")
    public KoreanResponse deleteKorean(Long id) {
        KoreanResponse response = koreanRepository.findById(id)
                .map(KoreanResponse::of)
                .orElseThrow(
                        // 어떤 예외처리? 보상 처리?
                );

        koreanRepository.deleteById(id);
        // 단어 삭제 이벤트 발행
        eventPublisher.publishDeleteEvent(id);
        return response;
    }
}
