package com.cs.quiz.repository;

import com.cs.quiz.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemScoreRepository extends JpaRepository<ProblemScore, Long> {

    List<ProblemScore> findByUser(User user);

    ProblemScore findByUserAndProblem(User user, Problem problem);
}
