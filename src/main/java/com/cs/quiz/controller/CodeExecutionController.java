package com.cs.quiz.controller;

import com.cs.quiz.dto.CodeExecutionResultDto;
import com.cs.quiz.dto.SolutionCodeDto;
import com.cs.quiz.service.CodeExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class CodeExecutionController {

    private final CodeExecutionService codeExecutionService;

    @Autowired
    public CodeExecutionController(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }

    @PostMapping("/home/problem/execute")
    public CodeExecutionResultDto executeCode(@RequestBody SolutionCodeDto solution) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return codeExecutionService.executeCode(solution, userEmail);
    }
}
