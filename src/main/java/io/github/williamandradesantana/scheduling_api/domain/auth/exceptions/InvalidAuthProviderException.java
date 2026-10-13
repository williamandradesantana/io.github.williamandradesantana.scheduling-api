package io.github.williamandradesantana.scheduling_api.domain.auth.exceptions;

import io.github.williamandradesantana.scheduling_api.domain.shared.exceptions.DomainException;

public class InvalidAuthProviderException extends DomainException {
    public InvalidAuthProviderException(String message) {
        super(message);
    }
}
