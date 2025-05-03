package com.example.search_sol.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "koreans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// JPA 생성자 접근 제어 protected, 캡슐화 유지
public class Korean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entry")
    private String entry;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "pos", length = 50)
    private String pos;

    /**
     * text는 varchar보다 최대 길이가 크지만 조회가 덜 될 때 유리한편
     **/
    // @Lob
    @Column(name = "definition", length = 500)
    private String definition;
}
