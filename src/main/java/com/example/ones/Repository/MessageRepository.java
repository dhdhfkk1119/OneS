package com.example.ones.Repository;

import com.example.ones.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    List<Message> findBySenderIdxAndReceiverIdxOrReceiverIdxAndSenderIdx(
            Long sender1, Long receiver1, Long sender2, Long receiver2);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.senderIdx = :senderIdx AND m.receiverIdx = :receiverIdx) OR " +
            "(m.senderIdx = :receiverIdx AND m.receiverIdx = :senderIdx) " +
            "ORDER BY m.sendAt ASC")
    List<Message> findMessagesBetweenUsers(@Param("senderIdx") Long senderIdx, @Param("receiverIdx") Long receiverIdx);

    @Query("SELECT m FROM Message m WHERE m.senderIdx = :senderIdx AND m.receiverIdx = :receiverIdx AND m.isRead = false")
    List<Message> findUnreadMessages(@Param("senderIdx") Long senderIdx, @Param("receiverIdx") Long receiverIdx);
}
