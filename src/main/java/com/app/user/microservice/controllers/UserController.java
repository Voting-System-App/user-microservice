package com.app.user.microservice.controllers;

import com.app.user.microservice.config.security.JWTUtil;
import com.app.user.microservice.config.security.PBKDF2Encoder;
import com.app.user.microservice.entities.Role;
import com.app.user.microservice.entities.User;
import com.app.user.microservice.entities.authentication.AuthRequest;
import com.app.user.microservice.entities.authentication.AuthResponse;
import com.app.user.microservice.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@Tag(name = "Voter Api", description = "Api for testing the endpoint for voters")
@RequestMapping("/user")
public class UserController {
    private final JWTUtil jwtUtil;
    private final PBKDF2Encoder passwordEncoder;
    private final UserService userService;

    public UserController(JWTUtil jwtUtil, PBKDF2Encoder passwordEncoder, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/{role}")
    public ResponseEntity<Mono<User>> saveVoter(@RequestBody User user, @PathVariable Role role){
        return ResponseEntity.ok(userService.save(user,role));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest ar) {
        return userService.findByUsername(ar.getUsername())
                .filter(userDetails -> passwordEncoder.encode(ar.getPassword()).equals(userDetails.getPassword()))
                .map(userDetails -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(userDetails))))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }
}
