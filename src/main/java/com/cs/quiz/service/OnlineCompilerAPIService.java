package com.cs.quiz.service;

import java.util.concurrent.CompletableFuture;

public interface OnlineCompilerAPIService {
    CompletableFuture<String> executeCode(String code, String stdin, String language);
}