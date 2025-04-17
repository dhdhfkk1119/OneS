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
}
