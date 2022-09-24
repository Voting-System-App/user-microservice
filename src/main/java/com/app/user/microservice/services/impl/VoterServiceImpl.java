package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.Status;
import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.entities.models.Voting;
import com.app.user.microservice.entities.models.VotingDetail;
import com.app.user.microservice.entities.models.VotingGroup;
import com.app.user.microservice.repositories.VoterRepository;
import com.app.user.microservice.services.VoterService;
import com.app.user.microservice.utils.DateComparison;
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

import java.util.Date;


@Service
public class VoterServiceImpl implements VoterService {

    private final VoterRepository voterRepository;
    private final DateComparison dateComparison;
    private final Groups groups;
    private final WebClient webClientElectronicVote;

    private final WebClient webClientFinger;

    public VoterServiceImpl(VoterRepository voterRepository, DateComparison dateComparison, Groups groups, WebClient.Builder webClientElectronicVote,
                            @Value("${electronic.vote}") String electronicVote,@Value("${finger.reader}") String reader ,WebClient.Builder webClientFinger){
        this.voterRepository = voterRepository;
        this.dateComparison = dateComparison;
        this.groups = groups;
        this.webClientFinger = webClientFinger.baseUrl(reader).build();
        this.webClientElectronicVote = webClientElectronicVote.baseUrl(electronicVote).build();
    }
    private Flux<VotingGroup> findAllGroupData(){
        return webClientElectronicVote.get().uri("/voting/groups").
                retrieve().bodyToFlux(VotingGroup.class);
    }
    private Mono<Boolean> saveFingerPrint(String fileName){
        return webClientFinger.get().uri("/s3/"+fileName+"/validate/"+false)
                .retrieve().bodyToMono(Boolean.class);
    }
    private Mono<VotingDetail> createElectoralVote(VotingDetail votingDetail) {
        return webClientElectronicVote.post().uri("/vote/detail").
                body(Mono.just(votingDetail), VotingDetail.class).
                retrieve().bodyToMono(VotingDetail.class);
    }
    private Mono<Boolean> viewStatusGroup(String name) {
        return webClientElectronicVote.get().uri("/voting/group/"+ name +"/status").
                retrieve().bodyToMono(Boolean.class);
    }
    private Mono<Voting> findByVotingIdView(String id) {
        return webClientElectronicVote.get().uri("/voting/"+ id).
                retrieve().bodyToMono(Voting.class);
    }
    @Override
    @Transactional(readOnly = true)
    public Flux<Voter> findAll() {
        return voterRepository.findAll();
    }

    @Override
    public Flux<VotingGroup> findAllGroups() {
        return findAllGroupData();
    }

    @Override
    public Mono<Voting> findByVotingId(String id) {
        return findByVotingIdView(id);
    }

    @Override
    public Mono<Page<Voter>> findAllVotersByPage(Pageable pageable) {
        return voterRepository.findAllBy(pageable)
                .collectList()
                .zipWith(voterRepository.count())
                .map(result-> new PageImpl<>(result.getT1(),pageable, result.getT2()));
    }

    @Override
    public Mono<Voter> findByDniAndDate(String dni, Date birthDate,Date emissionDate) {
        return voterRepository.findByDniAndBirthDateBetweenAndEmissionDateBetween(dni,dateComparison.minusDays(birthDate),birthDate,
                dateComparison.minusDays(emissionDate),emissionDate);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Voter> findById(String id){
        return voterRepository.findById(id);
    }

    @Override
    public Mono<Voter> save(Voter voter) {
        voter.setIsActive(Status.ACTIVE);
        voter.setGroup(groups.assignGroup(voter.getDni()));
        return saveFingerPrint(voter.getDni()).flatMap(result->{
            voter.setFingerPrint("https://printreader.s3.sa-east-1.amazonaws.com/"+voter.getDni());
            return result ?voterRepository.save(voter):Mono.just(new Voter());
        });
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
            result.setEmissionDate(voter.getEmissionDate());
            result.setDni(voter.getDni());
            result.setGender(voter.getGender());
            return voterRepository.save(result);
        });
    }

    @Override
    public Mono<Boolean> groupStatus(String name) {
        return viewStatusGroup(name);
    }
}
