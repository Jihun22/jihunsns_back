package com.jihunsns_back.domain.repository;

import com.jihunsns_back.domain.entity.Comment;
import com.jihunsns_back.domain.entity.Post;
import com.jihunsns_back.domain.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글의 댓글 목록 조회
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);

    // 특정 사용자가 작성한 댓글 목록
    List<Comment> findByAuthor(User author);

    // 게시글 ID로 댓글 삭제
    void deleteByPost(Post post);

    // 사용자 ID로 댓글 삭제 (사용자 탈퇴 시)
    void deleteByAuthor(User author);

    List<Comment> findBypostId(Long postId);

    List<Comment> id(Long id);

    List<Comment> findByPostId(Long postId, Pageable pageable);

    void deleteById(Long id);
}