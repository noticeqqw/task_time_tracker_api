package test.cdek.api.dto;

// DTO для ответа после успешного входа
public class LoginResponse {

    // JWT токен для аутентификации последующих запросов
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
