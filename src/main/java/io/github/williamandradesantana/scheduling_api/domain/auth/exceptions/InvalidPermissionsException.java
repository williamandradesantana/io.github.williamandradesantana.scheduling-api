package io.github.williamandradesantana.scheduling_api.domain.auth.exceptions;

import io.github.williamandradesantana.scheduling_api.domain.shared.exceptions.DomainException;

public class InvalidPermissionsException extends DomainException {
    public InvalidPermissionsException(String message) {
        super(message);
    }
}
