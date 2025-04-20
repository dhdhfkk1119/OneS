package com.example.ones.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MessageSearchDTO {
    private Long memberIdx;
    private String userId;
    private String userName;
    private String userImage;
    private String messageContent;
    private String userStatus; // ✅ 로그인 상태 추가
}
