package io.github.williamandradesantana.scheduling_api.domain.auth.repositories;

import io.github.williamandradesantana.scheduling_api.domain.auth.entities.Permission;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.User;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.AuthProvider;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.PermissionName;
import io.github.williamandradesantana.scheduling_api.exceptions.ResourceNotFoundException;
import io.github.williamandradesantana.scheduling_api.integrationtests.PostgresIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest extends PostgresIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    private PermissionName permissionName;
    private Permission permission;
    private Set<Permission> permissions;
    private User user;

    @BeforeEach
    void setUp() {
        permissionName = PermissionName.CUSTOMER;
        permission = permissionRepository.findByName(permissionName)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found"));
        permissions = new HashSet<>(Set.of(permission));

        user = User.create(
            "username-test", "email@email.com", "fullName-test",
            "password", AuthProvider.LOCAL, null, permissions
        );
    }

    @Test
    @DisplayName("Test: when a user is saved, should return all correct values")
    void test_WhenUserIsSaved_ShouldReturnAllCorrectValues() {
        String expectedUserName = user.getUserName();
        String expectedEmail = user.getEmail();
        String expectedFullName = user.getFullName();
        String expectedPassword = user.getPassword();

        userRepository.save(user);
        User savedUser = userRepository.findByUserName(expectedUserName);

        assertNotNull(user.getId(), () -> "The id cannot be null");
        assertEquals(expectedUserName, savedUser.getUserName(), () -> "The userName not matches");
        assertEquals(expectedEmail, savedUser.getEmail(), () -> "The email not matches");
        assertEquals(expectedFullName, savedUser.getFullName(), () -> "The fullName not matches");
        assertEquals(expectedPassword, savedUser.getPassword(), () -> "The password not matches");
        assertTrue(savedUser.getRoles().contains(permission.getName()), () -> "The role not matches");
    }

    @Test
    @DisplayName("Test: when userName is null, should return exception")
    void test_WhenUserNameIsNull_ShouldReturnException() {
        user.setUserName(null);

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(user));
    }

    @Test
    @DisplayName("Test: when userName is duplicated (case-insensitive), should throw exception")
    void test_WhenUserNameIsDuplicated_ShouldThrowException() {
        userRepository.saveAndFlush(user);

        User duplicated = User.create(
                user.getUserName().toUpperCase(), "other@email.com", "other",
                "password", AuthProvider.LOCAL, null, new HashSet<>(permissions)
        );

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(duplicated));
    }

    @Test
    @DisplayName("Test: when email is duplicated, should throw exception")
    void test_WhenEmailIsDuplicated_ShouldThrowException() {
        userRepository.saveAndFlush(user);

        User duplicated = User.create(
                "other-user", user.getEmail().toUpperCase(), "other",
                "password", AuthProvider.LOCAL, null, new HashSet<>(permissions)
        );

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(duplicated));
    }

    @Test
    @DisplayName("Test: when userName exceeds 20 chars, should throw exception")
    void test_WhenUserNameIsTooLong_ShouldThrowException() {
        user.setUserName("a".repeat(21));

        assertThrows(DataIntegrityViolationException.class, () -> userRepository.saveAndFlush(user));
    }

    @Test
    @DisplayName("Test: finding a non-existent should return empty")
    void test_FindingANonExistent_ShouldReturnEmpty() {
        // when - act
        User found = userRepository.findByUserName("does-non-existent");

        // assert - that
        assertNull(found, () -> "The username cannot be present!");
    }
}