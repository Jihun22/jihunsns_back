package com.jihunsns_back.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "likes")
public class Like {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Post post;

    @ManyToOne
    private User user;
}