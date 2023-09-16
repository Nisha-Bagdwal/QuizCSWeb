package com.cs.quiz.service.impl;

import com.cs.quiz.dto.AnswersDto;
import com.cs.quiz.entity.Question;
import com.cs.quiz.entity.Quiz;
import com.cs.quiz.entity.QuizScore;
import com.cs.quiz.entity.User;
import com.cs.quiz.repository.QuestionRepository;
import com.cs.quiz.repository.QuizRepository;
import com.cs.quiz.repository.QuizScoreRepository;
import com.cs.quiz.repository.UserRepository;
import com.cs.quiz.service.ScoreService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class ScoreServiceImpl implements ScoreService {

    private final QuizScoreRepository quizScoreRepository;
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    public ScoreServiceImpl(QuizScoreRepository quizScoreRepository, QuestionRepository questionRepository, QuizRepository quizRepository, UserRepository userRepository) {
        this.quizScoreRepository = quizScoreRepository;
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void calculateScores(String userEmail, AnswersDto answersDto) {
        User user = userRepository.findByEmail(userEmail);
        Optional<Quiz> quizOp = quizRepository.findById(answersDto.getQuizId());
        if (quizOp.isEmpty()) {
            return;
        }

        Quiz quiz = quizOp.get();

        List<Question> questions = questionRepository.findAllByQuiz(quiz);
        Map<Long, Integer> answers = answersDto.getAnswers();
        int score = 0;

        for (Question question : questions) {
            if (Objects.equals(answers.get(question.getId()), question.getAnswer())) {
                score++;
            }
        }

        double div = (double) score / questions.size();
        double percentageScore = div * 100;

        QuizScore quizScoreObj = quizScoreRepository.findByUserAndQuiz(user, quiz);
        quizScoreObj.setScore(String.format("%.2f", percentageScore));
        quizScoreRepository.save(quizScoreObj);

        updateTotalQuizScores(user);
    }

    private void updateTotalQuizScores(User user) {
        List<QuizScore> quizScores = quizScoreRepository.findByUser(user);
        List<Quiz> quizzes = quizRepository.findAll();
        double totalScore = 0;

        for (QuizScore quizScore : quizScores) {
            totalScore += Double.parseDouble(quizScore.getScore());
        }

        double percentageScore = totalScore / quizzes.size();
        user.setTotalQuizScore(String.format("%.2f", percentageScore));
        userRepository.save(user);
    }
}
