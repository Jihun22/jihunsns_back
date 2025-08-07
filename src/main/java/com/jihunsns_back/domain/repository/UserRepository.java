package com.jihunsns_back.domain.repository;

import com.jihunsns_back.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자 찾기 (로그인/중복확인 등에 사용)
    Optional<User> findByEmail(String email);

    // 닉네임으로 사용자 찾기 (닉네임 중복확인용)
    Optional<User> findByNickname(String nickname);

    // 이메일 중복 여부 확인
    boolean existsByEmail(String email);

    // 닉네임 중복 여부 확인
    boolean existsByNickname(String nickname);
}