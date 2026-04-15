package test.cdek.api.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    // Подготовка к каждому тесту: создание JwtUtil с тестовым секретом
    @BeforeEach
    void setUp() throws Exception {
        jwtUtil = new JwtUtil();

        // Используем reflection для установки секретного ключа
        Field secretField = JwtUtil.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(jwtUtil, "mySecretKeyForJWTAuthenticationTaskTimeTrackerApp2024");

        // Используем reflection для установки времени жизни токена
        Field expirationField = JwtUtil.class.getDeclaredField("expirationMs");
        expirationField.setAccessible(true);
        expirationField.set(jwtUtil, 3600000L);
    }

    // Тест: генерация и извлечение имени пользователя работает правильно
    @Test
    void generateToken_extractUsername_returnsCorrectUsername() {
        String token = jwtUtil.generateToken("testuser");

        assertEquals("testuser", jwtUtil.extractUsername(token));
    }

    // Тест: валидный токен считается корректным
    @Test
    void isValid_validToken_returnsTrue() {
        String token = jwtUtil.generateToken("testuser");

        assertTrue(jwtUtil.isValid(token));
    }

    // Тест: невалидный токен не проходит проверку
    @Test
    void isValid_invalidToken_returnsFalse() {
        assertFalse(jwtUtil.isValid("this.is.not.a.valid.token"));
    }

    // Тест: пустая строка не является валидным токеном
    @Test
    void isValid_emptyString_returnsFalse() {
        assertFalse(jwtUtil.isValid(""));
    }

    // Тест: разные пользователи получают разные токены
    @Test
    void generateToken_differentUsers_differentTokens() {
        String token1 = jwtUtil.generateToken("user1");
        String token2 = jwtUtil.generateToken("user2");

        assertNotEquals(token1, token2);
    }
}
