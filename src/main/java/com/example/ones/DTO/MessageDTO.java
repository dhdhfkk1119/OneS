package com.example.ones.DTO;

import com.example.ones.Entity.Message;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long senderIdx;
    private Long receiverIdx;
    private String content;
    private String imagesContent;
    private LocalDateTime sendAt;

}
