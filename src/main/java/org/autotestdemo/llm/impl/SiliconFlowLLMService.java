package org.autotestdemo.llm.impl;

import org.autotestdemo.llm.LLMService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class SiliconFlowLLMService implements LLMService {
    private static final Logger logger = LoggerFactory.getLogger(SiliconFlowLLMService.class);

    private final RestTemplate restTemplate;

    @Value("${llm.api.url}")
    private String apiUrl;

    @Value("${llm.api.token}")
    private String apiToken;


    @Value("${llm.api.model}")
    private String model;

    public SiliconFlowLLMService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public String getMessage(String query) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiToken);

        // 直接构建 JSON 字符串
        String requestBody = String.format(
                "{\"model\": \"%s\", \"messages\": [{\"role\": \"user\", \"content\": \"%s\"}], " +
                        "\"max_tokens\": 512, \"temperature\": 0.7}",
                model,
                query.replace("\"", "\\\"")  // 转义双引号
        );

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        logger.debug("发送请求到 SiliconFlow API: {}", requestBody);

        String jsonResponse = restTemplate.postForObject(apiUrl, requestEntity, String.class);
        logger.debug("收到 API 响应");

        return extractTextFromJsonResponse(jsonResponse);

    }
    /**
     * 从 JSON 响应中提取文字内容（使用 Spring 的 JsonParser）
     */
    private String extractTextFromJsonResponse(String jsonResponse) {
            // 使用 Spring 的 JsonParser
            org.springframework.boot.json.JsonParser parser =
                    org.springframework.boot.json.JsonParserFactory.getJsonParser();
            Map<String, Object> responseMap = parser.parseMap(jsonResponse);

            // 提取 choices 数组
            List<Object> choices = (List<Object>) responseMap.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> firstChoice = (Map<String, Object>) choices.get(0);
                Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                if (message != null) {
                    String content = (String) message.get("content");
                    if (content != null) {
                        return content;
                    }
                }
            }

            // 尝试直接获取 content
            String content = (String) responseMap.get("content");
            if (content != null) {
                return content;
            }

            logger.warn("无法从响应中提取内容，返回原始响应");
            return jsonResponse;


    }


}