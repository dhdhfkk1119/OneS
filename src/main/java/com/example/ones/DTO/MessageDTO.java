package com.example.ones.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {
    private Long senderIdx;
    private Long receiverIdx;
    private String contentIdx;
    private String imagesContent;
}
