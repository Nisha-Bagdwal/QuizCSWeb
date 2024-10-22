package com.cs.quiz;

import com.cs.quiz.dto.UserDto;
import com.cs.quiz.entity.Role;
import com.cs.quiz.entity.User;
import com.cs.quiz.repository.*;
import com.cs.quiz.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @InjectMocks
    private UserServiceImpl userService;  // Inject the service we are testing

    @Mock
    private UserRepository userRepository;  // Mock the repository dependency

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private QuizScoreRepository quizScoreRepository;

    @Mock
    private ProblemScoreRepository problemScoreRepository;

    @Test
    public void testSaveUser() {

        UserDto userDto = new UserDto(1L, "John", "Doe", "john.doe@example.com", "password123");
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setTotalQuizScore("0.00");
        user.setTotalProblemScore("0.00");

        // Mocking the behavior of password encoder and role repository
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encryptedPassword");
        Role role = new Role(1L, "ROLE_USER", List.of(user));
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        // Test
        userService.saveUser(userDto);

        // Assert
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture()); // Capture the saved user

        User savedUser = userCaptor.getValue(); // Get the captured user

        // Verify properties of the saved user
        assertEquals("John Doe", savedUser.getName());
        assertEquals("john.doe@example.com", savedUser.getEmail());
        assertEquals("encryptedPassword", savedUser.getPassword());
        assertEquals(List.of(role), savedUser.getRoles());
        verify(quizScoreRepository, times(1)).saveAll(anyList());
        verify(problemScoreRepository, times(1)).saveAll(anyList());
    }

    @Test
    public void testFindUserByEmail() {
        // Arrange
        String email = "john.doe@example.com";
        User user = new User();
        user.setName("John Doe");
        user.setEmail(email);

        // Mock behavior of userRepository
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        User result = userService.findUserByEmail(email);

        // Assert
        assertEquals("John Doe", result.getName());
        assertEquals(email, result.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testFindAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");

        List<User> users = List.of(user1, user2);

        // Mock behavior of userRepository
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<UserDto> result = userService.findAllUsers();

        // Assert
        assertEquals(2, result.size());

        // Verify the first user
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("john.doe@example.com", result.get(0).getEmail());

        // Verify the second user
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Smith", result.get(1).getLastName());
        assertEquals("jane.smith@example.com", result.get(1).getEmail());

        verify(userRepository, times(1)).findAll();
    }
}
