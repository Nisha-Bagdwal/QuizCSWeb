package com.cs.quiz.service.impl;

import com.cs.quiz.dto.AllProblemsScoreDto;
import com.cs.quiz.dto.ProblemDto;
import com.cs.quiz.dto.ProblemScoreDto;
import com.cs.quiz.entity.*;
import com.cs.quiz.repository.ExampleRepository;
import com.cs.quiz.repository.ProblemRepository;
import com.cs.quiz.repository.ProblemScoreRepository;
import com.cs.quiz.repository.UserRepository;
import com.cs.quiz.service.ProblemListService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProblemListServiceImpl implements ProblemListService {

    private final ProblemRepository problemRepository;
    private final ProblemScoreRepository problemScoreRepository;

    private final ExampleRepository exampleRepository;
    private final UserRepository userRepository;

    public ProblemListServiceImpl(ProblemRepository problemRepository,
                                  ProblemScoreRepository problemScoreRepository,
                                  UserRepository userRepository,
                                  ExampleRepository exampleRepository) {
        this.problemRepository = problemRepository;
        this.problemScoreRepository = problemScoreRepository;
        this.userRepository = userRepository;
        this.exampleRepository = exampleRepository;
    }

    @Override
    public AllProblemsScoreDto getAllProblemsWithScoresForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        List<ProblemScore> problemScores = problemScoreRepository.findByUser(user);
        List<ProblemScoreDto> problemScoreDtoList = problemScores.stream()
                .map(this::mapToProblemScoreDto)
                .sorted((q1, q2) -> q1.getLevel() - q2.getLevel())
                .collect(Collectors.toList());

        AllProblemsScoreDto allProblemsScoreDto = new AllProblemsScoreDto();
        allProblemsScoreDto.setProblems(problemScoreDtoList);
        allProblemsScoreDto.setTotalScores(user.getTotalProblemScore());
        return allProblemsScoreDto;
    }

    @Override
    public ProblemDto getProblemByProblemId(Long problemId) {
        Optional<Problem> problemOp = problemRepository.findById(problemId);
        if (problemOp.isEmpty()) {
            return new ProblemDto();
        }

        Problem problem = problemOp.get();
        List<Example> examples = exampleRepository.findAllByProblem(problem);
        return mapToProblemDto(problem, examples);
    }

    private ProblemDto mapToProblemDto(Problem problem, List<Example> examples) {

        ProblemDto problemDto = new ProblemDto();

        problemDto.setId(problem.getId());
        problemDto.setQuestion(problem.getQuestion());
        problemDto.setExample1(examples.get(0));
        problemDto.setExample2(examples.get(1));
        problemDto.setSampleCode(problem.getSampleCode());

        return problemDto;
    }

    private ProblemScoreDto mapToProblemScoreDto(ProblemScore problemScore) {
        ProblemScoreDto problemScoreDto = new ProblemScoreDto();
        problemScoreDto.setProblemId(problemScore.getProblem().getId());
        problemScoreDto.setName(problemScore.getProblem().getName());
        problemScoreDto.setLevel(problemScore.getProblem().getLevel());
        problemScoreDto.setScore(String.format("%.2f", Double.valueOf(problemScore.getScore())));

        return problemScoreDto;
    }
}
