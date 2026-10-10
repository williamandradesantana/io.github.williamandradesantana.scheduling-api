package io.github.williamandradesantana.scheduling_api.security.oauth2;

import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.request.GoogleProfileCommand;
import io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security.TokenDTO;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.User;
import io.github.williamandradesantana.scheduling_api.domain.auth.services.GoogleAuthServices;
import io.github.williamandradesantana.scheduling_api.exceptions.BusinessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final GoogleAuthServices googleAuthServices;
    private final String authorizedRedirectUri;

    public OAuth2LoginSuccessHandler(GoogleAuthServices googleAuthServices, String authorizedRedirectUri) {
        this.googleAuthServices = googleAuthServices;
        this.authorizedRedirectUri = authorizedRedirectUri;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Boolean emailVerified = oAuth2User.getAttribute("email_verified");
        String email = oAuth2User.getAttribute("email");
        String providerId = oAuth2User.getAttribute("sub");
        if (emailVerified == null || !emailVerified || email == null || providerId == null) {
            redirectWithError(request, response, "email_not_verified");
            return;
        }

        try {
            String name = oAuth2User.getAttribute("name");
            User user = googleAuthServices.resolveUser(new GoogleProfileCommand(providerId, email, name));
            TokenDTO token = googleAuthServices.generateTokenFor(user);

            String fragment = "access_token=" + token.getAccessToken()
                    + "&refresh_token=" + token.getRefreshToken()
                    + "&user_name=" + user.getUsername()
                    + "&expires_at=" + token.getExpiration().toEpochMilli();

            getRedirectStrategy()
                .sendRedirect(request, response,
                    UriComponentsBuilder
                            .fromUriString(authorizedRedirectUri).fragment(fragment).build().toUriString());

        } catch (BusinessException e) {
            redirectWithError(request, response, e.getMessage());
        }
    }

    private void redirectWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage) throws IOException {
        getRedirectStrategy()
            .sendRedirect(request, response,
                UriComponentsBuilder.fromUriString(authorizedRedirectUri)
                        .queryParam("error", errorMessage).build().toUriString());
    }
}
