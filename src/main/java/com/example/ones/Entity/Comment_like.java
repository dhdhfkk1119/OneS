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
public class Comment_like {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long commentlikeIdx;

    @Column(nullable = false)
    private Long commentlikeUseridx;

    @Column(nullable = false)
    private Long commentlikeCommentidx;

    @Column(nullable = false)
    private LocalDateTime commentlikeAt;
}
