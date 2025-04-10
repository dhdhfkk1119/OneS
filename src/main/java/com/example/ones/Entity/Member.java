package com.example.ones.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    private Long userAge;

    @Column(nullable = false)
    private String userPhoneNumber;

    @Column(nullable = false)
    private String userAddress;

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private LocalDateTime userAt = LocalDateTime.now();

    @Column(nullable = false)
    private String userImage;

    @Column(nullable = false)
    private Long userPoint;

    @Column(nullable = false)
    private String userAction;

    @Column(nullable = false)
    private String userStatus;

    @Column(nullable = false)
    private Long follow;

    @Column(nullable = false)
    private Long following;
}
