package com.cs.quiz.service;

import com.cs.quiz.dto.CodeExecutionResultDto;
import com.cs.quiz.dto.SolutionCodeDto;

public interface CodeExecutionService {
    CodeExecutionResultDto executeCode(SolutionCodeDto solution, String userEmail);
}