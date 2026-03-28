package com.travel.controller;

import com.travel.dto.ApiResponse;
import com.travel.dto.RagProcessRequest;
import com.travel.dto.RagProcessResponse;
import com.travel.dto.RagSuggestRequest;
import com.travel.entity.Message;
import com.travel.service.RagService;
import com.travel.service.ConversationService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;
    private final ChatClient.Builder chatClientBuilder;
    private final ConversationService conversationService;

    @Autowired
    public RagController(RagService ragService, ChatClient.Builder chatClientBuilder, 
                        ConversationService conversationService) {
        this.ragService = ragService;
        this.chatClientBuilder = chatClientBuilder;
        this.conversationService = conversationService;
    }

    private ChatClient getChatClient() {
        return chatClientBuilder.build();
    }

    @PostMapping(value = "/process", produces = "application/json; charset=UTF-8")
    public ApiResponse<RagProcessResponse> processPrompt(@RequestBody RagProcessRequest request) {
        try {
            RagProcessResponse response = ragService.processPrompt(request.getPrompt());
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("Failed to process prompt: " + e.getMessage());
        }
    }

    @PostMapping(value = "/suggest", produces = "application/json; charset=UTF-8")
    public ApiResponse<RagSuggestResponse> suggest(@RequestBody RagSuggestRequest request) {
        try {
            List<String> suggestions = ragService.getSuggestions(request.getPartialPrompt());
            RagSuggestResponse response = new RagSuggestResponse(suggestions);
            return ApiResponse.success(response);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get suggestions: " + e.getMessage());
        }
    }

    @PostMapping(value = "/chat", produces = "application/json; charset=UTF-8")
    public ApiResponse<String> chat(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            String sessionId = request.get("sessionId");

            if (message == null || message.trim().isEmpty()) {
                return ApiResponse.error("Message cannot be empty");
            }

            if (sessionId == null || sessionId.trim().isEmpty()) {
                sessionId = java.util.UUID.randomUUID().toString();
            }

            System.out.println("Received message from session " + sessionId + ": " + message);

            // 获取会话历史
            List<Message> history = conversationService.getConversationMessages(sessionId);

            // 构建对话上下文 - 只保留最近10条消息
            StringBuilder conversationContext = new StringBuilder();
            int maxHistorySize = Math.min(history.size(), 10);
            for (int i = history.size() - maxHistorySize; i < history.size(); i++) {
                Message msg = history.get(i);
                if ("user".equals(msg.getRole())) {
                    conversationContext.append("用户: ").append(msg.getContent()).append("\n");
                } else if ("assistant".equals(msg.getRole())) {
                    conversationContext.append("助手: ").append(msg.getContent()).append("\n");
                }
            }

            // 添加当前用户消息
            conversationContext.append("用户: ").append(message).append("\n");

            // 直接调用DeepSeek API进行对话
            String systemPrompt = "你是一个专业的旅行规划助手，能够根据用户的需求提供详细的旅行建议和行程规划。" +
                "请始终以表格形式返回行程规划，表格应包含以下列：日期、时间、活动、地点、备注。" +
                "表格使用Markdown格式，确保表格清晰易读。" +
                "请用友好、专业的语气回答用户的问题。" +
                "以下是最近的对话历史，请参考这些历史来提供更个性化的回答：\n" + conversationContext.toString();

            System.out.println("Sending prompt to DeepSeek API...");

            String aiResponse = getChatClient().prompt()
                .system(systemPrompt)
                .user(message)
                .call()
                .content();

            System.out.println("Received response from DeepSeek: " + aiResponse);

            // 保存用户消息和AI响应到数据库
            conversationService.addMessage(sessionId, "user", message);
            conversationService.addMessage(sessionId, "assistant", aiResponse);

            return ApiResponse.success(aiResponse);
        } catch (Exception e) {
            System.err.println("Error calling DeepSeek API: " + e.getMessage());
            e.printStackTrace();
            return ApiResponse.error("Failed to process chat message: " + e.getMessage());
        }
    }

    // Inner class for suggest response
    public static class RagSuggestResponse {
        private List<String> suggestions;

        public RagSuggestResponse(List<String> suggestions) {
            this.suggestions = suggestions;
        }

        public List<String> getSuggestions() {
            return suggestions;
        }

        public void setSuggestions(List<String> suggestions) {
            this.suggestions = suggestions;
        }
    }
}