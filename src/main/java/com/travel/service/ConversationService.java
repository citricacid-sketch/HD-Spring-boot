package com.travel.service;

import com.travel.entity.Conversation;
import com.travel.entity.Message;
import com.travel.repository.ConversationRepository;
import com.travel.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, 
                              MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    // 创建新会话
    @Transactional
    public Conversation createConversation(String sessionId, String title) {
        Conversation conversation = new Conversation(sessionId, title);
        return conversationRepository.save(conversation);
    }

    // 获取或创建会话
    @Transactional
    public Conversation getOrCreateConversation(String sessionId, String title) {
        return conversationRepository.findBySessionId(sessionId)
            .orElseGet(() -> createConversation(sessionId, title));
    }

    // 添加消息到会话
    @Transactional
    public Message addMessage(String sessionId, String role, String content) {
        Conversation conversation = getOrCreateConversation(sessionId, "新对话");

        // 获取当前消息数量作为序列号
        int sequence = conversation.getMessages().size();

        Message message = new Message(conversation, role, content, sequence);
        conversation.getMessages().add(message);

        // 更新会话时间
        conversation.setUpdatedAt(java.time.LocalDateTime.now());
        conversationRepository.save(conversation);

        return message;
    }

    // 获取会话的所有消息
    @Transactional(readOnly = true)
    public List<Message> getConversationMessages(String sessionId) {
        return messageRepository.findByConversationSessionIdOrderBySequenceAsc(sessionId);
    }

    // 获取会话历史
    @Transactional(readOnly = true)
    public List<Conversation> getConversationHistory(String sessionId) {
        return conversationRepository.findBySessionIdOrderByUpdatedAtDesc(sessionId);
    }

    // 删除会话
    @Transactional
    public void deleteConversation(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new RuntimeException("Conversation not found"));
        messageRepository.deleteByConversation(conversation);
        conversationRepository.delete(conversation);
    }

    // 更新会话标题
    @Transactional
    public Conversation updateConversationTitle(Long conversationId, String title) {
        Conversation conversation = conversationRepository.findById(conversationId)
            .orElseThrow(() -> new RuntimeException("Conversation not found"));
        conversation.setTitle(title);
        return conversationRepository.save(conversation);
    }
}
