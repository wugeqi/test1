package org.autotestdemo.dto;

public  class SuggestionRequestDTO {
    // API文档内容
    private String api;
    // 测试环境信息（允许为空，适配无测试环境场景）
    private String environment;
    // 接口依赖关系
    private String dependency;
    // 测试脚本内容
    private String test;
    // 测试执行结果
    private String testResult;

    // 无参构造器（JSON反序列化需要）
    public SuggestionRequestDTO() {}

    // Getter & Setter
    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String dependency) {
        this.dependency = dependency;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}