package com.jihunsns_back.api.service;

import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class UserService {

    private  final UserRepository userRepository;

    public Optional<User> findByid(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
