package com.jihunsns_back.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes",
        uniqueConstraints = @UniqueConstraint(name = "uk_likes_post_user", columnNames = {"post_id","user_id"}),
        indexes = {
                @Index(name = "idx_likes_post_id", columnList = "post_id"),
                @Index(name = "idx_likes_user_id", columnList = "user_id")
        })
public class PostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public PostLike(Post post, User user) {
        this.post = post;
        this.user = user;
    }
    public static PostLike of(Post post, User user) {
        return new PostLike(post, user);
    }
}