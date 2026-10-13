package io.github.williamandradesantana.scheduling_api.domain.shared.exceptions;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
