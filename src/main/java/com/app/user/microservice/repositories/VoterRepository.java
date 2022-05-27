package com.app.user.microservice.repositories;

import com.app.user.microservice.entities.Voter;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoterRepository extends ReactiveMongoRepository<Voter,String> {
}
