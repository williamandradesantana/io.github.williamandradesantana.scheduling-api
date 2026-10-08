package io.github.williamandradesantana.scheduling_api.domain.auth.controller;

import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.request.CreateUserRequest;
import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.AccountCredentialsDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.TokenDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.services.AuthServices;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServices authServices;

    public AuthController(AuthServices authServices) {
        this.authServices = authServices;
    }

    @PostMapping(
        value = "/sign-in",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TokenDTO> signIn(@RequestBody @Valid AccountCredentialsDTO credentials) {
        return authServices.signIn(credentials);
    }

    @PostMapping(
        value = "/create-user",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserRequest request) {
        authServices.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(
        value = "/refresh/{userName}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TokenDTO> refreshToken(
        @PathVariable("userName") String userName,
        @RequestHeader("Authorization") String refreshToken
    ) {
        return authServices.refreshToken(userName, refreshToken);
    }
}
