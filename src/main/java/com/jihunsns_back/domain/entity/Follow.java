package com.jihunsns_back.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "follows",
        uniqueConstraints = @UniqueConstraint(name = "uk_follows_from_to",
                columnNames = {"follower_id","following_id"}),
        indexes = {
                @Index(name = "idx_follows_follower_id", columnList = "follower_id"),
                @Index(name = "idx_follows_following_id", columnList = "following_id")
        })
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "following_id", nullable = false)
    private User following;
}