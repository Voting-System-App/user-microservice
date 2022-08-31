package com.app.user.microservice.repositories;

import com.app.user.microservice.entities.Voter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VoterRepository extends ReactiveMongoRepository<Voter,String> {
    Flux<Voter> findAllBy(Pageable pageable);
    Flux<Voter> findAllByDniEndingWith(String lastNumber);
}
