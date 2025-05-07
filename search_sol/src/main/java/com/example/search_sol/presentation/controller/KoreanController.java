package com.example.search_sol.presentation.controller;

import com.example.search_sol.application.service.KoreanService;
import com.example.search_sol.presentation.dto.KoreanCreateRequest;
import com.example.search_sol.presentation.dto.KoreanResponse;
import com.example.search_sol.presentation.dto.KoreanUpdateRequest;
import com.example.search_sol.presentation.dto.SimpleKoreanUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/korean")
@RequiredArgsConstructor
public class KoreanController {

    private final KoreanService koreanService;

    @GetMapping
    public ResponseEntity<KoreanResponse> getKorean(@RequestParam Long id) {
        return ResponseEntity.ok(koreanService.getKorean(id));
    }

    @PostMapping
    public ResponseEntity<KoreanResponse> createKorean(@RequestBody KoreanCreateRequest request) {
        return ResponseEntity.ok(koreanService.createKorean(request.toDTO()));
    }

    @PutMapping("/update")
    public ResponseEntity<KoreanResponse> updateKorean(
            @RequestParam Long id,
            @RequestBody KoreanUpdateRequest request) {
        return ResponseEntity.ok(koreanService.updateKorean(id,request.toDTO()));
    }

    @PatchMapping("/simple-update")
    public ResponseEntity<KoreanResponse> simpleUpdateKorean(
            @RequestParam Long id,
            @RequestBody SimpleKoreanUpdateRequest request
    ) {
        return ResponseEntity.ok(koreanService.simpleUpdateKorean(id, request.toDTO()));
    }

    @DeleteMapping
    public ResponseEntity<KoreanResponse> deleteKorean(@RequestParam Long id) {
        return ResponseEntity.ok(koreanService.deleteKorean(id));
    }
}
