package com.cs.quiz.repository;

import com.cs.quiz.entity.Quiz;
import com.cs.quiz.entity.QuizScore;
import com.cs.quiz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizScoreRepository extends JpaRepository<QuizScore, Long> {

    List<QuizScore> findByUser(User user);

    QuizScore findByUserAndQuiz(User user, Quiz quiz);
}
