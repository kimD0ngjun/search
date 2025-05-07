package com.example.search_sol.presentation.controller;

import com.example.search_sol.application.service.KeywordSearchService;
import com.example.search_sol.presentation.dto.KeywordSearchResponse;
import com.example.search_sol.presentation.dto.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class KeywordSearchController {

    private final KeywordSearchService keywordSearchService;

    @GetMapping
    public ResponseEntity<PageResponse<KeywordSearchResponse>> searchKeyword(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<KeywordSearchResponse> page = keywordSearchService.searchKeyword(keyword, pageable);
        return ResponseEntity.ok(PageResponse.from(page));
    }
}
