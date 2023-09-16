package com.cs.quiz.service;

import com.cs.quiz.dto.AnswersDto;

public interface ScoreService {
    void calculateScores(String userEmail, AnswersDto answersDto);
}