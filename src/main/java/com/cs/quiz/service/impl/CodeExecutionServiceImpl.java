package com.cs.quiz.service.impl;

import com.cs.quiz.dto.CodeExecutionResultDto;
import com.cs.quiz.dto.SolutionCodeDto;
import com.cs.quiz.entity.*;
import com.cs.quiz.repository.*;
import com.cs.quiz.service.CodeExecutionService;
import com.cs.quiz.service.OnlineCompilerAPIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CodeExecutionServiceImpl implements CodeExecutionService {

    private final TestCaseRepository testCaseRepository;
    private final UserRepository userRepository;

    private final ProblemScoreRepository problemScoreRepository;
    private final ProblemRepository problemRepository;
    private final OnlineCompilerAPIService onlineCompilerAPIService;

    public CodeExecutionServiceImpl(ProblemScoreRepository problemScoreRepository, ProblemRepository problemRepository, OnlineCompilerAPIService onlineCompilerAPIService, TestCaseRepository testCaseRepository, UserRepository userRepository) {
        this.testCaseRepository = testCaseRepository;
        this.userRepository = userRepository;
        this.onlineCompilerAPIService = onlineCompilerAPIService;
        this.problemRepository = problemRepository;
        this.problemScoreRepository = problemScoreRepository;
    }

    @Override
    public CodeExecutionResultDto executeCode(SolutionCodeDto solution, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        String code = solution.getCode();
        Long problemId = solution.getProblemId();
        ObjectMapper objectMapper = new ObjectMapper();
        CodeExecutionResultDto codeExecutionResultDto = new CodeExecutionResultDto();

        List<TestCase> testCases = testCaseRepository.findAllByProblemId(problemId);

        for (var testcase : testCases) {

            String input = testcase.getInput();
            String output = testcase.getOutput();
            String response = onlineCompilerAPIService.executeCode(code, input, "java");
            try {
                JsonNode jsonNode = objectMapper.readTree(response);
                String codeOutput = jsonNode.get("output").asText();

                if(!output.equals(codeOutput)){
                    codeExecutionResultDto.setMessage("Test case failed!");
                    return codeExecutionResultDto;
                }
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        addProblemScore(solution.getProblemId(), user);

        codeExecutionResultDto.setMessage("SUCCESS!");
        return codeExecutionResultDto;
    }

    private void addProblemScore(Long problemId, User user) {

        Optional<Problem> problemOp = problemRepository.findById(problemId);
        if (problemOp.isEmpty()) {
            return;
        }

        Problem problem = problemOp.get();
        ProblemScore problemScoreObj = problemScoreRepository.findByUserAndProblem(user, problem);

        problemScoreObj.setScore(String.format("%.2f", (double) 100));
        problemScoreRepository.save(problemScoreObj);

        updateTotalProblemScores(user);
    }

    private void updateTotalProblemScores(User user) {
        List<ProblemScore> problemScores = problemScoreRepository.findByUser(user);
        List<Problem> problems = problemRepository.findAll();
        double totalScore = 0;

        for (ProblemScore problemScore : problemScores) {
            totalScore += Double.parseDouble(problemScore.getScore());
        }

        double percentageScore = totalScore / problems.size();
        user.setTotalProblemScore(String.format("%.2f", percentageScore));
        userRepository.save(user);
    }
}
