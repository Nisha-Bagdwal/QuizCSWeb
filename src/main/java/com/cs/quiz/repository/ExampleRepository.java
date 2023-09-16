package com.cs.quiz.repository;

import com.cs.quiz.entity.Example;
import com.cs.quiz.entity.Problem;
import com.cs.quiz.entity.Question;
import com.cs.quiz.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExampleRepository extends JpaRepository<Example, Long> {

    List<Example> findAllByProblem(Problem problem);
}
