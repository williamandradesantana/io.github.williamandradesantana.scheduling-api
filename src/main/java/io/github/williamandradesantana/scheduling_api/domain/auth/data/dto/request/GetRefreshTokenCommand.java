package io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.request;

public record GetRefreshTokenCommand(String userName, String refreshToken) {
}
