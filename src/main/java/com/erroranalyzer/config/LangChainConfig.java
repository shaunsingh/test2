package com.erroranalyzer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ConfigurationProperties(prefix = "langchain4j.open-ai.chat-model")
public class LangChainConfig {
    private static final Logger logger = LoggerFactory.getLogger(LangChainConfig.class);
    private final String apiKey;
    private final String modelName;
    private final double temperature;
    private final boolean logRequests;
    private final boolean logResponses;

    @ConstructorBinding
    public LangChainConfig(String apiKey, String modelName, double temperature, boolean logRequests, boolean logResponses) {
        logger.debug("Creating LangChainConfig with apiKey: {}, modelName: {}, temperature: {}, logRequests: {}, logResponses: {}", 
            apiKey != null ? "REDACTED" : "null", modelName, temperature, logRequests, logResponses);
        this.apiKey = apiKey;
        this.modelName = modelName;
        this.temperature = temperature;
        this.logRequests = logRequests;
        this.logResponses = logResponses;
    }

    public String getApiKey() {
        logger.debug("Getting API key: {}", apiKey != null ? "REDACTED" : "null");
        return apiKey;
    }

    public String getModelName() {
        return modelName;
    }

    public double getTemperature() {
        return temperature;
    }

    public boolean isLogRequests() {
        return logRequests;
    }

    public boolean isLogResponses() {
        return logResponses;
    }
} 