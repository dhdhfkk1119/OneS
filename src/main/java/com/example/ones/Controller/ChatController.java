package com.example.ones.Controller;

import com.example.ones.DTO.MessageDTO;
import com.example.ones.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    // app/chat/send로 전송된 메시지를 처리
    @MessageMapping("/chat/send") // 철자 수정됨!
    @SendTo("/topic/messages")
    public MessageDTO sendMessage(MessageDTO messageDTO) {
        messageService.saveMessage(messageDTO);

        return messageDTO;
    }
}
