package com.app.user.microservice.repositories;

import com.app.user.microservice.entities.VotingManager;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingManagerRepository extends ReactiveMongoRepository<VotingManager,String> {
}
