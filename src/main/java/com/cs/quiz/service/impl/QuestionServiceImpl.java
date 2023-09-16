package com.cs.quiz.service.impl;

import com.cs.quiz.dto.QuestionDto;
import com.cs.quiz.entity.Question;
import com.cs.quiz.entity.Quiz;
import com.cs.quiz.repository.QuestionRepository;
import com.cs.quiz.repository.QuizRepository;
import com.cs.quiz.service.QuestionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuizRepository quizRepository) {
        this.questionRepository = questionRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public List<QuestionDto> getAllQuestionsByQuizId(Long quizId) {
        Optional<Quiz> quizOp = quizRepository.findById(quizId);
        if (quizOp.isEmpty()) {
            return new ArrayList<>();
        }

        Quiz quiz = quizOp.get();
        List<Question> questions = questionRepository.findAllByQuiz(quiz);
        return questions.stream()
                .map(this::mapToQuestionDto)
                .collect(Collectors.toList());
    }

    private QuestionDto mapToQuestionDto(Question question) {

        QuestionDto questionDto = new QuestionDto();
        questionDto.setId(question.getId());
        questionDto.setQuestion(question.getQuestion());
        questionDto.setOption1(question.getOption1());
        questionDto.setOption2(question.getOption2());
        questionDto.setOption3(question.getOption3());
        questionDto.setOption4(question.getOption4());

        return questionDto;
    }
}
