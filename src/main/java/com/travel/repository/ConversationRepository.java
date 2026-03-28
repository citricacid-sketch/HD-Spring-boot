package com.travel.repository;

import com.travel.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    // 根据会话ID查找会话
    Optional<Conversation> findBySessionId(String sessionId);

    // 查找用户的所有会话
    List<Conversation> findBySessionIdOrderByUpdatedAtDesc(String sessionId);

    // 检查会话是否存在
    boolean existsBySessionId(String sessionId);
}
