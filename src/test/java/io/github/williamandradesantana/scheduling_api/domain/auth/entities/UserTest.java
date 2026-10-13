package io.github.williamandradesantana.scheduling_api.domain.auth.entities;

import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.AuthProvider;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.PermissionName;
import io.github.williamandradesantana.scheduling_api.domain.shared.exceptions.DomainException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class UserTest {

    private User user;
    private PermissionName permissionName;
    private Permission permission;
    private Set<Permission> permissions = new HashSet<>();

    @BeforeEach
    void setUp() {
        // Arrange - Given
        permissionName = PermissionName.CUSTOMER;
        permission = Permission.create(permissionName);
        permissions.add(permission);
        user = User.create(
             "user_name", "email@email.com", "fullName", "password",
            AuthProvider.LOCAL, null, permissions
        );
    }

    @Test
    void test_When_ValidUserIsCreated_ReturnAllValuesCorrected () {
        String expectedUserName = "user_name";
        String expectedEmail = "email@email.com";
        String expectedFullName = "fullName";
        String expectedPassword = "password";
        AuthProvider expectedAuthProvider = AuthProvider.LOCAL;


        // Then - Assert
        assertEquals(expectedUserName, user.getUserName(), () -> "The userName not matches");
        assertEquals(expectedEmail, user.getEmail(), () -> "The email not matches");
        assertEquals(expectedFullName, user.getFullName(), () -> "The fullName not matches");
        assertEquals(expectedPassword, user.getPassword(), () -> "The password not matches");
        assertEquals(expectedAuthProvider, user.getAuthProvider(), () -> "The authProvider not matches");
        assertTrue(user.getRoles().contains(permission.getName()));
        assertNull(user.getProviderId(), () -> "The providerId must be null");
    }

    @Test
    void test_ShouldReturnAuthorities() {
        assertNotNull(user.getAuthorities());

        assertTrue(
            user.getAuthorities().stream().anyMatch(
                authority -> Objects.equals(authority.getAuthority(), PermissionName.CUSTOMER.name())
            )
        );
    }

    @Test
    void test_WhenUserNameIsNull_ShouldReturnException() {
        assertThrows(DomainException.class, () -> User.create(
            null, "email@email.com", "fullName", "password",
            AuthProvider.LOCAL, null, permissions
        ));
    }

    @Test
    void test_WhenUserNameIsBlank_ShouldReturnException() {
        assertThrows(DomainException.class, () -> User.create(
            " ", "email@email.com", "fullName", "password",
            AuthProvider.LOCAL, null, permissions
        ));
    }

    @Test
    void test_WhenEmailIsNull_ShouldReturnException() {
        assertThrows(DomainException.class, () -> User.create(
            "user", null, "fullName", "password",
            AuthProvider.LOCAL, null, permissions
        ));
    }

    @Test
    void test_WhenEmailIsBlank_ShouldReturnException() {
        assertThrows(DomainException.class, () -> User.create(
            "user", " ", "fullName", "password",
            AuthProvider.LOCAL, null, permissions
        ));
    }

    @Test
    void test_WhenFullNameIsNull_ShouldReturnException() {
        assertThrows(DomainException.class, () -> User.create(
            "user", "email@email.com", null, "password",
            AuthProvider.LOCAL, null, permissions
        ));
    }

    @Test
    void test_WhenFullNameIsBlank_ShouldReturnException() {
        assertThrows(DomainException.class, () -> User.create(
            "user", "email@email.com", " ", "password",
            AuthProvider.LOCAL, null, permissions
        ));
    }
}