package io.github.williamandradesantana.scheduling_api.domain.auth.exceptions;

import io.github.williamandradesantana.scheduling_api.domain.shared.exceptions.DomainException;

public class InvalidFullNameException extends DomainException {
    public InvalidFullNameException(String message) {
        super(message);
    }
}
