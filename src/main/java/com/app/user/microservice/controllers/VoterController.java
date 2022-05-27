package com.app.user.microservice.controllers;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.services.VoterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/voter")
public class VoterController {

    private final VoterService voterService;

    public VoterController(VoterService voterService) {
        this.voterService = voterService;
    }

    @GetMapping
    public ResponseEntity<Flux<Voter>> findAll(){
        Flux<Voter> Voter = voterService.findAll();
        return ResponseEntity.ok(Voter);
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Voter>> findById(@PathVariable String id){
        return voterService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mono<Voter>> saveVoter(@RequestBody Voter voter){
        return ResponseEntity.ok(voterService.save(voter));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Voter>> updateVoter(@RequestBody Voter Voter, @PathVariable String id){
        return voterService.update(Voter,id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
