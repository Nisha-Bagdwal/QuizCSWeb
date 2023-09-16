package com.cs.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemScoreDto {

    private Long problemId;
    private String name;
    private Integer level;
    private String score;
}
