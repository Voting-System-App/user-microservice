package com.app.user.microservice.services;

import com.app.user.microservice.entities.Voter;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoterService {
    Flux<Voter> findAll();
    Flux<Voter> findAllPaged(Pageable pageable);
    Mono<Voter> findById(String id);
    Mono<Voter> save(Voter voter);
    Mono<Voter> update(Voter voter,String id);
}
