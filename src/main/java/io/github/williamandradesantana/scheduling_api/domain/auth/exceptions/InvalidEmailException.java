package io.github.williamandradesantana.scheduling_api.domain.auth.exceptions;

import io.github.williamandradesantana.scheduling_api.domain.shared.exceptions.DomainException;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException(String message) {
        super(message);
    }
}
