package com.travel.repository;

import com.travel.entity.Conversation;
import com.travel.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // 根据会话查找所有消息，按顺序排序
    List<Message> findByConversationOrderBySequenceAsc(Conversation conversation);

    // 根据会话ID查找所有消息，按顺序排序
    List<Message> findByConversationSessionIdOrderBySequenceAsc(String sessionId);

    // 根据会话删除所有消息
    void deleteByConversation(Conversation conversation);
}
