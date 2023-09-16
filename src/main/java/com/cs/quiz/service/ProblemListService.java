package com.cs.quiz.service;

import com.cs.quiz.dto.AllProblemsScoreDto;
import com.cs.quiz.dto.ProblemDto;
import com.cs.quiz.dto.QuestionDto;

import java.util.List;

public interface ProblemListService {

    AllProblemsScoreDto getAllProblemsWithScoresForUser(String userEmail);

    ProblemDto getProblemByProblemId(Long problemId);
}
