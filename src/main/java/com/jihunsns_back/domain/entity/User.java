package com.jihunsns_back.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users", indexes = {@Index(name = "idx_users_email" , columnList = "email" , unique = true),
@Index(name = "idx_users_nickname" , columnList = "nickname")} )
public class User {
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(nullable = false , length = 190, unique = true)
   private String email;

   @Column(nullable = false , length = 50)
   private String nickname;

   @Column(nullable = false , length = 60)
   private String password;

   @Column(nullable = false , length = 30)
   private String role = "ROLE_USER";

   @CreationTimestamp
   @Column(nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @OneToMany(mappedBy = "author" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Post>posts = new ArrayList<>();

   @OneToMany(mappedBy = "author" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}

