package com.app.user.microservice.services;

import com.app.user.microservice.entities.Voter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VoterService {
    Flux<Voter> findAll();
    Mono<Page<Voter>> findAllVotersByPage(Pageable pageable);
    Mono<Voter> findById(String id);
    Mono<Voter> save(Voter voter);
    Mono<Voter> updateFingerPrint(String voterId,String fingerPrint);
}
