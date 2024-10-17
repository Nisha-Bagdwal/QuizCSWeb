package com.cs.quiz.service;

public interface OnlineCompilerAPIService {
    public String executeCode(String code, String stdin, String language);
}