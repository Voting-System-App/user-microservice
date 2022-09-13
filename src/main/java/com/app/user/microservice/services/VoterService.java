package com.app.user.microservice.services;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.entities.models.VotingDetail;
import com.app.user.microservice.entities.models.VotingGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Date;

public interface VoterService {
    Flux<Voter> findAll();
    Flux<VotingGroup> findAllGroups();
    Mono<Page<Voter>> findAllVotersByPage(Pageable pageable);
    Mono<Voter> findByDniAndDate(String dni, Date birthDate,Date emissionDate);
    Mono<Voter> findById(String id);
    Mono<Voter> save(Voter voter);
    Mono<VotingDetail> saveElectoralVote(VotingDetail votingDetail);
    Mono<Voter> update(Voter voter,String id);
    Mono<Boolean> groupStatus(String name);
}
