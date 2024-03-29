package com.app.user.microservice.controllers;

import com.app.user.microservice.entities.VotingManager;
import com.app.user.microservice.entities.models.Voting;
import com.app.user.microservice.entities.models.VotingStatus;
import com.app.user.microservice.services.VotingManagerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @GetMapping("/voting/status/{status}")
    public Flux<Voting> findByVotingStatus(@PathVariable VotingStatus status){
        return votingManagerService.findAllVotingByStatus(status);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Flux<VotingManager> findAll(){
        return votingManagerService.findAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/voting")
    public Flux<Voting> findAllElectoralVoting(){
        return votingManagerService.findAllElectoralVoting();
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/voting")
    public Mono<Voting> saveVotingDateByGroups(@RequestBody Voting voting){
        return votingManagerService.saveVoting(voting);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<VotingManager>> updateManager(@RequestBody VotingManager manager,@PathVariable String id){
        return votingManagerService.update(manager,id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/voting/{id}")
    public Mono<ResponseEntity<Voting>> updateVoting(@RequestBody Voting voting,@PathVariable String id){
        return votingManagerService.updateVotingTask(voting,id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/voting/{id}")
    public Mono<ResponseEntity<String>> deleteVoting(@PathVariable String id){
        return votingManagerService.deleteVotingTask(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/graphic/electoral/voting/{id}")
    public Mono<Long> findAllByVotingId(@PathVariable String id){
        return votingManagerService.findAllByVotingId(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/graphic/candidate/{id}")
    public Mono<Long> findAllByCandidateId(@PathVariable String id){
        return votingManagerService.findAllByCandidateListId(id);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/graphic/state/{name}")
    public Mono<Long> findAllByCityName(@PathVariable String name){
        return votingManagerService.findAllByVoterCityStateName(name);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/graphic/political/party/{id}")
    public Mono<Long> findAllByPoliticalParty(@PathVariable String id){
        return votingManagerService.findAllByCandidateListPoliticalPartyId(id);
    }
}
