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
public class Board {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long boardIdx;

    @Column(nullable = false)
    private String boardContent;

    @Column(nullable = false)
    private Long boardUseridx;

    @Column(nullable = false)
    private Long boardLike;

    @Column(nullable = false)
    private Long boardComent;

    private String boardImages;

    @Column(nullable = false)
    private LocalDateTime boardAt = LocalDateTime.now();
}
