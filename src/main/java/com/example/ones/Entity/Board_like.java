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
public class Board_like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeIdx;

    @Column(nullable = false)
    private Long likeBoardidx;

    @Column(nullable = false)
    private Long likeUseridx;

    @Column(nullable = false)
    private LocalDateTime likeAt;
}
