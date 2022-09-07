package com.app.user.microservice.services;

import com.app.user.microservice.entities.VotingManager;
import com.app.user.microservice.entities.models.Voting;
import com.app.user.microservice.entities.models.VotingDate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VotingManagerService {
    Flux<VotingManager> findAll();
    Mono<VotingManager> findById(String id);
    Mono<VotingManager> save(VotingManager manager);
    Flux<Voting> findAllElectoralVoting();
    Mono<Voting> assignVotingGroup(Voting voting);
    Mono<VotingDate> updateVotingDate(VotingDate date, String id);
    Mono<Voting> updateVotingTask(Voting voting, String id);
    Mono<String> deleteVotingTask(String id);
    Mono<VotingManager> update(VotingManager manager,String id);
    Mono<Long> findAllByCandidateListId(String id);
    Mono<Long> findAllByCandidateListPoliticalPartyId(String id);
    Mono<Long> findAllByVoterCityStateName(String name);
    Mono<Long> findAllByVotingId(String id);
}
