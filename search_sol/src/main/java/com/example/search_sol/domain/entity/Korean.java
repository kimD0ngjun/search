package com.example.search_sol.domain.entity;

import com.example.search_sol.application.dto.KoreanCreateDTO;
import com.example.search_sol.application.dto.KoreanUpdateDTO;
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

    /**
     * 관리자가 사용할 단어 추가 정적 메소드
     */
    // 정적 팩토리 메소드 패턴
    public static Korean of(KoreanCreateDTO dto) {
        Korean korean = new Korean();
        korean.entry = dto.entry();
        korean.type = dto.type();
        korean.pos = dto.pos();
        korean.definition = dto.definition();
        return korean;
    }

    public void update(String entry, String definition) {
        this.entry = entry;
        this.definition = definition;
    }

    public void update(KoreanUpdateDTO dto) {
        update(dto.entry(), dto.definition());
        this.type = dto.type();
        this.pos = dto.pos();
    }
}
