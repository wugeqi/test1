package org.autotestdemo.llm;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface LLMService {
    String getMessage(String query) throws JsonProcessingException;
}
