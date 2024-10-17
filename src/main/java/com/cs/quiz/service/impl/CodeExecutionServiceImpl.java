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
import java.util.concurrent.CompletableFuture;

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

        // Create a list of CompletableFutures for each test case
        List<CompletableFuture<String>> futures = testCases.stream()
                .map(testCase -> onlineCompilerAPIService.executeCode(code, testCase.getInput(), "java")
                        .thenApply(response -> {
                            try {
                                JsonNode jsonNode = objectMapper.readTree(response);
                                String codeOutput = jsonNode.get("output").asText();
                                boolean isMatch = testCase.getOutput().equals(codeOutput);

                                if (!isMatch) {
                                    // Return the error response from the API if output does not match
                                    return "Error: Test case failed for input: " + testCase.getInput() +
                                            ". Expected: " + testCase.getOutput() + ", but got: " + codeOutput;
                                }
                                // Return a success message if the output matches
                                return "SUCCESS";
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        })
                ).toList();  // Convert the stream to a list of futures

        // Wait for all async tasks to complete and gather the results
        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Once all futures are completed, check results
        allOf.join(); // Wait for all tasks to finish

        // Collect results and check for errors
        for (CompletableFuture<String> future : futures) {
            String result = future.join(); // Get the result of each future
            if (result.startsWith("Error:")) { // Check if it's an error response
                codeExecutionResultDto.setMessage(result); // Set the error message
                return codeExecutionResultDto;  // Exit early on error
            }
        }

        // If all test cases pass
        addProblemScore(solution.getProblemId(), user);
        codeExecutionResultDto.setMessage("All test cases passed successfully!");
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
