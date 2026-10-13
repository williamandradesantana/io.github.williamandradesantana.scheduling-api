package io.github.williamandradesantana.scheduling_api.domain.auth.services;

import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.request.GoogleProfileCommand;
import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.TokenDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.Permission;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.User;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.AuthProvider;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.PermissionName;
import io.github.williamandradesantana.scheduling_api.domain.auth.exceptions.PermissionNameNotFound;
import io.github.williamandradesantana.scheduling_api.domain.auth.repositories.PermissionRepository;
import io.github.williamandradesantana.scheduling_api.domain.auth.repositories.UserRepository;
import io.github.williamandradesantana.scheduling_api.exceptions.BusinessException;
import io.github.williamandradesantana.scheduling_api.security.jwt.JwtTokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class GoogleAuthServices {

    private static final int USER_NAME_MAX_LENGTH = 20;

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final JwtTokenProvider tokenProvider;

    public GoogleAuthServices(
            UserRepository userRepository,
            PermissionRepository permissionRepository,
            JwtTokenProvider tokenProvider
    ) {
        this.userRepository = userRepository;
        this.permissionRepository = permissionRepository;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public User resolveUser(GoogleProfileCommand command) {
        String email = command.email().trim().toLowerCase();

        User user = userRepository.findByAuthProviderAndProviderId(AuthProvider.GOOGLE, command.providerId())
                .or(() -> userRepository.findByEmailIgnoreCase(email)
                        .map(existing -> linkToGoogle(existing, command.providerId())))
                .orElseGet(() -> registerNewGoogleUser(command.providerId(), email, command.fullName()));

        // No login LOCAL isso é validado pelo AuthenticationManager; aqui precisa ser manual.
        if (!user.isEnabled() || !user.isAccountNonLocked() || !user.isAccountNonExpired()) {
            throw new BusinessException("account_disabled");
        }
        return user;
    }

    public TokenDTO generateTokenFor(User user) {
        return tokenProvider.createAccessToken(user.getUsername(), user.getRoles());
    }

    private User linkToGoogle(User existing, String providerId) {
        if (existing.getAuthProvider() == AuthProvider.GOOGLE && existing.getProviderId() != null) {
            throw new BusinessException("email_already_registered");
        }

        existing.setAuthProvider(AuthProvider.GOOGLE);
        existing.setProviderId(providerId);
        existing.setPassword(null);
        return userRepository.save(existing);
    }

    private User registerNewGoogleUser(String providerId, String email, String fullName) {
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new BusinessException("email_already_registered");
        }

        Permission customer = permissionRepository.findByName(PermissionName.CUSTOMER)
                .orElseThrow(() -> new PermissionNameNotFound("Permission CUSTOMER not found"));

        String userName = generateUniqueUserName(email);

        User user = User.create(userName, email, fullName, null, AuthProvider.GOOGLE, providerId, Set.of(customer));
        return userRepository.save(user);
    }

    private String generateUniqueUserName(String email) {
        String base = email.substring(0, email.indexOf('@')).replaceAll("[^a-zA-Z0-9._-]", "");
        if (base.length() < 3) base = (base + "user").substring(0, 4);
        base = base.substring(0, Math.min(base.length(), USER_NAME_MAX_LENGTH));

        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUserNameIgnoreCase(candidate)) {
            String s = String.valueOf(suffix++);
            candidate = base.substring(0, Math.min(base.length(), USER_NAME_MAX_LENGTH - s.length())) + s;
        }
        return candidate;
    }
}