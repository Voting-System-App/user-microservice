package com.app.user.microservice.repositories;

import com.app.user.microservice.entities.Voter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface VoterRepository extends ReactiveSortingRepository<Voter,String> {
    Flux<Voter> findAll(Pageable pageable);
}
