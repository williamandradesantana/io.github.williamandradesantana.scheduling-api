package io.github.williamandradesantana.scheduling_api.domain.auth.services;

import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.request.CreateUserRequest;
import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.AccountCredentialsDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.TokenDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.Permission;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.User;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.AuthProvider;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.PermissionName;
import io.github.williamandradesantana.scheduling_api.domain.auth.exceptions.PermissionNameNotFound;
import io.github.williamandradesantana.scheduling_api.domain.auth.repositories.PermissionRepository;
import io.github.williamandradesantana.scheduling_api.domain.auth.repositories.UserRepository;
import io.github.williamandradesantana.scheduling_api.exceptions.BusinessException;
import io.github.williamandradesantana.scheduling_api.exceptions.RequiredObjectIsNullException;
import io.github.williamandradesantana.scheduling_api.security.jwt.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthServices {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

    public AuthServices(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, UserRepository userRepository, PasswordEncoder passwordEncoder, PermissionRepository permissionRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.permissionRepository = permissionRepository;
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

    public void createUser(CreateUserRequest request) {
        if (request == null) throw new RequiredObjectIsNullException();

        var userName = request.getUserName().trim();
        var email = request.getEmail().trim().toLowerCase();

        if (userRepository.existsByUserNameIgnoreCase(userName)) throw new BusinessException("userName already exists");
        if (userRepository.existsByEmailIgnoreCase(email)) throw new BusinessException("email already exists");

        Permission customer = permissionRepository.findByName(PermissionName.CUSTOMER)
                .orElseThrow(() -> new PermissionNameNotFound("Permission CUSTOMER not found"));

        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setFullName(request.getFullName().trim());
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setProviderId(null);
        user.setPermissions(Set.of(customer));

        userRepository.save(user);
    }
}
