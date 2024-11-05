package com.giyeon.chatbotforforeignstudentsincbnu.config;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatConfig {

    @Value("${spring.ai.openai.api-key}")
    private String secretKey;

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

}
