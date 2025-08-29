// src/main/java/com/jihunsns_back/domain/repository/PostRepository.java
package com.jihunsns_back.domain.repository;

import com.jihunsns_back.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 단건 조회: 작성자 + 이미지까지 한 번에 로딩
    @EntityGraph(attributePaths = {"author", "images"})
    Optional<Post> findWithAuthorAndImagesById(Long id);

    // 목록 조회(페이지): 작성자 + 이미지까지 로딩
    @EntityGraph(attributePaths = {"author", "images"})
    Page<Post> findAllBy(Pageable pageable);

    // 사용자별 목록
    @EntityGraph(attributePaths = {"author", "images"})
    Page<Post> findByAuthor_Id(Long authorId, Pageable pageable);
}