package com.app.user.microservice.services;

import com.app.user.microservice.entities.Voter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoterService {
    Flux<Voter> findAll();
    Mono<Voter> findById(String id);
    Mono<Voter> save(Voter voter);
    Mono<Voter> update(Voter voter,String id);
}
