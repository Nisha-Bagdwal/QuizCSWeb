package com.cs.quiz.service.impl;

import com.cs.quiz.dto.AllQuizzesScoreDto;
import com.cs.quiz.dto.QuizScoreDto;
import com.cs.quiz.entity.QuizScore;
import com.cs.quiz.entity.User;
import com.cs.quiz.repository.QuizScoreRepository;
import com.cs.quiz.repository.UserRepository;
import com.cs.quiz.service.QuizService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuizScoreRepository quizScoreRepository;
    private final UserRepository userRepository;

    public QuizServiceImpl(QuizScoreRepository quizScoreRepository, UserRepository userRepository) {
        this.quizScoreRepository = quizScoreRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AllQuizzesScoreDto getAllQuizzesWithScoresForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        List<QuizScore> quizScores = quizScoreRepository.findByUser(user);
        List<QuizScoreDto> quizScoreDtoList = quizScores.stream()
                .map(this::mapToQuizScoreDto)
                .sorted((q1, q2) -> q1.getLevel() - q2.getLevel())
                .collect(Collectors.toList());

        AllQuizzesScoreDto allQuizzesScoreDto = new AllQuizzesScoreDto();
        allQuizzesScoreDto.setQuizzes(quizScoreDtoList);
        allQuizzesScoreDto.setTotalScores(user.getTotalQuizScore());
        return allQuizzesScoreDto;
    }

    private QuizScoreDto mapToQuizScoreDto(QuizScore quizScore) {
        QuizScoreDto quizScoreDto = new QuizScoreDto();
        quizScoreDto.setQuizId(quizScore.getQuiz().getId());
        quizScoreDto.setName(quizScore.getQuiz().getName());
        quizScoreDto.setLevel(quizScore.getQuiz().getLevel());
        quizScoreDto.setScore(String.format("%.2f", Double.valueOf(quizScore.getScore())));

        return quizScoreDto;
    }
}
