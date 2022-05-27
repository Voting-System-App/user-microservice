package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.entities.models.PersonalData;
import com.app.user.microservice.repositories.VoterRepository;
import com.app.user.microservice.services.VoterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VoterServiceImpl implements VoterService {

    private final VoterRepository voterRepository;

    private final WebClient dniValidator;

    public VoterServiceImpl(WebClient.Builder dniValidator,VoterRepository voterRepository, @Value("${reniec.validator}") String reniec) {
        this.voterRepository = voterRepository;
        this.dniValidator = dniValidator.baseUrl(reniec).build();
    }

    private Mono<PersonalData> findDataFromDni(String dni) {
        return dniValidator.get().uri("/dni" + dni).
                retrieve().bodyToMono(PersonalData.class);
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
