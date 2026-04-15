package test.cdek.api.dto;

import jakarta.validation.constraints.NotBlank;

// DTO для запроса входа пользователя
public class LoginRequest {

    // Имя пользователя
    @NotBlank(message = "Username is required")
    private String username;

    // Пароль пользователя
    @NotBlank(message = "Password is required")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
