package com.app.user.microservice.controllers;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.entities.models.VotingDetail;
import com.app.user.microservice.entities.models.VotingGroup;
import com.app.user.microservice.services.VoterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@RestController
@Tag(name = "Voter Api", description = "Api for testing the endpoint for voters")
@RequestMapping("/voter")
public class VoterController {
    private final VoterService voterService;

    public VoterController(VoterService voterService) {
        this.voterService = voterService;
    }
    @GetMapping
    public ResponseEntity<Flux<Voter>> findAll(){
        Flux<Voter> voters = voterService.findAll();
        return ResponseEntity.ok(voters);
    }
    @GetMapping("/groups")
    public ResponseEntity<Flux<VotingGroup>> findAllGroupData(){
        Flux<VotingGroup> groups = voterService.findAllGroups();
        return ResponseEntity.ok(groups);
    }
    @GetMapping("/all")
    public Mono<Page<Voter>> findAllByPage(@RequestParam("page") int page, @RequestParam("size") int size){
        return voterService.findAllVotersByPage(PageRequest.of(page, size));
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Voter>> findById(@PathVariable String id){
        return voterService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/dni/{dni}/birth/{birthDate}/emission/{emissionDate}")
    public Mono<ResponseEntity<Voter>> findByDniData(@PathVariable String dni, @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") Date birthDate, @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy") Date emissionDate){
        return voterService.findByDniAndDate(dni,birthDate,emissionDate).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<Mono<Voter>> saveVoter(@RequestBody Voter voter){
        return ResponseEntity.ok(voterService.save(voter));
    }

    @PostMapping("/vote")
    public ResponseEntity<Mono<VotingDetail>> saveVote(@RequestBody VotingDetail votingDetail){
        return ResponseEntity.ok(voterService.saveElectoralVote(votingDetail));
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Voter>> update(@RequestBody Voter voter ,@PathVariable String id){
        return voterService.update(voter,id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/group/status/{name}")
    public Mono<Boolean> findGroupStatus(@PathVariable String name){
        return voterService.groupStatus(name);
    }
}
