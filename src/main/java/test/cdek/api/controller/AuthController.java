package test.cdek.api.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import test.cdek.api.dto.LoginRequest;
import test.cdek.api.dto.LoginResponse;
import test.cdek.api.dto.RegisterRequest;
import test.cdek.api.security.JwtUtil;
import test.cdek.api.service.UserService;

// REST контроллер для аутентификации и авторизации
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtUtil jwtUtil,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    // Регистрирует нового пользователя
    // Метод: POST /api/auth/register
    // Параметры: RegisterRequest с username, email, password, confirmPassword
    // Возвращает: 201 Created если успешно
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
    }

    // Осуществляет вход пользователя и выдает JWT токен
    // Метод: POST /api/auth/login
    // Параметры: LoginRequest с username и password
    // Возвращает: LoginResponse с JWT токеном
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        // Аутентифицируем пользователя по имени и паролю
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Генерируем JWT токен для аутентифицированного пользователя
        String token = jwtUtil.generateToken(auth.getName());

        // Возвращаем токен в ответе
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
