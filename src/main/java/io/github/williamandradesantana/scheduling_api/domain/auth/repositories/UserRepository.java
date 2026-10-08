package io.github.williamandradesantana.scheduling_api.domain.auth.repositories;

import io.github.williamandradesantana.scheduling_api.domain.auth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("select u from User u where u.userName = :userName")
    User findByUserName(@Param("userName") String userName);

    boolean existsByUserNameIgnoreCase(String userName);
    boolean existsByEmailIgnoreCase(String email);
}
