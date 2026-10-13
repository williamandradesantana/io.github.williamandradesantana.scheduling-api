package io.github.williamandradesantana.scheduling_api.domain.auth.entities;

import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.PermissionName;
import io.github.williamandradesantana.scheduling_api.domain.shared.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PermissionTest {

    private PermissionName customer;
    private PermissionName professional;
    private Permission customerPermission;
    private Permission professionalPermission;
    private Set<Permission> permissions = new HashSet<>();

    @BeforeEach
    void setUp() {
        // Arrange - Given
        customer = PermissionName.CUSTOMER;
        professional = PermissionName.PROFESSIONAL;
        customerPermission = Permission.create(customer);
        professionalPermission = Permission.create(professional);
        permissions.add(customerPermission);
        permissions.add(professionalPermission);
    }

    @Test
    void test_When_PermissionIsCreated_ShouldReturnCorrectValue() {
        assertEquals(customer, customerPermission.getName());
        assertEquals(professional, professionalPermission.getName());
    }

    @Test
    void test_WhenPermissionNameIsNull_ShouldThrowException() {
        String expectedMessage = "Permission name cannot be null";
        DomainException exception = assertThrows(DomainException.class, () -> Permission.create(null));
        assertEquals(expectedMessage, exception.getMessage(), () -> "Exception message not matches");
    }

}