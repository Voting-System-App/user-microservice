package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.entities.VotingManager;
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

    private Flux<VotingGroup> createDateAndGroups(VotingDate votingDate) {
        return webClientElectronicVote.post().uri("/date").
                body(Mono.just(votingDate), VotingDate.class).
                retrieve().bodyToFlux(VotingGroup.class);
    }
    private Flux<Voter> assignGroupIdToVoter(VotingGroup result){
        if(result.getName().equals("A")){
            return voterRepository.findAllByDniEndingWith("1").flatMap(object->{
                object.getGroupsId().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("B")){
            return voterRepository.findAllByDniEndingWith("2").flatMap(object->{
                object.getGroupsId().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("C")){
            return voterRepository.findAllByDniEndingWith("3").flatMap(object->{
                object.getGroupsId().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("D")){
            return voterRepository.findAllByDniEndingWith("4").flatMap(object->{
                object.getGroupsId().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("E")){
            return voterRepository.findAllByDniEndingWith("5").flatMap(object->{
                object.getGroupsId().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("F")){
            return voterRepository.findAllByDniEndingWith("6").flatMap(object->{
                object.getGroupsId().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("G")){
            return voterRepository.findAllByDniEndingWith("7").flatMap(object->{
                object.getGroupsId().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("H")){
            return voterRepository.findAllByDniEndingWith("8").flatMap(object->{
                object.getGroupsId().add(result.getId());
                return voterRepository.save(object);
            });
        }
        if(result.getName().equals("I")){
            return voterRepository.findAllByDniEndingWith("9").flatMap(object->{
                object.getGroupsId().add(result.getId());
                return voterRepository.save(object);
            });
        }
        return voterRepository.findAllByDniEndingWith("0").flatMap(object->{
            object.getGroupsId().add(result.getId());
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
    public Mono<String> assignVotingGroup(VotingDate date) {
        return createDateAndGroups(date).flatMap(this::assignGroupIdToVoter).then(Mono.just("Group updated"));
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
