package com.cs.quiz.service;

import com.cs.quiz.dto.UserDto;
import com.cs.quiz.entity.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}