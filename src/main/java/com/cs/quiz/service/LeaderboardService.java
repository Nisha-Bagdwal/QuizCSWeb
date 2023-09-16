package com.cs.quiz.service;

import com.cs.quiz.dto.LeaderboardDto;

public interface LeaderboardService {

    LeaderboardDto getRanksAndScoresOfAllUsers(String userEmail);
}
