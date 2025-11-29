package org.autotestdemo;

import org.autotestdemo.service.SuggestionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@SpringBootApplication
public class application {

    public static void main(String[] args) {
        SpringApplication.run(application.class, args);
    }
    private String readJsonFile(String filePath) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            try (InputStream inputStream = resource.getInputStream()) {
                byte[] bytes = FileCopyUtils.copyToByteArray(inputStream);
                return new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    @Bean
    public CommandLineRunner run(SuggestionService suggestionService) {
        return args -> {
            // 从 resources 目录读取 JSON 文件
//            String apiDoc = readJsonFile("data/api.json");
//            String environment = readJsonFile("data/environment.json");
//            String dependency = readJsonFile("data/dependency.json");
//            String testScript = readJsonFile("data/test.json");       // 注意：您提供的文件是test.json，而不是test-script.json
//            String testResult = readJsonFile("data/testresult.json"); // 注意：您提供的文件是testresult.json，而不是test-result.json
            String apiDoc="";
            String environment="";
            String dependency="";
            String testScript="";
            String testResult="";
            String response = suggestionService.getSuggestion(apiDoc, environment, dependency, testScript, testResult);
            System.out.println("=== 测试建议分析结果 ===");
            System.out.println(response);
        };
    }
}