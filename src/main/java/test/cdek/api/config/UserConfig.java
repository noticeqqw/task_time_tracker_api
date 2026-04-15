package test.cdek.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.cdek.api.entity.User;
import test.cdek.api.mapper.UserMapper;

// Настраивает шифрование паролей и загрузку пользователей из БД
@Configuration
public class UserConfig {

    // Создаёт BCrypt для шифрования паролей
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Создаёт сервис для загрузки пользователей из БД по имени
    // Используется Spring Security для аутентификации
    @Bean
    public UserDetailsService userDetailsService(UserMapper userMapper) {
        return username -> {
            // Ищем пользователя в БД по имени
            User user = userMapper.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            // Возвращаем объект Spring Security UserDetails
            // Spring Security сам сравнит введённый пароль с зашифрованным
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
        };
    }
}
