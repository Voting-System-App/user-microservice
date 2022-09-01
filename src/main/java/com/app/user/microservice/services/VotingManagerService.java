package com.app.user.microservice.services;

import com.app.user.microservice.entities.VotingManager;
import com.app.user.microservice.entities.models.VotingDate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VotingManagerService {
    Flux<VotingManager> findAll();
    Mono<VotingManager> findById(String id);
    Mono<VotingManager> save(VotingManager manager);
    Mono<String> assignVotingGroup(VotingDate date);
    Mono<VotingDate> updateVotingDate(VotingDate date, String id);
    Mono<VotingManager> update(VotingManager manager,String id);
}
