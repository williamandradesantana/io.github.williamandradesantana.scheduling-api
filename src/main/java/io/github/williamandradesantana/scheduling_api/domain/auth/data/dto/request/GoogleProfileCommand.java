package io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.request;

public record GoogleProfileCommand(String providerId, String email, String fullName) {
}
