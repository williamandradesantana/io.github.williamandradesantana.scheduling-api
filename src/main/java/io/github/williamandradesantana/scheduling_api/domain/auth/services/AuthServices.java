package io.github.williamandradesantana.scheduling_api.domain.auth.services;

import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.AccountCredentialsDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.TokenDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.User;
import io.github.williamandradesantana.scheduling_api.domain.auth.repositories.UserRepository;
import io.github.williamandradesantana.scheduling_api.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthServices {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;

    public AuthServices(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
    }

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(credentials.getUserName(), credentials.getPassword())
        );

        User user = userRepository.findByUsername(credentials.getUserName());
        if (user == null) throw new UsernameNotFoundException("Username: " + credentials.getUserName() + " not found!");

        var token = tokenProvider.createAccessToken(credentials.getUserName(), user.getRoles());
        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String userName, String refreshToken) {
        User user = userRepository.findByUsername(userName);
        TokenDTO token;
        if (user != null) {
            token = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("Username: " + userName + " not found!");
        }
        return ResponseEntity.ok(token);
    }
}
