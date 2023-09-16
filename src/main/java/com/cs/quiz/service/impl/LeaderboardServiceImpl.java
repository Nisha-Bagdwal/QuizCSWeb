package com.cs.quiz.service.impl;

import com.cs.quiz.dto.LeaderboardDto;
import com.cs.quiz.dto.UserScoreRankDto;
import com.cs.quiz.entity.User;
import com.cs.quiz.repository.QuizRepository;
import com.cs.quiz.repository.QuizScoreRepository;
import com.cs.quiz.repository.UserRepository;
import com.cs.quiz.service.LeaderboardService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderboardServiceImpl implements LeaderboardService {

    private final QuizRepository quizRepository;
    private final QuizScoreRepository quizScoreRepository;
    private final UserRepository userRepository;

    public LeaderboardServiceImpl(QuizRepository quizRepository, QuizScoreRepository quizScoreRepository, UserRepository userRepository) {
        this.quizRepository = quizRepository;
        this.quizScoreRepository = quizScoreRepository;
        this.userRepository = userRepository;
    }

    @Override
    public LeaderboardDto getRanksAndScoresOfAllUsers(String userEmail) {
        List<User> users = userRepository.findAll();
        users.sort((u1, u2) -> Double.compare(
                Double.parseDouble(u2.getTotalQuizScore()) + Double.parseDouble(u2.getTotalProblemScore()),
                Double.parseDouble(u1.getTotalQuizScore()) + Double.parseDouble(u2.getTotalProblemScore())
        ));

        int rank = 1;
        int userRank = -1;
        List<UserScoreRankDto> userScoreRankDtoList = new ArrayList<>();

        for (User user : users) {
            userScoreRankDtoList.add(mapToUserScoreRankDto(user, rank));
            if (user.getEmail().equals(userEmail)) {
                userRank = rank;
            }
            rank++;
        }

        LeaderboardDto leaderboardDto = new LeaderboardDto();
        leaderboardDto.setAllUsers(userScoreRankDtoList);
        leaderboardDto.setUserRank(userRank);
        return leaderboardDto;
    }

    private UserScoreRankDto mapToUserScoreRankDto(User user, int rank) {
        UserScoreRankDto userScoreRankDto = new UserScoreRankDto();
        userScoreRankDto.setName(user.getName());
        Double totalScore = Double.parseDouble(user.getTotalQuizScore()) + Double.parseDouble(user.getTotalProblemScore())/2;
        userScoreRankDto.setScore(String.format("%.2f", totalScore));
        userScoreRankDto.setRank(rank);
        return userScoreRankDto;
    }
}
