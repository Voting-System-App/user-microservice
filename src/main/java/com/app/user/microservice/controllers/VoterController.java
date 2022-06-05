package com.app.user.microservice.controllers;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.services.VoterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Voter Api", description = "Api for testing the endpoint for voters")
@RequestMapping("/voter")
public class VoterController {
    private final VoterService voterService;

    private final ReactiveGridFsTemplate gridFsTemplate;

    public VoterController(VoterService voterService, ReactiveGridFsTemplate gridFsTemplate) {
        this.voterService = voterService;
        this.gridFsTemplate = gridFsTemplate;
    }

    @GetMapping
    public ResponseEntity<Flux<Voter>> findAll(){
        Flux<Voter> voters = voterService.findAll();
        return ResponseEntity.ok(voters);
    }
    @GetMapping("all")
    public Mono<Page<Voter>> findAllByPage(@RequestParam("page") int page, @RequestParam("size") int size){
        return voterService.findAllVotersByPage(PageRequest.of(page, size));
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Voter>> findById(@PathVariable String id){
        return voterService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/upload")
    public Mono<ResponseEntity> upload(@RequestPart("file") Mono<FilePart> file) {
        return file
                .flatMap(part -> this.gridFsTemplate.store(part.content(), part.filename()))
                .map((id) -> ok().body(Map.of("id", id.toHexString())));
    }

    @GetMapping("/read/{id}")
    public Flux<Void> read(@PathVariable String id, ServerWebExchange exchange) {
        return this.gridFsTemplate.findOne(query(where("_id").is(id)))
                .log()
                .flatMap(gridFsTemplate::getResource)
                .flatMapMany(r -> exchange.getResponse().writeWith(r.getDownloadStream()));
    }

    @PostMapping
    public ResponseEntity<Mono<Voter>> saveVoter(@RequestBody Voter voter){
        return ResponseEntity.ok(voterService.save(voter));
    }

    @PutMapping("/{voterId}/finger/{fingerPrint}")
    public Mono<ResponseEntity<Voter>> updateFingerPrint(@PathVariable String voterId, @PathVariable String fingerPrint){
        return voterService.updateFingerPrint(voterId,fingerPrint).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
