package org.autotestdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.autotestdemo.dto.SuggestionRequestDTO;
import org.autotestdemo.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/api")
public class suggestion {
    @Autowired
    SuggestionService suggestionService;
    //建议生成

    /**
     * 获取测试脚本优化建议
     * @param request 请求体，包含API文档、接口依赖、测试脚本等参数
     * @return 包含AI生成建议的响应体
     */
    @PostMapping("/generateSuggestion")
    public ResponseEntity<Map<String, Object>> generateSuggestion(@RequestBody SuggestionRequestDTO request) throws JsonProcessingException {
        // 响应结果封装
        Map<String, Object> response = new HashMap<>();

            // 调用服务层方法，获取AI生成的建议
            String suggestion = suggestionService.getSuggestion(
                    request.getApi(),
                    request.getEnvironment(),
                    request.getDependency(),
                    request.getTest(),
                    request.getTestResult()
            );

            // 封装成功响应
            response.put("code", 200);
            response.put("message", "获取测试脚本优化建议成功");
            response.put("data", suggestion); // AI返回的JSON格式建议字符串
            return new ResponseEntity<>(response, HttpStatus.OK);


    }


}
