package test.cdek.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import test.cdek.api.dto.RegisterRequest;
import test.cdek.api.entity.User;
import test.cdek.api.mapper.UserMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_newUser_insertsWithEncodedPassword() {
        when(userMapper.findByUsername("john")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");

        userService.register(buildRequest("john", "john@example.com", "password123", "password123"));

        verify(passwordEncoder).encode("password123");
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void register_usernameTaken_throwsIllegalArgumentException() {
        User existing = new User();
        existing.setUsername("john");
        when(userMapper.findByUsername("john")).thenReturn(existing);

        assertThrows(IllegalArgumentException.class,
                () -> userService.register(buildRequest("john", "john@example.com", "password123", "password123")));
        verify(userMapper, never()).insert(any());
    }

    @Test
    void register_passwordsDoNotMatch_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> userService.register(buildRequest("john", "john@example.com", "password123", "different")));
        verify(userMapper, never()).findByUsername(any());
        verify(userMapper, never()).insert(any());
    }

    @Test
    void register_passwordsDoNotMatch_errorMessageIsCorrect() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.register(buildRequest("john", "john@example.com", "password123", "different")));

        assertEquals("Passwords do not match", ex.getMessage());
    }

    @Test
    void register_usernameTaken_errorMessageContainsUsername() {
        User existing = new User();
        existing.setUsername("john");
        when(userMapper.findByUsername("john")).thenReturn(existing);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> userService.register(buildRequest("john", "john@example.com", "password123", "password123")));

        assertTrue(ex.getMessage().contains("john"));
    }

    private RegisterRequest buildRequest(String username, String email,
                                         String password, String confirmPassword) {
        RegisterRequest request = new RegisterRequest();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);
        return request;
    }
}
