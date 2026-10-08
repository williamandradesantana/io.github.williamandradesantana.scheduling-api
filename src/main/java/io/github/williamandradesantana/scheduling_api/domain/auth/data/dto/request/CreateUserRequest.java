package io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class CreateUserRequest {

    @NotBlank(message = "userName cannot be blank")
    @Size(min = 3, max = 20, message = "The userName must be between 3 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "userName may only contain letters, numbers, '.', '_' and '-'")
    private String userName;

    @NotBlank(message = "fullName cannot be blank")
    @Size(min = 3, max = 255, message = "The fullName must be between 3 and 255 characters")
    private String fullName;

    @NotBlank(message = "email cannot be blank")
    @Email(message = "Invalid email format")
    @Size(max = 160, message = "The email must have at most 160 characters")
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 255, message = "The password needs to be at least 6 characters")
    private String password;

    public CreateUserRequest(){}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        CreateUserRequest that = (CreateUserRequest) o;
        return Objects.equals(userName, that.userName) && Objects.equals(fullName, that.fullName) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(userName);
        result = 31 * result + Objects.hashCode(fullName);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(password);
        return result;
    }
}
