package com.example.search_sol.presentation.controller;

import com.example.search_sol.application.service.KeywordSearchService;
import com.example.search_sol.presentation.dto.KeywordSearchRequest;
import com.example.search_sol.presentation.dto.KeywordSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class KeywordSearchController {

    private final KeywordSearchService keywordSearchService;

    @GetMapping
    public ResponseEntity<List<KeywordSearchResponse>> searchKeyword(
            @RequestBody KeywordSearchRequest request) {
        return ResponseEntity.ok(keywordSearchService.searchKeyword(request.keyword()));
    }
}
