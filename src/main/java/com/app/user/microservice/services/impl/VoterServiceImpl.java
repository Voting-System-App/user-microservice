package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.repositories.VoterRepository;
import com.app.user.microservice.services.VoterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public Mono<Page<Voter>> findAllVotersByPage(Pageable pageable) {
        return voterRepository.findAllBy(pageable)
                .collectList()
                .zipWith(voterRepository.count())
                .map(result-> new PageImpl<>(result.getT1(),pageable, result.getT2()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Voter> findById(String id){
        return voterRepository.findById(id);
    }

    @Override
    @Transactional
    public Mono<Voter> save(Voter voter){
        return voterRepository.save(voter);
    }

    @Override
    public Mono<Voter> updateFingerPrint(String voterId, String fingerPrint) {
        return voterRepository.findById(voterId).flatMap(result->{
            result.setFingerPrint(fingerPrint);
            return voterRepository.save(result);
        });
    }

}
