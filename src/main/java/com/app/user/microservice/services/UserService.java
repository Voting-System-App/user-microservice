package com.app.user.microservice.services;

import com.app.user.microservice.entities.Role;
import com.app.user.microservice.entities.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> findByUsername(String username);
    Mono<User> save(User user, Role role);
    Mono<User> update(User user,String id);
}
