package com.cs.quiz.service;

import com.cs.quiz.dto.UserDto;
import com.cs.quiz.entity.User;

import java.util.List;

public interface JDoodleAPIService {
    public String executeCode(String code, String stdin, String language, String versionIndex);
}