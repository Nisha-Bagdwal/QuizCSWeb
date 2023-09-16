package com.cs.quiz.controller;

import com.cs.quiz.dto.AnswersDto;
import com.cs.quiz.service.QuizService;
import com.cs.quiz.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private QuizService quizService;

    @PostMapping("/home/quiz/answers")
    public String calculateScore(@RequestBody AnswersDto answersDto, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        scoreService.calculateScores(userEmail, answersDto);
        return "redirect:/";
    }
}
