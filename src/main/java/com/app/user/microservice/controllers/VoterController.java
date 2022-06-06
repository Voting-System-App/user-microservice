package com.app.user.microservice.controllers;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.services.VoterService;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    @GetMapping("all")
    public Mono<Page<Voter>> findAllByPage(@RequestParam("page") int page, @RequestParam("size") int size){
        return voterService.findAllVotersByPage(PageRequest.of(page, size));
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Voter>> findById(@PathVariable String id){
        return voterService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/validate")
    public boolean read() throws IOException {
        FingerprintTemplate probe = new FingerprintTemplate(
                new FingerprintImage(
                        Files.readAllBytes(Paths.get("C://Users//User//OneDrive//Documentos//GitHub//images//")
                                .resolve("dc615ca7-0f3f-4903-8354-5d3238178164-h1.png").toAbsolutePath()),
                        new FingerprintImageOptions()
                                .dpi(500)));
        FingerprintTemplate candidate = new FingerprintTemplate(
                new FingerprintImage(
                        Files.readAllBytes(Paths.get("C://Users//User//OneDrive//Documentos//GitHub//images//")
                                .resolve("h2.png").toAbsolutePath()),
                        new FingerprintImageOptions()
                                .dpi(500)));
        double score = new FingerprintMatcher(probe)
                .match(candidate);
        double threshold = 40;
        boolean matches = score >= threshold;
        return matches;
    }

    @PostMapping
    public ResponseEntity<Mono<Voter>> saveVoter(@Valid Voter voter,@RequestPart FilePart file){
        return ResponseEntity.ok(voterService.save(voter,file));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Voter>> update(@RequestBody Voter voter ,@PathVariable String id){
        return voterService.update(voter,id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
