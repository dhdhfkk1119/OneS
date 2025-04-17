package com.example.ones.Controller;

import com.example.ones.DTO.MessageDTO;
import com.example.ones.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketMessageController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/message")
    public void Sendmessage(MessageDTO messageDTO) {
        // DB 저장
        messageService.saveMessage(messageDTO);

        // 수신자에게 메시지 전송
        messagingTemplate.convertAndSendToUser(
                messageDTO.getReceiverIdx().toString(),
                "/queue/messages",
                messageDTO
        );
    }

}
