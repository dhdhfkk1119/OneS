package com.example.ones.Controller;

import com.example.ones.DTO.MessageDTO;
import com.example.ones.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    // app/chat/send로 전송된 메시지를 처리
    @MessageMapping("/chat/send")
    @SendTo("/topic/messages")
    public MessageDTO sendMessage(MessageDTO messageDTO) {

        System.out.println( "이미지 내용 ChatController " +messageDTO.getImagesContent());

        if (messageDTO.getImagesContent() != null && !messageDTO.getImagesContent().isBlank()) {
            String[] imageList = messageDTO.getImagesContent().split(",");
            for (String url : imageList) {
                System.out.println("📷 이미지 URL: " + url);
            }
        }

        messageService.saveMessage(messageDTO);

        return messageDTO;
    }
}
