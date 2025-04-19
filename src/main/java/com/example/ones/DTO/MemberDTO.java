package com.example.ones.DTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long idx;

    @NotEmpty(message = "아이디를 입력해주세요")
    private String userId;

    @NotEmpty(message = "이름을 입력해주세요")
    private String userName;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String userPassword;

    @NotEmpty(message = "재 비밀번호를 입력해주세요")
    private String userRePassword;

    @NotNull(message = "나이를 입력해주세요")
    private Long userAge;

    @NotEmpty(message = "전화번호를 입력해주세요")
    private String userPhoneNumber;

    @NotEmpty(message = "우편번호를 입력해주세요")
    private String address; //우편번호
    @NotEmpty(message = "기본 주소를 입력해주세요")
    private String addressDefault; // 기본주소
    @NotEmpty(message = "상세 주소를 입력해주세요")
    private String addressDetail; // 상세주소 동호수

    @NotEmpty(message = "주소를 입력해주세요")
    public String getUserAddress() {
        return address + "/" +  addressDefault + "/" + addressDetail;
    }

    @NotEmpty(message = "이메일을 입력해주세요")
    private String userEmail;

    @Column(nullable = false)
    private LocalDateTime userAt = LocalDateTime.now();

    @Column(nullable = false)
    private String userImage = "basic.png";

    @Column(nullable = false)
    private Long userPoint = 200L;

    @Column(nullable = false)
    private String userAction = "In-progress";

    @Column(nullable = false)
    private String userStatus = "disconnect";

    @Column(nullable = false)
    private Long follow;

    @Column(nullable = false)
    private Long following;

    private String introduce;

}
