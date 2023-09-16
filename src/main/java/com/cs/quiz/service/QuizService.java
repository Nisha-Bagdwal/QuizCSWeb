package com.cs.quiz.service;

import com.cs.quiz.dto.AllQuizzesScoreDto;

public interface QuizService {

    AllQuizzesScoreDto getAllQuizzesWithScoresForUser(String userEmail);
}
