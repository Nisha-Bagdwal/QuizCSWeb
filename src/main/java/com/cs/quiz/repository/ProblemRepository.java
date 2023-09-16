package com.cs.quiz.repository;

import com.cs.quiz.entity.Problem;
import com.cs.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
}
