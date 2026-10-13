package io.github.williamandradesantana.scheduling_api.domain.auth.exceptions;

import io.github.williamandradesantana.scheduling_api.domain.shared.exceptions.DomainException;

public class InvalidUserNameException extends DomainException {
    public InvalidUserNameException(String message) {
        super(message);
    }
}
