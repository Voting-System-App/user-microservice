package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.VotingManager;
import com.app.user.microservice.repositories.VotingManagerRepository;
import com.app.user.microservice.services.VotingManagerService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VotingManagerServiceImpl implements VotingManagerService {

    private final VotingManagerRepository votingManagerRepository;

    public VotingManagerServiceImpl(VotingManagerRepository votingManagerRepository) {
        this.votingManagerRepository = votingManagerRepository;
    }

    @Override
    public Flux<VotingManager> findAll() {
        return votingManagerRepository.findAll();
    }

    @Override
    public Mono<VotingManager> findById(String id) {
        return votingManagerRepository.findById(id);
    }

    @Override
    public Mono<VotingManager> save(VotingManager manager) {
        return votingManagerRepository.save(manager);
    }

    @Override
    public Mono<VotingManager> update(VotingManager manager, String id) {
        return votingManagerRepository.findById(id).flatMap(result->{
            result.setName(manager.getName());
            result.setLastName(manager.getLastName());
            result.setDni(manager.getDni());
            result.setEmail(manager.getEmail());
            result.setGender(manager.getGender());
            return votingManagerRepository.save(result);
        });
    }
}
