package io.github.williamandradesantana.scheduling_api.domain.auth.repositories;

import io.github.williamandradesantana.scheduling_api.domain.auth.entities.Permission;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.PermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(PermissionName name);
}
