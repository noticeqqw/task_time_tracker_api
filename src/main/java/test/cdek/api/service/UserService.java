package test.cdek.api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import test.cdek.api.dto.RegisterRequest;
import test.cdek.api.entity.User;
import test.cdek.api.mapper.UserMapper;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Регистрирует нового пользователя
    // Проверяет что пароли совпадают и что имя пользователя не занято
    // Затем зашифровывает пароль и сохраняет пользователя в БД
    public void register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new IllegalArgumentException("Username already taken: " + request.getUsername());
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userMapper.insert(user);
    }
}
