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

    List<Message> findBySenderIdxAndReceiverIdxOrderBySendAtDesc(Long senderIdx, Long receiverIdx);


    // 현재 로그인한 유저에게 보낸 메세지를 읽었는지 체크한다
    List<Message> findBySenderIdxAndReceiverIdxAndIsReadFalse(Long senderIdx, Long receiverIdx); // 상대방이 보낸 메세지를 읽 지않았으면

    List<Message> findBySenderIdxOrReceiverIdx(Long senderIdx, Long receiverIdx);

    @Query(value = """
    SELECT 
        CASE 
            WHEN m.sender_idx = :userIdx THEN m.receiver_idx
            ELSE m.sender_idx 
        END AS other_user_idx,
        m.content,
        m.send_at,
        mem.user_name
    FROM message m
    JOIN member mem ON mem.idx = CASE 
        WHEN m.sender_idx = :userIdx THEN m.receiver_idx
        ELSE m.sender_idx 
    END
    INNER JOIN (
        SELECT 
            CASE 
                WHEN sender_idx = :userIdx THEN receiver_idx
                ELSE sender_idx 
            END AS other_user_idx,
            MAX(send_at) AS latest_send_at
        FROM message
        WHERE sender_idx = :userIdx OR receiver_idx = :userIdx
        GROUP BY other_user_idx
    ) latest_msg
    ON (
        (
            (m.sender_idx = :userIdx AND m.receiver_idx = latest_msg.other_user_idx) OR
            (m.receiver_idx = :userIdx AND m.sender_idx = latest_msg.other_user_idx)
        )
        AND m.send_at = latest_msg.latest_send_at
    )
    WHERE mem.user_name LIKE %:keyword%
       OR m.content LIKE %:keyword%
    ORDER BY m.send_at DESC
""", nativeQuery = true)
    List<Object[]> findRecentMessagesWithKeyword(@Param("userIdx") Long userIdx, @Param("keyword") String keyword);



}
