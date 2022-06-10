package com.app.user.microservice.services;

import com.app.user.microservice.entities.User;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<User> save(User user);
    Mono<User> update(User user,String id);
}
