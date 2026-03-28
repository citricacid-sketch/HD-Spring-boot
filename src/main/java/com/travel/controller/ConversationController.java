package com.travel.controller;

import com.travel.dto.ApiResponse;
import com.travel.entity.Conversation;
import com.travel.entity.Message;
import com.travel.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    @Autowired
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    // 创建新会话
    @PostMapping
    public ApiResponse<Conversation> createConversation(@RequestBody CreateConversationRequest request) {
        try {
            Conversation conversation = conversationService.createConversation(
                request.getSessionId(), 
                request.getTitle()
            );
            return ApiResponse.success(conversation);
        } catch (Exception e) {
            return ApiResponse.error("Failed to create conversation: " + e.getMessage());
        }
    }

    // 获取会话历史
    @GetMapping("/history/{sessionId}")
    public ApiResponse<List<Conversation>> getConversationHistory(@PathVariable String sessionId) {
        try {
            List<Conversation> conversations = conversationService.getConversationHistory(sessionId);
            return ApiResponse.success(conversations);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get conversation history: " + e.getMessage());
        }
    }

    // 获取会话的所有消息
    @GetMapping("/{conversationId}/messages")
    public ApiResponse<List<Message>> getConversationMessages(@PathVariable Long conversationId) {
        try {
            Conversation conversation = conversationService.getOrCreateConversation(
                conversationId.toString(), 
                "对话"
            );
            List<Message> messages = conversationService.getConversationMessages(conversation.getSessionId());
            return ApiResponse.success(messages);
        } catch (Exception e) {
            return ApiResponse.error("Failed to get messages: " + e.getMessage());
        }
    }

    // 删除会话
    @DeleteMapping("/{conversationId}")
    public ApiResponse<Void> deleteConversation(@PathVariable Long conversationId) {
        try {
            conversationService.deleteConversation(conversationId);
            return ApiResponse.success(null);
        } catch (Exception e) {
            return ApiResponse.error("Failed to delete conversation: " + e.getMessage());
        }
    }

    // 更新会话标题
    @PutMapping("/{conversationId}/title")
    public ApiResponse<Conversation> updateConversationTitle(
        @PathVariable Long conversationId,
        @RequestBody UpdateTitleRequest request
    ) {
        try {
            Conversation conversation = conversationService.updateConversationTitle(
                conversationId, 
                request.getTitle()
            );
            return ApiResponse.success(conversation);
        } catch (Exception e) {
            return ApiResponse.error("Failed to update title: " + e.getMessage());
        }
    }

    // 请求DTO类
    public static class CreateConversationRequest {
        private String sessionId;
        private String title;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class UpdateTitleRequest {
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
