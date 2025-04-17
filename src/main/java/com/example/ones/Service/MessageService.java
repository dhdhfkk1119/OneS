package com.example.ones.Service;


import com.example.ones.DTO.MessageDTO;
import com.example.ones.Entity.Message;
import com.example.ones.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public void saveMessage(MessageDTO dto) {

        Message message = new Message();


        System.out.println("💬 저장된 메시지 내용: " + message.getContent());
        message.setSenderIdx(dto.getSenderIdx());
        message.setReceiverIdx(dto.getReceiverIdx());
        message.setContent(dto.getContent());
        message.setImagesContent(dto.getImagesContent());
        message.setSendAt(LocalDateTime.now());
        message.setRead(false);

        messageRepository.save(message);
    }


}
