package com.example.ones.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageIdx;

    @Column(nullable = false)
    private Long senderIdx;

    @Column(nullable = false)
    private Long receiverIdx;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sendAt;

    @Column(nullable = false)
    private boolean read;

    @Column(nullable = false)
    private String imagesContent;
}
