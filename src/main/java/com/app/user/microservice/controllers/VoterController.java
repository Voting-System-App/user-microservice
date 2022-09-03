package com.app.user.microservice.controllers;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.services.VoterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@Tag(name = "Voter Api", description = "Api for testing the endpoint for voters")
@RequestMapping("/voter")
public class VoterController {
    private final VoterService voterService;
    private final String tempDirectory;

    public VoterController(VoterService voterService,@Value("${config.upload.temp}") String tempDirectory) {
        this.voterService = voterService;
        this.tempDirectory = tempDirectory;
    }
    @GetMapping
    public ResponseEntity<Flux<Voter>> findAll(){
        Flux<Voter> voters = voterService.findAll();
        return ResponseEntity.ok(voters);
    }
    @GetMapping("/all")
    public Mono<Page<Voter>> findAllByPage(@RequestParam("page") int page, @RequestParam("size") int size){
        return voterService.findAllVotersByPage(PageRequest.of(page, size));
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Voter>> findById(@PathVariable String id){
        return voterService.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @GetMapping("/validate")
    private Mono<Boolean> validate(@RequestPart String path,@RequestPart String tempPath) throws IOException{
        return voterService.validate(path,tempPath);
    }
    @PostMapping("/saveTemp")
    public Mono<String> saveTemporalFingerPrint(@RequestPart FilePart file) {
        String tempPath = UUID.randomUUID() + "-" + file.filename()
                .replace(" ","")
                .replace(":","")
                .replace("\\","");
        return file.transferTo(new File(tempDirectory+tempPath)).thenReturn(tempPath);
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
