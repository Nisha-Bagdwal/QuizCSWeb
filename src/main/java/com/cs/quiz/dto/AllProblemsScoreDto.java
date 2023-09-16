package com.cs.quiz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AllProblemsScoreDto {

    private List<ProblemScoreDto> problems;
    private String totalScores;
}
