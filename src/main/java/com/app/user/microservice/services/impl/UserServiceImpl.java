package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.User;
import com.app.user.microservice.repositories.UserRepository;
import com.app.user.microservice.services.UserService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;


@Service
public class UserServiceImpl implements ReactiveUserDetailsService, UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findByUsername(username).cast(UserDetails.class).log();
    }

    @Transactional
    @Override
    public Mono<User> save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public Mono<User> update(User user, String id) {
        return userRepository.findById(id).flatMap(result->{
            result.setEmail(user.getEmail());
            result.setPassword(user.getPassword());
            return userRepository.save(result);
        });
    }
}
