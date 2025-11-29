package org.autotestdemo.service.impl;

import org.autotestdemo.llm.LLMService;
import org.autotestdemo.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuggestionImpl implements SuggestionService {
    @Autowired
    private LLMService llmService;
    // 封装为方法，统一处理Prompt构建
    /**
     * 构建标准化、高性能的测试脚本建议生成 Prompt
     * 核心优化：精简冗余描述、强化JSON格式约束、控制长度、统一转义规则
     */
    private String buildStandardPrompt(String api, String environment, String dependency, String test, String testResult) {
        // 1. 基础参数转义（避免破坏JSON结构，同时控制长度）
        String escapedApi = escapeAndTruncate(api, 2000);
        String escapedEnv = escapeAndTruncate(environment, 1000);
        String escapedDep = escapeAndTruncate(dependency, 1000);
        String escapedTest = escapeAndTruncate(test, 3000);
        String escapedResult = escapeAndTruncate(testResult, 1000);

        // 2. 构建Prompt（结构化、无冗余、强约束）
        StringBuilder promptBuilder = new StringBuilder(8000); // 预设容量，减少扩容
        promptBuilder
                // 角色定义（极简，聚焦核心任务）
                .append("### 角色\n")
                .append("你是自动化测试专家，仅输出符合以下要求的JSON格式测试脚本优化建议，无任何多余文字、注释、缩进。\n\n")

                // 输入数据（结构化，标注数据类型）
                .append("### 输入数据\n")
                .append("1. API文档（JSON）：").append(escapedApi).append("\n")
                .append("2. 测试环境（文本）：").append(escapedEnv).append("\n")
                .append("3. 接口依赖（文本）：").append(escapedDep).append("\n")
                .append("4. 测试脚本（JSON）：").append(escapedTest).append("\n")
                .append("5. 执行结果（文本）：").append(escapedResult).append("\n\n")

                // 分析维度（精简描述，减少冗余）
                .append("### 分析维度\n")
                .append("- 逻辑：API规范贴合度、依赖参数传递、环境适配性\n")
                .append("- 语法：JSON格式、参数类型、逻辑冲突\n")
                .append("- 场景：正向/异常/边界覆盖、幂等性、重试机制\n\n")

                // 输出要求（强化JSON约束，移除冗余示例）
                .append("### 输出要求\n")
                .append("1. 层级：high（必须修）/medium（建议优化）/low（体验提升）\n")
                .append("2. 每个建议包含：id、priority、dimension、problem_desc、modify_operation、modify_example、value\n")
                .append("3. 汇总包含：total_suggestions（整数）、high_priority_count（整数）、core_problem（一句话）\n")
                .append("4. 输出示例（仅参考格式，无缩进）：\n")
                .append("{\"suggestions\":[{\"id\":\"S001\",\"priority\":\"high\",\"dimension\":\"逻辑\",\"problem_desc\":\"\",\"modify_operation\":\"\",\"modify_example\":\"\",\"value\":\"\"}],\"summary\":{\"total_suggestions\":1,\"high_priority_count\":1,\"core_problem\":\"\"}}\n\n")

                // 禁止项（极简，聚焦核心约束）
                .append("### 禁止项\n")
                .append("- 不输出JSON外的任何内容（包括说明、注释、缩进）\n")
                .append("- 不夸大优化效果，基于输入客观分析\n")
                .append("- 不遗漏执行结果中的失败节点问题");

        // 最终长度校验（避免超出大模型上下文限制）
        String finalPrompt = promptBuilder.toString();
//        if (finalPrompt.length() > 8000) {
//            logger.warn("Prompt长度超限（{}字符），已截断", finalPrompt.length());
//            finalPrompt = finalPrompt.substring(0, 8000) + "...[内容截断，优先分析核心问题]";
//        }
        return finalPrompt;
    }

    /**
     * 转义特殊字符 + 长度截断（避免参数过大导致400）
     * @param content 原始内容
     * @param maxLen 最大长度
     * @return 转义并截断后的内容
     */
    private String escapeAndTruncate(String content, int maxLen) {
        if (content == null || content.isEmpty()) {
            return "";
        }
        // 转义核心特殊字符（避免破坏JSON结构）
        String escaped = content
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", " ") // 换行符替换为空格，减少长度
                .replace("\r", "")
                .replace("\t", " ");
        // 长度截断（保留末尾标识）
        if (escaped.length() > maxLen) {
            return escaped.substring(0, maxLen - 3) + "...";
        }
        return escaped;
    }
    @Override
    public String getSuggestion(String api, String environment, String dependency, String test, String testResult) {

        String response = llmService.getMessage(buildStandardPrompt(api,environment,dependency,test,testResult));
        return response;

    }
}
