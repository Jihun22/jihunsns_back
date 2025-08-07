package com.jihunsns_back.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue
    private Long id;

    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User author;
}