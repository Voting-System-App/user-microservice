package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.entities.models.VotingDetail;
import com.app.user.microservice.repositories.VoterRepository;
import com.app.user.microservice.services.VoterService;
import com.app.user.microservice.utils.Groups;
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
    private final Groups groups;
    private final WebClient webClientElectronicVote;

    public VoterServiceImpl(VoterRepository voterRepository, Groups groups, WebClient.Builder webClientElectronicVote,
                            @Value("${electronic.vote}") String electronicVote){
        this.voterRepository = voterRepository;
        this.groups = groups;
        this.webClientElectronicVote = webClientElectronicVote.baseUrl(electronicVote).build();
    }
    private Mono<VotingDetail> createElectoralVote(VotingDetail votingDetail) {
        return webClientElectronicVote.post().uri("/vote/detail").
                body(Mono.just(votingDetail), VotingDetail.class).
                retrieve().bodyToMono(VotingDetail.class);
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
    public Mono<Voter> save(Voter voter) {
        voter.setGroup(groups.assignGroup(voter.getDni()));
        return voterRepository.save(voter);
    }

    @Override
    public Mono<VotingDetail> saveElectoralVote(VotingDetail votingDetail) {
        return createElectoralVote(votingDetail);
    }

    @Override
    @Transactional
    public Mono<Voter> update(Voter voter, String id) {
        return voterRepository.findById(id).flatMap(result->{
            result.setName(voter.getName());
            result.setLastName(voter.getLastName());
            result.setEmail(voter.getEmail());
            result.setDni(voter.getDni());
            result.setGender(voter.getGender());
            return voterRepository.save(result);
        });
    }
}
