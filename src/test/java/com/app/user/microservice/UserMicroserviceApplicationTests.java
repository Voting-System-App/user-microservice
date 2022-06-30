package com.app.user.microservice;


import com.app.user.microservice.entities.Status;
import com.app.user.microservice.entities.User;
import com.app.user.microservice.entities.Voter;
import com.app.user.microservice.repositories.UserRepository;
import com.app.user.microservice.repositories.VoterRepository;
import com.app.user.microservice.repositories.VotingManagerRepository;
import com.app.user.microservice.services.UserService;
import com.app.user.microservice.services.VoterService;
import com.app.user.microservice.services.VotingManagerService;
import com.app.user.microservice.services.impl.VoterServiceImpl;
import com.google.gson.Gson;
import de.flapdoodle.embed.mongo.MongodExecutable;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.mongo.MongoHealthContributorAutoConfiguration;
import org.springframework.boot.actuate.endpoint.web.reactive.AdditionalHealthEndpointPathsWebFluxHandlerMapping;
import org.springframework.boot.actuate.health.HealthContributorRegistry;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.gridfs.ReactiveGridFsTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.WebHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@EnableMongoRepositories
@SpringBootTest
class UserMicroserviceApplicationTests {
    UserMicroserviceApplicationTests(){

    }

    @Autowired
    private VoterService voterService;

    @Autowired
    private UserService userService;

    @Autowired
    private VotingManagerService voterManagerService;

    @MockBean
    private VoterRepository voterRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private VotingManagerRepository voterManagerRespository;

    @MockBean
    private ReactiveGridFsTemplate reactiveGridFsTemplate;

    @MockBean
    private WebHandler webHandler;

    @MockBean
    private AdditionalHealthEndpointPathsWebFluxHandlerMapping healthEndpointWebFluxHandlerMapping;

    @MockBean
    private HealthEndpoint healthEndpoint;

    @MockBean
    private HealthContributorRegistry healthContributorRegistry;

    @MockBean
    private MongoHealthContributorAutoConfiguration mongoHealthContributorAutoConfiguration;

    @MockBean
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @MockBean
    private ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory;

    @MockBean
    private EmbeddedMongoAutoConfiguration embeddedMongoAutoConfiguration;

    @MockBean
    private MongodExecutable embeddedMongoServer;


    private static MockWebServer mockBack;
    private static  List<Voter> listVoter=new ArrayList<>();


    private static  Voter voter=new Voter();

    private static User user=new User();

    @BeforeAll
    static void setUp() throws IOException {
        voter.setId("629d54df602bc72e16a3cfb5");
        voter.setName("Walter");
        voter.setLastName("XD");
        voter.setEmail("w7moises@gmail.com");
        voter.setDni("73938616");
        voter.setGender("Male");
        voter.setBirthDate("19/05/1999");
        voter.setIsActive(Status.ACTIVE);
        voter.setFingerPrint("40f63fd7-3018-440b-9712-90af64a446c9-h1.png");


        listVoter.add(voter);

        mockBack=new MockWebServer();
        mockBack.start(8090);
    }

    @AfterAll
    static void tearsuwu() throws IOException{
        mockBack.shutdown();
    }

    @BeforeEach
    void initialize(@Value("localhost:8090") String urlClient) {

        Gson gson = new Gson();
        mockBack.url("localhost:8090");
        mockBack.enqueue(new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(gson.toJson(listVoter))
                .setResponseCode(HttpStatus.OK.value()));

    }

    //get all voters
    @Test
    void getAll(){
        when(voterRepository.findAll()).thenReturn(Flux.fromIterable(List.of(voter)));

        Assertions.assertNotNull(voterService.findAll());
    }

    //get voter by dni
    @Test
    void getVoterByDni(){
        when(voterRepository.findById("629d54df602bc72e16a3cfb5")).thenReturn(Mono.just(voter));
        Mono<Voter> voterMono=voterService.findById("629d54df602bc72e16a3cfb5");

        StepVerifier.create(voterMono).consumeNextWith(newVoter->{
            assertEquals(newVoter.getDni(),"73938616");
        }).verifyComplete();
    }

    //get voter by id
    @Test
    void getVoterById(){
        when(voterRepository.findById("629d54df602bc72e16a3cfb5")).thenReturn(Mono.just(voter));
        Mono<Voter> voterMono=voterService.findById("629d54df602bc72e16a3cfb5");

        StepVerifier.create(voterMono).consumeNextWith(newVoter->{
            assertEquals(newVoter.getId(),"629d54df602bc72e16a3cfb5");
        }).verifyComplete();
    }
    








}
