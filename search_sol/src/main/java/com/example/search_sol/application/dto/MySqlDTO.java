package com.example.search_sol.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
