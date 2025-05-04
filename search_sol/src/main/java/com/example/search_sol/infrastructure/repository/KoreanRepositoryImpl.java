package com.example.search_sol.infrastructure.repository;

import com.example.search_sol.domain.entity.Korean;
import com.example.search_sol.domain.repository.KoreanRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KoreanRepositoryImpl extends JpaRepository<Korean, Long>, KoreanRepository {
}
