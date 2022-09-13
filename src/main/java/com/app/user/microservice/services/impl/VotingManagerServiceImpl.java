package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.VotingManager;
import com.app.user.microservice.entities.models.Voting;
import com.app.user.microservice.entities.models.VotingStatus;
import com.app.user.microservice.repositories.VoterRepository;
import com.app.user.microservice.repositories.VotingManagerRepository;
import com.app.user.microservice.services.VotingManagerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VotingManagerServiceImpl implements VotingManagerService {

    private final VotingManagerRepository votingManagerRepository;
    private final VoterRepository voterRepository;
    private final WebClient webClientElectronicVote;

    public VotingManagerServiceImpl(VotingManagerRepository votingManagerRepository, VoterRepository voterRepository, WebClient.Builder webClientElectronicVote,
                                    @Value("${electronic.vote}") String electronicVote) {
        this.votingManagerRepository = votingManagerRepository;
        this.voterRepository = voterRepository;
        this.webClientElectronicVote = webClientElectronicVote.baseUrl(electronicVote).build();
    }
    private Flux<Voting> findAllVoting() {
        return webClientElectronicVote.get().uri("/voting").
                retrieve().bodyToFlux(Voting.class);
    }
    private Flux<Voting> findVotingByStatus(VotingStatus status) {
        return webClientElectronicVote.get().uri("/voting/status/"+status).
                retrieve().bodyToFlux(Voting.class);
    }
    private Mono<Long> findAllByElectoralVotingId(String id) {
        return webClientElectronicVote.get().uri("/vote/detail/graphic/electoral/voting/"+id).
                retrieve().bodyToMono(Long.class);
    }
    private Mono<Long> findAllByCandidateId(String id) {
        return webClientElectronicVote.get().uri("/vote/detail/graphic/candidate/"+id).
                retrieve().bodyToMono(Long.class);
    }
    private Mono<Long> findAllByPoliticalParty(String id) {
        return webClientElectronicVote.get().uri("/vote/detail/graphic/political/party/"+id).
                retrieve().bodyToMono(Long.class);
    }
    private Mono<Long> findAllByCityName(String name) {
        return webClientElectronicVote.get().uri("/vote/detail/graphic/state/"+name).
                retrieve().bodyToMono(Long.class);
    }
    private Mono<Voting> createVoting(Voting voting) {
        return webClientElectronicVote.post().uri("/voting").
                body(Mono.just(voting), Voting.class).
                retrieve().bodyToMono(Voting.class);
    }
    private Mono<Voting> updateVoting(Voting voting,String id) {
        return webClientElectronicVote.put().uri("/voting/" + id).
                body(Mono.just(voting), Voting.class).
                retrieve().bodyToMono(Voting.class);
    }
    private Mono<String> deleteVoting(String id) {
        return webClientElectronicVote.delete().uri("/voting/" + id).
                retrieve().bodyToMono(String.class);
    }
    @Override
    public Flux<VotingManager> findAll() {
        return votingManagerRepository.findAll();
    }

    @Override
    public Flux<Voting> findAllVotingByStatus(VotingStatus votingStatus) {
        return findVotingByStatus(votingStatus);
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
    public Flux<Voting> findAllElectoralVoting() {
        return findAllVoting();
    }
    @Override
    public Mono<Voting> saveVoting(Voting voting) {
        return createVoting(voting);
    }
    @Override
    public Mono<Voting> updateVotingTask(Voting voting, String id) {
        return updateVoting(voting,id);
    }
    @Override
    public Mono<String> deleteVotingTask(String id) {
        return deleteVoting(id);
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

    @Override
    public Mono<Long> findAllByCandidateListId(String id) {
        return findAllByCandidateId(id);
    }

    @Override
    public Mono<Long> findAllByCandidateListPoliticalPartyId(String id) {
        return findAllByPoliticalParty(id);
    }

    @Override
    public Mono<Long> findAllByVoterCityStateName(String name) {
        return findAllByCityName(name);
    }

    @Override
    public Mono<Long> findAllByVotingId(String id) {
        return findAllByElectoralVotingId(id);
    }
}
