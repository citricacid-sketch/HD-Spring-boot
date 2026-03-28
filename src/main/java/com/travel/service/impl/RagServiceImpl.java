package com.travel.service.impl;

import com.travel.dto.RagProcessResponse;
import com.travel.service.RagService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Primary
public class RagServiceImpl implements RagService {

    private final ChatClient.Builder chatClientBuilder;

    @Autowired
    public RagServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClientBuilder = chatClientBuilder;
    }

    private ChatClient getChatClient() {
        return chatClientBuilder.build();
    }

    @Override
    public RagProcessResponse processPrompt(String prompt) {
        try {
            // Step 1: Generate enhanced prompt using AI
            String enhancedPrompt = generateEnhancedPrompt(prompt);

            // Step 2: Generate refinement questions
            List<String> refinedQuestions = generateRefinementQuestions(prompt);

            // Step 3: Calculate confidence score (simplified)
            double confidenceScore = calculateConfidenceScore(prompt);

            // Step 4: Find relevant context (simulated for now)
            List<String> relevantContext = findRelevantContext(prompt);

            return new RagProcessResponse(enhancedPrompt, refinedQuestions, confidenceScore, relevantContext);

        } catch (Exception e) {
            // Fallback to mock response if AI service fails
            return getFallbackResponse(prompt);
        }
    }

    @Override
    public List<String> getSuggestions(String partialPrompt) {
        try {
            if (partialPrompt == null || partialPrompt.trim().isEmpty()) {
                return Arrays.asList(
                    "可以补充出行天数",
                    "可以说明预算范围",
                    "可以描述旅行偏好",
                    "可以指定目的地"
                );
            }

            // Use AI to generate suggestions based on partial prompt
            String promptTemplate = """
                用户正在输入旅行需求："{partialPrompt}"
                请提供3-5个补全建议，帮助用户完善他们的旅行需求描述。
                每个建议应该简短明了，以中文提供。
                返回格式：每行一个建议，不要编号。
                """;

            PromptTemplate template = new PromptTemplate(promptTemplate);
            Map<String, Object> model = Map.of("partialPrompt", partialPrompt);
            Prompt prompt = template.create(model);

            String response = getChatClient().prompt(prompt).call().content();

            // Parse response lines
            List<String> suggestions = new ArrayList<>();
            String[] lines = response.split("\\n");
            for (String line : lines) {
                String trimmed = line.trim();
                if (!trimmed.isEmpty() && !trimmed.startsWith("```")) {
                    suggestions.add(trimmed);
                }
                if (suggestions.size() >= 5) {
                    break;
                }
            }

            // Fallback if no suggestions generated
            if (suggestions.isEmpty()) {
                suggestions.add("可以补充出行天数");
                suggestions.add("可以说明预算范围");
                suggestions.add("可以描述旅行偏好");
            }

            return suggestions;

        } catch (Exception e) {
            // Fallback suggestions
            return Arrays.asList(
                "可以补充出行天数",
                "可以说明预算范围",
                "可以描述旅行偏好",
                "可以指定目的地"
            );
        }
    }

    private String generateEnhancedPrompt(String originalPrompt) {
        String promptTemplate = """
            用户输入了一个旅行需求："{originalPrompt}"

            请根据以下要求增强这个提示词：
            1. 完善语法和表达，使其更清晰
            2. 补充旅行相关的常见要素（如预算、住宿偏好、交通方式等）
            3. 保持原意的完整性
            4. 使用中文回复，只返回增强后的提示词，不要额外解释

            增强后的提示词：
            """;

        PromptTemplate template = new PromptTemplate(promptTemplate);
        Map<String, Object> model = Map.of("originalPrompt", originalPrompt);
        Prompt prompt = template.create(model);

        return getChatClient().prompt(prompt).call().content().trim();
    }

    private List<String> generateRefinementQuestions(String prompt) {
        String promptTemplate = """
            用户输入了旅行需求："{prompt}"

            请生成3-4个细化问题，帮助用户提供更多细节以便规划更好的行程。
            问题应该具体、相关，并且以中文提问。
            返回格式：每行一个问题，不要编号。

            细化问题：
            """;

        PromptTemplate template = new PromptTemplate(promptTemplate);
        Map<String, Object> model = Map.of("prompt", prompt);
        Prompt aiPrompt = template.create(model);

        String response = getChatClient().prompt(aiPrompt).call().content();

        // Parse questions from response
        List<String> questions = new ArrayList<>();
        String[] lines = response.split("\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() &&
                !trimmed.startsWith("```") &&
                (trimmed.endsWith("？") || trimmed.endsWith("?"))) {
                questions.add(trimmed);
            }
            if (questions.size() >= 4) {
                break;
            }
        }

        // Fallback questions
        if (questions.isEmpty()) {
            questions.add("请问出行天数是多少？");
            questions.add("您的预算是多少？");
            questions.add("同行是否有老人或儿童？");
            questions.add("您对住宿有什么要求？");
        }

        return questions;
    }

    private double calculateConfidenceScore(String prompt) {
        // Simplified confidence calculation
        // In a real RAG system, this would be based on retrieval confidence and prompt completeness

        double score = 0.0;

        // Length factor
        int length = prompt.length();
        if (length > 50) {
            score += 0.3;
        } else if (length > 20) {
            score += 0.2;
        } else {
            score += 0.1;
        }

        // Keyword factor (simple check)
        String lowerPrompt = prompt.toLowerCase();
        String[] keywords = {"北京", "上海", "西安", "成都", "杭州", "三天", "五天", "预算", "老人", "儿童"};
        int keywordCount = 0;
        for (String keyword : keywords) {
            if (lowerPrompt.contains(keyword.toLowerCase())) {
                keywordCount++;
            }
        }
        score += keywordCount * 0.1;

        // Cap at 0.9
        return Math.min(0.9, Math.max(0.1, score));
    }

    private List<String> findRelevantContext(String prompt) {
        // Simplified context retrieval
        // In a real RAG system, this would query a vector database

        List<String> context = new ArrayList<>();
        String lowerPrompt = prompt.toLowerCase();

        // Mock travel knowledge base
        Map<String, String> knowledge = Map.of(
            "北京", "北京故宫需要提前预约门票，建议游玩3-4小时",
            "上海", "上海外滩夜景最佳观赏时间为晚上7-9点",
            "西安", "西安兵马俑建议请导游讲解，游玩时间2-3小时",
            "成都", "成都宽窄巷子有很多特色小吃，适合傍晚游览",
            "杭州", "杭州西湖春季和秋季是最佳游览季节",
            "三天", "三天行程适合城市游览，建议安排主要景点",
            "五天", "五天行程可以进行深度游，适合多个城市或区域",
            "预算", "旅行预算应包含交通、住宿、餐饮和门票费用"
        );

        for (Map.Entry<String, String> entry : knowledge.entrySet()) {
            if (lowerPrompt.contains(entry.getKey().toLowerCase())) {
                context.add(entry.getValue());
                if (context.size() >= 3) {
                    break;
                }
            }
        }

        return context;
    }

    private RagProcessResponse getFallbackResponse(String prompt) {
        // Fallback to mock-like response when AI service fails
        String enhancedPrompt = prompt + "\n\n请确认以上信息准确，或补充更多细节以便我为您优化行程。";

        List<String> questions = Arrays.asList(
            "请问出行天数是多少？",
            "您的预算是多少？",
            "同行是否有老人或儿童？",
            "您对住宿有什么要求？"
        );

        double confidenceScore = 0.3;
        List<String> context = findRelevantContext(prompt);

        return new RagProcessResponse(enhancedPrompt, questions, confidenceScore, context);
    }
}
