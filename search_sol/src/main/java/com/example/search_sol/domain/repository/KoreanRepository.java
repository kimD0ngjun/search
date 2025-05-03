package com.example.search_sol.domain.repository;

import com.example.search_sol.domain.entity.Korean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KoreanRepository {
    List<Korean> findAll();
    Optional<Korean> findById(String id);
    Korean save(Korean korean);
}
