package com.cs.quiz.controller;

import com.cs.quiz.entity.User;
import com.cs.quiz.service.LeaderboardService;
import com.cs.quiz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LeaderboardController {

    @Autowired
    private LeaderboardService leaderboardService;

    @Autowired
    private UserService userService;

    @GetMapping("/home/leaderboard")
    public String getLeaderboard(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        User user = userService.findUserByEmail(userEmail);
        model.addAttribute("username", user.getName());
        model.addAttribute("leaderboard", leaderboardService.getRanksAndScoresOfAllUsers(userEmail));
        return "leaderboard";
    }
}
