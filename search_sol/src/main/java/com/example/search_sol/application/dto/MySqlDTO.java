package com.example.search_sol.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 배치 작업에서 쿼리 프로바이딩할 때 리플렉션으로 세터 필요해서 불변객체 사용 불가...
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MySqlDTO {
    private Long id;
    private String entry;
    private String type;
    private String pos;
    private String definition;
}
