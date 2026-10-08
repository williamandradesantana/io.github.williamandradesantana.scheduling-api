package io.github.williamandradesantana.scheduling_api.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.TokenDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.PermissionName;
import io.github.williamandradesantana.scheduling_api.domain.auth.exceptions.InvalidJwtAuthenticationException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.Instant;
import java.util.*;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.expiration-milliseconds}")
    private long validityInMilliseconds = 3600000;

    @Autowired
    private UserDetailsService userDetailsService;

    Algorithm algorithm = null;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public TokenDTO createAccessToken(String userName, Set<PermissionName> roles) {
        Instant now = Instant.now();
        Instant validity = now.plusMillis(validityInMilliseconds);
        String accessToken = getAccessToken(userName, roles, now, validity);
        String refreshToken = getRefreshToken(userName, roles, now);
        return new TokenDTO(userName, true, now, validity, accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken) {
        if (refreshTokenContainsBearer(refreshToken))
            refreshToken = refreshToken.substring("Bearer ".length());

        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);

        String username = decodedJWT.getSubject();
        Set<PermissionName> roles = new HashSet<>(decodedJWT.getClaim("roles").asList(PermissionName.class));
        return createAccessToken(username, roles);
    }

    private String getRefreshToken(String userName, Set<PermissionName> roles, Instant now) {
        Instant refreshTokenValidity = Instant.ofEpochMilli(now.toEpochMilli() + validityInMilliseconds * 3);
        List<String> rolesName = roles.stream().map(Enum::name).toList();

        return JWT.create()
                .withClaim("roles", rolesName)
                .withIssuedAt(now)
                .withExpiresAt(refreshTokenValidity)
                .withSubject(userName)
                .sign(algorithm);
    }

    private String getAccessToken(String userName, Set<PermissionName> roles, Instant now, Instant validity) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        List<String> rolesName = roles.stream().map(Enum::name).toList();

        return JWT.create()
                .withClaim("roles", rolesName)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(userName)
                .withIssuer(issuerUrl)
                .sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (refreshTokenContainsBearer(bearerToken)) return bearerToken.substring("Bearer ".length());
        return null;
    }

    private boolean refreshTokenContainsBearer(String refreshToken) {
        return StringUtils.isNotBlank(refreshToken) && refreshToken.startsWith("Bearer ");
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);

        try {
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false;
            }
            return true;
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token!");
        }
    }
}
