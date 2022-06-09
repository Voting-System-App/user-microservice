package com.app.user.microservice.controllers;

import com.app.user.microservice.entities.VotingManager;
import com.app.user.microservice.services.VotingManagerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Voting Manager Api", description = "Api for testing the endpoint for managers")
@RequestMapping("/manager")
public class VotingManagerController {

    private final VotingManagerService votingManagerService;

    public VotingManagerController(VotingManagerService votingManagerService) {
        this.votingManagerService = votingManagerService;
    }

    @GetMapping
    public ResponseEntity<Flux<VotingManager>> findAll(){
        Flux<VotingManager> votingManager = votingManagerService.findAll();
        return ResponseEntity.ok(votingManager);
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<VotingManager>> findById(@PathVariable String id){
        return votingManagerService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mono<VotingManager>> saveVoter(@RequestBody VotingManager manager){
        return ResponseEntity.ok(votingManagerService.save(manager));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<VotingManager>> update(@RequestBody VotingManager manager,@PathVariable String id){
        return votingManagerService.update(manager,id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
