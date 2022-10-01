package com.app.user.microservice.repositories;

import com.app.user.microservice.entities.Voter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Repository
public interface VoterRepository extends ReactiveMongoRepository<Voter,String> {
    Flux<Voter> findAllBy(Pageable pageable);
    Mono<Voter> findByDniAndBirthDateBetweenAndEmissionDateBetween(String dni, Date startBirthDate,Date endBirthDate,Date startEmissionDate,Date endEmissionDate);
    Flux<Voter> findByDniIsContaining(String dni);
    Flux<Voter> findByNameIsContainingIgnoreCase(String name);
}
