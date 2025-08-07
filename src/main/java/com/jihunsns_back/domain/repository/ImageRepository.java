package com.jihunsns_back.domain.repository;

import com.jihunsns_back.domain.entity.Image;
import com.jihunsns_back.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    // 게시글에 연결된 이미지 전체 조회
    List<Image> findByPost(Post post);

    // 이미지 URL로 조회
    Image findByUrl(String url);

    // 게시글 삭제 시 이미지 일괄 삭제 가능
    void deleteByPost(Post post);
}