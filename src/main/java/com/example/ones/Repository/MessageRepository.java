package com.example.ones.Repository;

import com.example.ones.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE " +
            "(m.senderIdx = :user1 AND m.receiverIdx = :user2) OR " +
            "(m.senderIdx = :user2 AND m.receiverIdx = :user1) " +
            "ORDER BY m.sendAt")
    List<Message> findConversation(Long user1, Long user2);

}
