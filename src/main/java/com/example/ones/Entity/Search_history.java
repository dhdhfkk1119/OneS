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
public class Search_history {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long searchIdx;

    @Column(nullable = false)
    private Long searchUseridx;

    @Column(nullable = false)
    private String searchKeyword;

    @Column(nullable = false)
    private LocalDateTime searchAt;
}
