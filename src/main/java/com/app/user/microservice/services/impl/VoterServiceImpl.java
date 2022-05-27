package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.repositories.VoterRepository;
import com.app.user.microservice.services.VoterService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VoterServiceImpl implements VoterService {

    private final VoterRepository voterRepository;

    public VoterServiceImpl(VoterRepository voterRepository) {
        this.voterRepository = voterRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Voter> findAll() {
        return voterRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Voter> findAllPaged(Pageable pageable) {
        return voterRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Voter> findById(String id) {
        return voterRepository.findById(id);
    }

    @Override
    @Transactional
    public Mono<Voter> save(Voter voter) {
        return voterRepository.save(voter);
    }

    @Override
    @Transactional
    public Mono<Voter> update(Voter voter, String id) {
        return voterRepository.findById(id).flatMap(result->{
            result.setName(voter.getName());
            result.setLastName(voter.getLastName());
            result.setEmail(voter.getEmail());
            result.setDni(voter.getDni());
            result.setAge(voter.getAge());
            result.setGender(voter.getGender());
            return voterRepository.save(result);
        });
    }
}
