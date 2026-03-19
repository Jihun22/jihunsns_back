package com.jihunsns_back.api.service;

import com.jihunsns_back.domain.entity.User;
import com.jihunsns_back.domain.enums.AuthProvider;
import com.jihunsns_back.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private  final UserRepository userRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User>findByAuthProviderAndProviderId(AuthProvider authProvider, String providerId) {return userRepository.findByAuthProviderAndProviderId(authProvider,providerId);}

    public User save(User user) {
        return userRepository.save(user);
    }
}
