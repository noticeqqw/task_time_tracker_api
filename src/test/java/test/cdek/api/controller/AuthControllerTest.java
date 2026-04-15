package test.cdek.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import test.cdek.api.dto.LoginRequest;
import test.cdek.api.dto.LoginResponse;
import test.cdek.api.dto.RegisterRequest;
import test.cdek.api.security.JwtUtil;
import test.cdek.api.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    // Mock менеджера аутентификации
    @Mock
    private AuthenticationManager authenticationManager;

    // Mock утилиты для работы с JWT
    @Mock
    private JwtUtil jwtUtil;

    // Mock сервиса пользователей
    @Mock
    private UserService userService;

    // Тестируемый контроллер (зависимости внедряются через конструктор)
    @InjectMocks
    private AuthController authController;

    // Тест: логин с корректными учётными данными возвращает токен с статусом 200
    @Test
    void login_validCredentials_returnsTokenWith200() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("john");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(jwtUtil.generateToken("john")).thenReturn("jwt_token");

        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("password123");

        ResponseEntity<LoginResponse> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt_token", response.getBody().getToken());
    }

    // Тест: логин с неправильными учётными данными выбрасывает исключение
    @Test
    void login_badCredentials_exceptionPropagates() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("wrong");

        assertThrows(BadCredentialsException.class, () -> authController.login(request));
        // Проверяем что токен не был сгенерирован (не дошли до генерации)
        verify(jwtUtil, never()).generateToken(any());
    }

    // Тест: регистрация передаёт запрос сервису
    @Test
    void register_callsUserService() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password123");

        authController.register(request);

        verify(userService).register(request);
    }

    // Тест: если имя пользователя занято, исключение пробрасывается
    @Test
    void register_usernameTaken_exceptionPropagates() {
        doThrow(new IllegalArgumentException("Username already taken: newuser"))
                .when(userService).register(any());

        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setPassword("password123");

        assertThrows(IllegalArgumentException.class, () -> authController.register(request));
    }
}
