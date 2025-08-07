package com.jihunsns_back.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {
    @Id @GeneratedValue
    private Long id;

    private String url;
    private String mimeType;

    @ManyToOne
    private Post post;
}