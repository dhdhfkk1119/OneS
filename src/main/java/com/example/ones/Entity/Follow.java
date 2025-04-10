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
public class Follow {
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY)
    private Long followIdx;

    @Column(nullable = false)
    private Long followLoginidx;

    @Column(nullable = false)
    private Long followUseridx;

    @Column(nullable = false)
    private LocalDateTime followAt;
}
