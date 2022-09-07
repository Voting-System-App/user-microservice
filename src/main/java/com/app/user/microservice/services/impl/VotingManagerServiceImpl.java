package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.entities.VotingManager;
import com.app.user.microservice.entities.models.Voting;
import com.app.user.microservice.entities.models.VotingDate;
import com.app.user.microservice.entities.models.VotingGroup;
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
    private Flux<VotingGroup> createDateAndGroups(VotingDate votingDate) {
        return webClientElectronicVote.post().uri("/date").
                body(Mono.just(votingDate), VotingDate.class).
                retrieve().bodyToFlux(VotingGroup.class);
    }
    private Mono<Long> findAllByElectoralVotingId(String id) {
        return webClientElectronicVote.get().uri("/graphic/electoral/voting/"+id).
                retrieve().bodyToMono(Long.class);
    }
    private Mono<Long> findAllByCandidateId(String id) {
        return webClientElectronicVote.get().uri("/graphic/candidate/"+id).
                retrieve().bodyToMono(Long.class);
    }
    private Mono<Long> findAllByPoliticalParty(String id) {
        return webClientElectronicVote.get().uri("/graphic/political/party/"+id).
                retrieve().bodyToMono(Long.class);
    }
    private Mono<Long> findAllByCityName(String name) {
        return webClientElectronicVote.get().uri("/graphic/state/"+name).
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
    private Mono<VotingDate> updateDate(VotingDate votingDate,String id) {
        return webClientElectronicVote.put().uri("/date/" + id).
                body(Mono.just(votingDate), VotingDate.class).
                retrieve().bodyToMono(VotingDate.class);
    }
    private Flux<Voter> assignGroupIdToVoter(VotingGroup result){
        if(result.getName().equals("A")){
            return voterRepository.findAllByDniEndingWith("1").flatMap(object->{
                object.getGroupList().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("B")){
            return voterRepository.findAllByDniEndingWith("2").flatMap(object->{
                object.getGroupList().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("C")){
            return voterRepository.findAllByDniEndingWith("3").flatMap(object->{
                object.getGroupList().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("D")){
            return voterRepository.findAllByDniEndingWith("4").flatMap(object->{
                object.getGroupList().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("E")){
            return voterRepository.findAllByDniEndingWith("5").flatMap(object->{
                object.getGroupList().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("F")){
            return voterRepository.findAllByDniEndingWith("6").flatMap(object->{
                object.getGroupList().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("G")){
            return voterRepository.findAllByDniEndingWith("7").flatMap(object->{
                object.getGroupList().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("H")){
            return voterRepository.findAllByDniEndingWith("8").flatMap(object->{
                object.getGroupList().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("I")){
            return voterRepository.findAllByDniEndingWith("9").flatMap(object->{
                object.getGroupList().add(result.getId());
                return voterRepository.save(object);
            });
        }
        return voterRepository.findAllByDniEndingWith("0").flatMap(object->{
            object.getGroupList().add(result.getId());
            return voterRepository.save(object);
        });
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
    public Flux<Voting> findAllElectoralVoting() {
        return findAllVoting();
    }

    @Override
    public Mono<Voting> assignVotingGroup(Voting voting) {
        return createDateAndGroups(voting.getVotingDate()).flatMap(result->{
            voting.setVotingDate(result.getVotingDate());
            return assignGroupIdToVoter(result);
        }).then(createVoting(voting));
    }

    @Override
    public Mono<VotingDate> updateVotingDate(VotingDate date, String id) {
        return updateDate(date,id);
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
