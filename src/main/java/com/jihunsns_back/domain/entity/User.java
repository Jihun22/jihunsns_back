package com.jihunsns_back.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
   @Id @GeneratedValue
    private Long id;

   private String email;
   private String nickname;
   private String password;
   private String role = "ROLE_USER";
   private LocalDateTime createdAt;
}

