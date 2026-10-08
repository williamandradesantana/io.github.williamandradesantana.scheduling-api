package io.github.williamandradesantana.scheduling_api.domain.auth.controller;

import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.AccountCredentialsDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.services.AuthServices;
import jakarta.validation.Valid;
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
    public ResponseEntity<?> signIn(@RequestBody @Valid AccountCredentialsDTO credentials) {
        return authServices.signIn(credentials);
    }

    @PutMapping(
        value = "/refresh/{userName}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> refreshToken(
        @PathVariable("userName") String userName,
        @RequestHeader("Authorization") String refreshToken
    ) {
        return authServices.refreshToken(userName, refreshToken);
    }
}
