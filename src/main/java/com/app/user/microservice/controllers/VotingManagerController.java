package com.app.user.microservice.controllers;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.entities.VotingManager;
import com.app.user.microservice.entities.authentication.Message;
import com.app.user.microservice.entities.models.Voting;
import com.app.user.microservice.entities.models.VotingDate;
import com.app.user.microservice.entities.models.VotingGroup;
import com.app.user.microservice.services.VotingManagerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Flux<VotingManager> findAll(){
        return votingManagerService.findAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<VotingManager>> findById(@PathVariable String id){
        return votingManagerService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Mono<VotingManager>> saveManager(@RequestBody VotingManager manager){
        return ResponseEntity.ok(votingManagerService.save(manager));
    }

    @PostMapping("/date")
    public ResponseEntity<Mono<Voting>> saveDateByGroups(@RequestBody Voting voting){
        return ResponseEntity.ok(votingManagerService.assignVotingGroup(voting));
    }

    @PutMapping("/date/{id}")
    public Mono<ResponseEntity<VotingDate>> updateDateByGroups(@RequestBody VotingDate date,@PathVariable String id){
        return votingManagerService.updateVotingDate(date,id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<VotingManager>> updateManager(@RequestBody VotingManager manager,@PathVariable String id){
        return votingManagerService.update(manager,id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
