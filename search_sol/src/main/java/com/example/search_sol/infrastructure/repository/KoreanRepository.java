package com.example.search_sol.infrastructure.repository;

import com.example.search_sol.domain.entity.Korean;
import com.example.search_sol.domain.repository.CustomKoreanRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KoreanRepository extends JpaRepository<Korean, Long>, CustomKoreanRepository {
}
