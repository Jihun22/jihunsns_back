package com.jihunsns_back.domain.repository;

import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 작성자의 게시글 목록 조회
    List<Post> findByAuthor(User author);

    // 내용으로 검색
    List<Post> findByContentContaining(String keyword);
}