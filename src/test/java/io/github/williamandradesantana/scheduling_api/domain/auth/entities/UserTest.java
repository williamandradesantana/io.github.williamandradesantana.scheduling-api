package io.github.williamandradesantana.scheduling_api.domain.auth.entities;

import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.AuthProvider;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.PermissionName;
import io.github.williamandradesantana.scheduling_api.domain.auth.exceptions.*;
import io.github.williamandradesantana.scheduling_api.domain.shared.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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
    @DisplayName("Test: when valid user is created should return all values correctly")
    void test_When_ValidUserIsCreated_ReturnAllValuesCorrected() {
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
    @DisplayName("Test: should return authorities")
    void test_ShouldReturnAuthorities() {
        assertNotNull(user.getAuthorities());

        assertTrue(
            user.getAuthorities().stream().anyMatch(
                authority -> Objects.equals(authority.getAuthority(), PermissionName.CUSTOMER.name())
            )
        );
    }

    @Test
    @DisplayName("Test: when userName is null should return exception")
    void test_WhenUserNameIsNull_ShouldReturnException() {
        assertThrows(InvalidUserNameException.class, () -> User.create(
            null, user.getEmail(), user.getFullName(), user.getPassword(),
            user.getAuthProvider(), user.getProviderId(), user.getPermissions()
        ));
    }

    @Test
    @DisplayName("Test: when userName is blank should return exception")
    void test_WhenUserNameIsBlank_ShouldReturnException() {
        assertThrows(InvalidUserNameException.class, () -> User.create(
            " ", user.getEmail(), user.getFullName(), user.getPassword(),
                user.getAuthProvider(), user.getProviderId(), user.getPermissions()
        ));
    }

    @Test
    @DisplayName("Test: when email is null should return exception")
    void test_WhenEmailIsNull_ShouldReturnException() {
        assertThrows(InvalidEmailException.class, () -> User.create(
            user.getUserName(), null, user.getFullName(), user.getPassword(),
            user.getAuthProvider(), user.getProviderId(), user.getPermissions()
        ));
    }

    @Test
    @DisplayName("Test: when email is blank should return exception")
    void test_WhenEmailIsBlank_ShouldReturnException() {
        assertThrows(InvalidEmailException.class, () -> User.create(
            user.getUserName(), " ", user.getFullName(), user.getPassword(),
            user.getAuthProvider(), user.getProviderId(), user.getPermissions()
        ));
    }

    @Test
    @DisplayName("Test: when fullName is null should return exception")
    void test_WhenFullNameIsNull_ShouldReturnException() {
        assertThrows(InvalidFullNameException.class, () -> User.create(
            user.getUserName(), user.getEmail(), null, user.getPassword(),
            user.getAuthProvider(), user.getProviderId(), user.getPermissions()
        ));
    }

    @Test
    @DisplayName("Test: when fullName is blank should return exception")
    void test_WhenFullNameIsBlank_ShouldReturnException() {
        assertThrows(InvalidFullNameException.class, () -> User.create(
            user.getUserName(), user.getEmail(), " ", user.getPassword(),
                user.getAuthProvider(), user.getProviderId(), user.getPermissions()
        ));
    }

    @Test
    @DisplayName("Test: when auth provider is null, should throw exception")
    void test_WhenAuthProviderIsNull_ShouldThrowException() {
        String expectedMessage = "authProvider cannot be null";
        InvalidAuthProviderException exception = assertThrows(
            InvalidAuthProviderException.class, () -> User.create(
                user.getUserName(), user.getEmail(), user.getFullName(), user.getPassword(),
                null, user.getProviderId(), user.getPermissions()
            )
        );
        assertEquals(expectedMessage, exception.getMessage(), () -> "The message not matches");
    }

    @Test
    @DisplayName("Test: when permissions is null should throw exception")
    void test_WhenPermissionsIsNull_ShouldThrowException() {
        String expectedMessage = "a user needs at least one permission";
        InvalidPermissionsException exception = assertThrows(
            InvalidPermissionsException.class, () -> User.create(
                user.getUserName(), user.getEmail(), user.getFullName(), user.getPassword(),
                user.getAuthProvider(), user.getProviderId(), null
            )
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Test: when permissions is empty should throw exception")
    void test_WhenPermissionsIsEmpty_ShouldThrowException() {
        String expectedMessage = "a user needs at least one permission";
        InvalidPermissionsException exception = assertThrows(
            InvalidPermissionsException.class, () -> User.create(
                user.getUserName(), user.getEmail(), user.getFullName(), user.getPassword(),
                user.getAuthProvider(), user.getProviderId(),  new HashSet<>()
            )
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Test: when local user password is null should throw exception")
    void test_WhenLocalUserPasswordIsNull_ShouldThrowException() {
        String expectedMessage = "LOCAL users require a password";
        DomainException exception = assertThrows(
            DomainException.class, () -> User.create(
                user.getUserName(), user.getEmail(), user.getFullName(), null,
                user.getAuthProvider(), user.getProviderId(), user.getPermissions()
            )
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Test: when local user password is blank should throw exception")
    void test_WhenLocalUserPasswordIsBlank_ShouldThrowException() {
        String expectedMessage = "LOCAL users require a password";
        DomainException exception = assertThrows(
            DomainException.class, () -> User.create(
                user.getUserName(), user.getEmail(), user.getFullName(), "",
                user.getAuthProvider(), user.getProviderId(), user.getPermissions()
            )
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Test: when local user has provider id, should throw exception")
    void test_WhenLocalUserHasProviderId_ShouldThrowException() {
        String expectedMessage = "LOCAL users cannot have a providerId";
        DomainException exception = assertThrows(
            DomainException.class, () -> User.create(
                user.getUserName(), user.getEmail(), user.getFullName(), user.getPassword(),
                user.getAuthProvider(), "google-123", user.getPermissions()
            )
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Test: when google user is created should return all values correctly")
    void test_WhenGoogleUserIsCreatedShouldReturnAllValuesCorrectly() {
        User googleUser = User.create(
            "google_user", "google@email.com", "Google User", null,
            AuthProvider.GOOGLE, "google-123", permissions
        );

        assertEquals("google_user", googleUser.getUserName(), () -> "The userName not matches");
        assertEquals("google@email.com", googleUser.getEmail(), () -> "The email not matches");
        assertEquals("Google User", googleUser.getFullName(), () -> "The fullName not matches");
        assertEquals(AuthProvider.GOOGLE, googleUser.getAuthProvider(), () -> "The auth provider not matches");
        assertEquals("google-123", googleUser.getProviderId(), () -> "The provider id not matches");
        assertNull(googleUser.getPassword(), () -> "The password must be null");
    }

    @Test
    @DisplayName("Test: when google user provider id is null should throw exception")
    void test_WhenGoogleUserProviderIdIsNull_ShouldThrowException() {
        String expectedMessage = "GOOGLE users require a providerId";
        user.setPassword(null);
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setProviderId(null);

        DomainException exception = assertThrows(
            DomainException.class, () -> User.create(
                user.getUserName(), user.getEmail(), user.getFullName(), user.getPassword(),
                user.getAuthProvider(), user.getProviderId(), user.getPermissions()
            )
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Test: when google user provider id is blank should throw exception")
    void test_WhenGoogleUserProviderIdIsBlank_ShouldThrowException() {
        String expectedMessage = "GOOGLE users require a providerId";
        user.setPassword(null);
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setProviderId(" ");

        DomainException exception = assertThrows(
            DomainException.class, () -> User.create(
                user.getUserName(), user.getEmail(), user.getFullName(), user.getPassword(),
                user.getAuthProvider(), user.getProviderId(), user.getPermissions()
            )
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Test: when google user has password should throw exception")
    void test_WhenGoogleUserHasPassword_ShouldThrowException() {
        String expectedMessage = "GOOGLE users cannot have a password";
        user.setPassword("password");
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setProviderId("google-123");

        DomainException exception = assertThrows(
            DomainException.class, () -> User.create(
                user.getUserName(), user.getEmail(), user.getFullName(), user.getPassword(),
                user.getAuthProvider(), user.getProviderId(), user.getPermissions()
            )
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void test_WhenUserIsCreated_ShouldTrimAndNormalizeFields() {
        User normalized = User.create(
                "  user_name  ", "  EMAIL@Email.COM  ", "  Full Name  ", "password",
                AuthProvider.LOCAL, null, permissions
        );

        assertEquals("user_name", normalized.getUserName(), () -> "The userName must be trimmed");
        assertEquals("email@email.com", normalized.getEmail(), () -> "The email must be trimmed and lowercased");
        assertEquals("Full Name", normalized.getFullName(), () -> "The fullName must be trimmed");
    }

    @Test
    void test_WhenUserIsCreated_ShouldSetDefaultValues() {
        assertNull(user.getId(), () -> "The id must be generated by the database");
        assertTrue(user.getAccountNonExpired(), () -> "accountNonExpired must default to true");
        assertTrue(user.getAccountNonLocked(), () -> "accountNonLocked must default to true");
        assertTrue(user.getCredentialsNonExpired(), () -> "credentialsNonExpired must default to true");
        assertTrue(user.getEnabled(), () -> "enabled must default to true");
        assertNotNull(user.getCreatedAt(), () -> "createdAt must be set");
        assertNotNull(user.getUpdatedAt(), () -> "updatedAt must be set");
        assertEquals(user.getCreatedAt(), user.getUpdatedAt(), () -> "createdAt and updatedAt must start equal");
    }

    @Test
    void test_WhenUserIsCreated_ShouldCopyPermissionsDefensively() {
        Set<Permission> original = new HashSet<>();
        original.add(Permission.create(PermissionName.CUSTOMER));

        User created = User.create(
                "user", "email@email.com", "fullName", "password",
                AuthProvider.LOCAL, null, original
        );
        original.add(Permission.create(PermissionName.ADMIN));

        assertEquals(1, created.getPermissions().size(), () -> "Changing the original set must not affect the user");
        assertFalse(created.getRoles().contains(PermissionName.ADMIN));
    }

    @Test
    void test_WhenComparedWithNull_ShouldNotBeEqual() {
        assertNotEquals(null, user);
    }

    @Test
    void test_WhenComparedWithAnotherClass_ShouldNotBeEqual() {
        assertNotEquals("not a user", user);
    }
}