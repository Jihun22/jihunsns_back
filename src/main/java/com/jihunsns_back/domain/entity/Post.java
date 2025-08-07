package com.jihunsns_back.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    @Id @GeneratedValue
    private Long id;

    private String content;
    private LocalDateTime createdAt;

    @ManyToOne
    private User author;
}