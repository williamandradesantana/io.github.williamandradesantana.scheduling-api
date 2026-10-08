package io.github.williamandradesantana.scheduling_api.domain.auth.entities;

import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.AuthProvider;
import io.github.williamandradesantana.scheduling_api.domain.auth.entities.enums.PermissionName;
import jakarta.persistence.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "tb_users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_name", nullable = false, length = 20, unique = true)
    private String userName;

    @Column(name = "email", nullable = false, length = 160, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider", nullable = false)
    private AuthProvider authProvider;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "account_non_expired", nullable = false)
    private Boolean accountNonExpired;

    @Column(name = "account_non_locked", nullable = false)
    private Boolean accountNonLocked;

    @Column(name = "credentials_non_expired", nullable = false)
    private Boolean credentialsNonExpired;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tb_user_permissions",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    public User() {}

    public User(UUID id, String userName, String email, String fullName, String password, AuthProvider authProvider, String providerId, Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired, Boolean enabled, Instant createdAt, Instant updatedAt, Set<Permission> permissions) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.authProvider = authProvider;
        this.providerId = providerId;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.permissions = permissions != null ? new HashSet<>(permissions) : new HashSet<>();
    }

    public Set<PermissionName> getRoles() {
        Set<PermissionName> roles = new HashSet<>();

        for (Permission permission : permissions)
            roles.add(permission.getName());

        return roles;
    }

    @PrePersist
    void prePersist() {
        if (this.accountNonExpired == null) this.accountNonExpired = true;
        if (this.accountNonLocked == null) this.accountNonLocked = true;
        if (this.credentialsNonExpired == null) this.credentialsNonExpired = true;
        if (this.enabled == null) this.enabled = true;
        if (this.createdAt == null) this.createdAt = Instant.now();
        if (this.updatedAt == null) this.updatedAt = Instant.now();
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = Instant.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    @Override
    public @Nullable String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(userName, user.userName) && Objects.equals(email, user.email) && Objects.equals(fullName, user.fullName) && Objects.equals(password, user.password) && authProvider == user.authProvider && Objects.equals(providerId, user.providerId) && Objects.equals(accountNonExpired, user.accountNonExpired) && Objects.equals(accountNonLocked, user.accountNonLocked) && Objects.equals(credentialsNonExpired, user.credentialsNonExpired) && Objects.equals(enabled, user.enabled) && Objects.equals(createdAt, user.createdAt) && Objects.equals(updatedAt, user.updatedAt) && Objects.equals(permissions, user.permissions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(userName);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(fullName);
        result = 31 * result + Objects.hashCode(password);
        result = 31 * result + Objects.hashCode(authProvider);
        result = 31 * result + Objects.hashCode(providerId);
        result = 31 * result + Objects.hashCode(accountNonExpired);
        result = 31 * result + Objects.hashCode(accountNonLocked);
        result = 31 * result + Objects.hashCode(credentialsNonExpired);
        result = 31 * result + Objects.hashCode(enabled);
        result = 31 * result + Objects.hashCode(createdAt);
        result = 31 * result + Objects.hashCode(updatedAt);
        result = 31 * result + Objects.hashCode(permissions);
        return result;
    }
}
