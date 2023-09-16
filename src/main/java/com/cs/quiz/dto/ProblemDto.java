package com.cs.quiz.dto;

import com.cs.quiz.entity.Example;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProblemDto {
    private Long id;
    private String question;
    private String sampleCode;
    private Example example1;
    private Example example2;
}
