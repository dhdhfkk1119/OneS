package com.example.ones.Repository;

import com.example.ones.Entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.senderIdx = :senderIdx AND m.receiverIdx = :receiverIdx) OR " +
            "(m.senderIdx = :receiverIdx AND m.receiverIdx = :senderIdx) " +
            "ORDER BY m.sendAt ASC")
    List<Message> findMessagesBetweenUsers(@Param("senderIdx") Long senderIdx, @Param("receiverIdx") Long receiverIdx);

    @Query("SELECT m FROM Message m WHERE m.senderIdx = :senderIdx AND m.receiverIdx = :receiverIdx AND m.isRead = false")
    List<Message> findUnreadMessages(@Param("senderIdx") Long senderIdx, @Param("receiverIdx") Long receiverIdx);

    List<Message> findBySenderIdx(Long senderIdx);
    List<Message> findByReceiverIdx(Long receiverIdx);

    Optional<Message> findTopBySenderIdxAndReceiverIdxOrderBySendAtDesc(Long senderIdx, Long receiverIdx);

    List<Message> findBySenderIdxAndReceiverIdx(Long senderIdx, Long receiverIdx);



}
