package com.cs.quiz.service.impl;

import com.cs.quiz.dto.UserDto;
import com.cs.quiz.entity.*;
import com.cs.quiz.repository.*;
import com.cs.quiz.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final QuizScoreRepository quizScoreRepository;

    private final ProblemRepository problemRepository;

    private final ProblemScoreRepository problemScoreRepository;
    private final QuizRepository quizRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           QuizScoreRepository quizScoreRepository,
                           QuizRepository quizRepository,
                           PasswordEncoder passwordEncoder,
                           ProblemRepository problemRepository,
                           ProblemScoreRepository problemScoreRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.quizScoreRepository = quizScoreRepository;
        this.quizRepository = quizRepository;
        this.passwordEncoder = passwordEncoder;
        this.problemRepository = problemRepository;
        this.problemScoreRepository = problemScoreRepository;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setTotalQuizScore("0.00");
        user.setTotalProblemScore("0.00");
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = checkRoleExist();
        }
        user.setRoles(List.of(role));
        userRepository.save(user);
        initializeQuizScores(user);
        initializeProblemScores(user);
    }

    private void initializeQuizScores(User user) {
        List<Quiz> quizzes = quizRepository.findAll();
        List<QuizScore> quizScores = quizzes.stream()
                .map(quiz -> createQuizScores(user, quiz))
                .collect(Collectors.toList());

        quizScoreRepository.saveAll(quizScores);
    }

    private void initializeProblemScores(User user) {
        List<Problem> problems = problemRepository.findAll();
        List<ProblemScore> problemScores = problems.stream()
                .map(problem -> createProblemScores(user, problem))
                .collect(Collectors.toList());

        problemScoreRepository.saveAll(problemScores);
    }

    private ProblemScore createProblemScores(User user, Problem problem) {
        ProblemScore problemScore = new ProblemScore();
        problemScore.setScore("0.00");
        problemScore.setUser(user);
        problemScore.setProblem(problem);

        return problemScore;
    }

    private QuizScore createQuizScores(User user, Quiz quiz) {
        QuizScore quizScore = new QuizScore();
        quizScore.setScore("0.00");
        quizScore.setUser(user);
        quizScore.setQuiz(quiz);

        return quizScore;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }
}
