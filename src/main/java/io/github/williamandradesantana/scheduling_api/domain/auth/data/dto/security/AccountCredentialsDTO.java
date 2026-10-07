package io.github.williamandradesantana.scheduling_api.domain.auth.data.dto.security;

import java.util.Objects;

public class AccountCredentialsDTO {

    private String userName;
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
