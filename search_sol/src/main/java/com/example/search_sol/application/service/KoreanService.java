package com.example.search_sol.application.service;

import com.example.search_sol.domain.repository.KoreanRepository;
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

    @Transactional(readOnly = true)
    public KoreanResponse getKorean(Long id) {
        return koreanRepository.findById(id)
                .map(KoreanResponse::of)
                .orElseThrow();
    }

}
