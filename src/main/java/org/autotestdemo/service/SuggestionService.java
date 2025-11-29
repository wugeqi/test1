package org.autotestdemo.service;

import org.autotestdemo.llm.LLMService;
import org.springframework.stereotype.Service;

public interface SuggestionService {
    //拼接提示词，调用ai
    public String getSuggestion(String api, String environment, String dependency, String test, String testResult) ;
}
