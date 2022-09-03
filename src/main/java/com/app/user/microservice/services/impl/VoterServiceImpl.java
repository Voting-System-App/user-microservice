package com.app.user.microservice.services.impl;

import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.repositories.VoterRepository;
import com.app.user.microservice.services.VoterService;
import com.app.user.microservice.utils.DateComparison;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class VoterServiceImpl implements VoterService {

    private final VoterRepository voterRepository;
    private final DateComparison dateTime;
    private final String directory;
    private final String tempDirectory;
    private final WebClient webClientElectronicVote;

    public VoterServiceImpl(VoterRepository voterRepository, DateComparison dateTime, @Value("${config.upload}") String directory,
                            @Value("${config.upload.temp}") String tempDirectory, WebClient.Builder webClientElectronicVote,
                            @Value("${electronic.vote}") String electronicVote){
        this.voterRepository = voterRepository;
        this.dateTime = dateTime;
        this.directory = directory;
        this.tempDirectory = tempDirectory;
        this.webClientElectronicVote = webClientElectronicVote.baseUrl(electronicVote).build();
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Voter> findAll() {
        return voterRepository.findAll();
    }

    @Override
    public Mono<Page<Voter>> findAllVotersByPage(Pageable pageable) {
        return voterRepository.findAllBy(pageable)
                .collectList()
                .zipWith(voterRepository.count())
                .map(result-> new PageImpl<>(result.getT1(),pageable, result.getT2()));
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Voter> findById(String id){
        return voterRepository.findById(id);
    }

    @Override
    @Transactional
    public Mono<Voter> save(Voter voter, FilePart file){
        voter.setFingerPrint(UUID.randomUUID() + "-" + file.filename()
                .replace(" ","")
                .replace(":","")
                .replace("\\",""));
        return file.transferTo(new File(directory+ voter.getFingerPrint())).then(voterRepository.save(voter));
    }
    @Override
    @Transactional
    public Mono<Voter> update(Voter voter, String id) {
        return voterRepository.findById(id).flatMap(result->{
            result.setName(voter.getName());
            result.setLastName(voter.getLastName());
            result.setEmail(voter.getEmail());
            result.setDni(voter.getDni());
            result.setGender(voter.getGender());
            return voterRepository.save(result);
        });
    }
    @Override
    public Mono<Boolean> validate(String path, String tempPath) throws IOException {
        FingerprintTemplate probe = new FingerprintTemplate(
                new FingerprintImage(
                        Files.readAllBytes(Paths.get(directory)
                                .resolve(path).toAbsolutePath()),
                        new FingerprintImageOptions()
                                .dpi(500)));
        FingerprintTemplate candidate = new FingerprintTemplate(
                new FingerprintImage(
                        Files.readAllBytes(Paths.get(tempDirectory)
                                .resolve(tempPath).toAbsolutePath()),
                        new FingerprintImageOptions()
                                .dpi(500)));
        double score = new FingerprintMatcher(probe)
                .match(candidate);
        double threshold = 40;
        boolean matches = score >= threshold;
        return Mono.just(matches);
    }
    @Scheduled(fixedRate = 3000)
    public void updateVotingGroup(){
        LocalDate date = LocalDate.now(ZoneId.of("America/Lima"));
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        System.out.println(date);
        System.out.println(time.format(formatter));
        System.out.println(dateTime.compareHours(time));
    }
}
