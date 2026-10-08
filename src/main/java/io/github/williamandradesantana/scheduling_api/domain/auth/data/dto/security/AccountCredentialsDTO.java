package io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class AccountCredentialsDTO {

    @NotBlank(message = "userName cannot be blank")
    @Size(min = 3, max = 20, message = "The userName must be between 3 and 20 characters")
    private String userName;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 255, message = "The password needs to be at least 6 characters")
    private String password;

    public AccountCredentialsDTO() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AccountCredentialsDTO that = (AccountCredentialsDTO) o;
        return Objects.equals(userName, that.userName) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(userName);
        result = 31 * result + Objects.hashCode(password);
        return result;
    }
}
