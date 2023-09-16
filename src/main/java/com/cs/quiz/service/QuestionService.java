package com.cs.quiz.service;

import com.cs.quiz.dto.QuestionDto;

import java.util.List;

public interface QuestionService {

    List<QuestionDto> getAllQuestionsByQuizId(Long quizId);
}
