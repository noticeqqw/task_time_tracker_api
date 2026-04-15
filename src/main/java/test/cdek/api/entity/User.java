package test.cdek.api.entity;

// Сущность пользователя, представляет строку в таблице users
public class User {

    // Уникальный идентификатор пользователя
    private Long id;

    // Имя пользователя для входа
    private String username;

    // Электронная почта пользователя
    private String email;

    // Зашифрованный пароль пользователя
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
