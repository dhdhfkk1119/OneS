package com.example.ones.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentIdx;

    @Column(nullable = false)
    private String commentContent;

    @Column(nullable = false)
    private String commentImages;

    @Column(nullable = false)
    private LocalDateTime commentAt;

    @Column(nullable = false)
    private Long commentUseridx;

    @Column(nullable = false)
    private Long commentBoardidx;

    @Column(nullable = false)
    private Long commentLike;

    @Column(nullable = false)
    private Long commentView;

    @Column(nullable = false)
    private Long commentRelay;

}
