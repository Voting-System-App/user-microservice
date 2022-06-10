package com.app.user.microservice.services.impl;

import com.app.user.microservice.config.security.PBKDF2Encoder;
import com.app.user.microservice.entities.Role;
import com.app.user.microservice.entities.User;
import com.app.user.microservice.repositories.UserRepository;
import com.app.user.microservice.services.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PBKDF2Encoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PBKDF2Encoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    @Override
    public Mono<User> save(User user) {
        user.setEnabled(true);
        user.setRoles(List.of(Role.ROLE_ADMIN));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Mono<User> update(User user, String id) {
        return userRepository.findById(id).flatMap(result->{
            result.setUsername(user.getUsername());
            result.setPassword(user.getPassword());
            return userRepository.save(result);
        });
    }
}
