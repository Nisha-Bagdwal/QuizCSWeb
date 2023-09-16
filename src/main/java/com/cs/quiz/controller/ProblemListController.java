package com.cs.quiz.controller;

import com.cs.quiz.entity.User;
import com.cs.quiz.service.ProblemListService;
import com.cs.quiz.service.QuizService;
import com.cs.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProblemListController {

    @Autowired
    private ProblemListService problemListService;

    @Autowired
    private UserService userService;

    @GetMapping("/home/problems")
    public String quizzes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findUserByEmail(userEmail);
        model.addAttribute("username", user.getName());
        model.addAttribute("allProblemsScores", problemListService.getAllProblemsWithScoresForUser(userEmail));
        return "codingProblemsList";
    }

    @GetMapping("/home/problem/{problem_id}/statement")
    public String questions(@PathVariable(value = "problem_id") Long problemId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findUserByEmail(userEmail);
        model.addAttribute("username", user.getName());
        model.addAttribute("problem", problemListService.getProblemByProblemId(problemId));
        return "codingProblem";
    }
}
