package io.github.williamandradesantana.scheduling_api.domain.auth.repositories;

import io.github.williamandradesantana.scheduling_api.domain.auth.entities.User;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("select u from User u where u.userName = :userName")
    User findByUserName(@Param("userName") String userName);

    Optional<User> findByAuthProviderAndProviderId(AuthProvider authProvider, String providerId);
    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByUserNameIgnoreCase(String userName);
    boolean existsByEmailIgnoreCase(String email);
}
