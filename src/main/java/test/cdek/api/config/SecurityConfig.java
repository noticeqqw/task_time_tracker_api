package test.cdek.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import test.cdek.api.security.JwtFilter;

// Настраивает правила доступа к эндпоинтам и JWT фильтр для аутентификации
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // JWT
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем CSRF защиту для REST API (используем JWT вместо этого)
                .csrf(AbstractHttpConfigurer::disable)
                // Используем stateless сессии (каждый запрос независим, состояние в JWT)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Настраиваем доступ к эндпоинтам
                .authorizeHttpRequests(auth -> auth
                        // Доступ без аутентификации
                        .requestMatchers("/api/auth/**").permitAll()
                        // Доступ к Swagger UI без аутентификации
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/webjars/**").permitAll()
                        // Все остальные эндпоинты требуют аутентификации
                        .anyRequest().authenticated()
                )
                // Добавляем JWT фильтр перед стандартным фильтром аутентификации
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Создаёт AuthenticationManager для аутентификации по имени и паролю
    // Используется в AuthController для входа
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
