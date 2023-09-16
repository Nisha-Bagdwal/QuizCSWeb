package com.cs.quiz.repository;

import com.cs.quiz.entity.Example;
import com.cs.quiz.entity.Problem;
import com.cs.quiz.entity.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findAllByProblemId(Long problemId);
}
