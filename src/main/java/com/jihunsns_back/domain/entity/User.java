package com.jihunsns_back.domain.entity;

import com.jihunsns_back.domain.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.Cleanup;
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
@Index(name = "idx_users_nickname" , columnList = "nickname"),
@Index(name = "idx_users_auth_provider_provider_id", columnList = "auth_provider, provider_id" , unique = true)} )

public class User {
   @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   // 소셜 로그인 이메일 없을수 있으니 nullable true로 변경
   @Column( length = 190, unique = true)
   private String email;

   @Column(nullable = false , length = 50)
   private String nickname;

   // 소셜 로그인 사용자 비번 없을수 있으므로 nullable 허용
   @Column(length = 60)
   private String password;

   //다이렉트로 회원가입 한사람은 로컬
   @Enumerated(EnumType.STRING)
   @Column(nullable = false, length = 20)
   private AuthProvider authProvider = AuthProvider.LOCAL;

   // 소셜 로그인 사용자를 구분하는 값
   @Column(length = 100)
   private String providerId;

   @Column(length = 500)
   private String profileImage;

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

