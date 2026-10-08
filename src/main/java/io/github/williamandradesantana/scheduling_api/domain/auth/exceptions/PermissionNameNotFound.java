package io.github.williamandradesantana.scheduling_api.domain.auth.exceptions;

public class PermissionNameNotFound extends RuntimeException {
    public PermissionNameNotFound(String message) {
        super(message);
    }
}
