package com.cs.quiz.controller;

import com.cs.quiz.entity.User;
import com.cs.quiz.service.QuizService;
import com.cs.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QuizController {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    @GetMapping("/home/quizzes")
    public String quizzes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findUserByEmail(userEmail);
        model.addAttribute("username", user.getName());
        model.addAttribute("allQuizzesScores", quizService.getAllQuizzesWithScoresForUser(userEmail));
        return "quizzes";
    }
}
