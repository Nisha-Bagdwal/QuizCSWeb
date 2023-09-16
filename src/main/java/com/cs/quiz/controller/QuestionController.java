package com.cs.quiz.controller;

import com.cs.quiz.entity.User;
import com.cs.quiz.service.QuestionService;
import com.cs.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @GetMapping("/home/quiz/{quiz_id}/questions")
    public String questions(@PathVariable(value = "quiz_id") Long quizId, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findUserByEmail(userEmail);
        model.addAttribute("username", user.getName());
        model.addAttribute("questions", questionService.getAllQuestionsByQuizId(quizId));
        model.addAttribute("quizId", quizId);
        return "questions";
    }
}
