package com.example.ones.Service;

import com.example.ones.DTO.MessageDTO;
import com.example.ones.Entity.Message;
import com.example.ones.Repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public void saveMessage(MessageDTO dto) {
        Message message = new Message();
        message.setSenderIdx(dto.getSenderIdx());
        message.setReceiverIdx(dto.getReceiverIdx());
        message.setContent(dto.getContentIdx());
        message.setImagesContent(dto.getImagesContent());
        message.setSendAt(LocalDateTime.now());
        message.setRead(false); // 처음엔 읽지 않은 상태
        messageRepository.save(message);
    }

    // 메시지 조회
    public List<MessageDTO> getConversation(Long user1, Long user2) {
        return messageRepository.findConversation(user1, user2).stream().map(message -> {
            MessageDTO dto = new MessageDTO();
            dto.setSenderIdx(message.getSenderIdx());
            dto.setReceiverIdx(message.getReceiverIdx());
            dto.setContentIdx(message.getContent());
            dto.setImagesContent(message.getImagesContent());
            return dto;
        }).collect(Collectors.toList());
    }

}
