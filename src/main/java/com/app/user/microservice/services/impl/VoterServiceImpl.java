package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.Status;
import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.entities.models.Voting;
import com.app.user.microservice.entities.models.VotingDetail;
import com.app.user.microservice.entities.models.VotingGroup;
import com.app.user.microservice.entities.models.VotingStatus;
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

    public VoterServiceImpl(VoterRepository voterRepository, DateComparison dateComparison, Groups groups, WebClient.Builder webClientElectronicVote,
                            @Value("${electronic.vote}") String electronicVote){
        this.voterRepository = voterRepository;
        this.dateComparison = dateComparison;
        this.groups = groups;
        this.webClientElectronicVote = webClientElectronicVote.baseUrl(electronicVote).build();
    }
    private Flux<VotingGroup> findAllGroupData(){
        return webClientElectronicVote.get().uri("/voting/groups").
                retrieve().bodyToFlux(VotingGroup.class);
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
    private Flux<Voting> findAllByCityStatus(String city, VotingStatus status){
        return webClientElectronicVote.get().uri("/voting/city/"+city+"/status/"+status).
                retrieve().bodyToFlux(Voting.class);
    }
    private Flux<Voting> findAllByVoterIdCityStatus(String city, VotingStatus status,String id){
        return webClientElectronicVote.get().uri("/voting/city/"+city+"/status/"+status+"/voter/"+id).
                retrieve().bodyToFlux(Voting.class);
    }
    @Override
    @Transactional(readOnly = true)
    public Flux<Voter> findAll() {
        return voterRepository.findAll();
    }

    @Override
    public Mono<Long> findTotalVoters() {
        return voterRepository.findAll().count();
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Voter> findByDni(String dni) {
        return voterRepository.findByDniIsContaining(dni);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Voter> findByName(String name) {
        return voterRepository.findByNameIsContainingIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<VotingGroup> findAllGroups() {
        return findAllGroupData();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Voting> findByVotingId(String id) {
        return findByVotingIdView(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Page<Voter>> findAllVotersByPage(Pageable pageable) {
        return voterRepository.findAllBy(pageable)
                .collectList()
                .zipWith(voterRepository.count())
                .map(result-> new PageImpl<>(result.getT1(),pageable, result.getT2()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Voter> findByDniAndDate(String dni, Date birthDate,Date emissionDate) {
        System.out.println(dateComparison.minusDays(birthDate));
        System.out.println(dateComparison.equal(birthDate));
        return voterRepository.findByDniAndBirthDateBetweenAndEmissionDateBetween(dni,dateComparison.minusDays(birthDate),dateComparison.equal(birthDate),
                dateComparison.minusDays(emissionDate),dateComparison.equal(emissionDate));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Voter> findById(String id){
        return voterRepository.findById(id);
    }

    @Override
    public Mono<Voter> save(Voter voter) {
        voter.setIsActive(Status.ACTIVE);
        voter.setGender(Boolean.parseBoolean(String.valueOf(voter.getGender())));
        voter.setGroup(groups.assignGroup(voter.getDni()));
        voter.setFingerPrint("https://fingerread.s3.sa-east-1.amazonaws.com/"+voter.getDni());
        return voterRepository.save(voter);
    }
    @Override
    @Transactional(readOnly = true)
    public Flux<Voting> findAllByCityAndStatus(String city, VotingStatus votingStatus) {
        return findAllByCityStatus(city, votingStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Voting> findAllByCityAndStatusAndVoter(String city, VotingStatus votingStatus, String id) {
        return findAllByVoterIdCityStatus(city, votingStatus, id);
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
            result.setBirthDate(voter.getBirthDate());
            result.setCity(voter.getCity());
            result.setEmissionDate(voter.getEmissionDate());
            result.setDni(voter.getDni());
            result.setGender(Boolean.parseBoolean(String.valueOf(voter.getGender())));
            return voterRepository.save(result);
        });
    }

    @Override
    public Mono<Boolean> groupStatus(String name) {
        return viewStatusGroup(name);
    }
}
