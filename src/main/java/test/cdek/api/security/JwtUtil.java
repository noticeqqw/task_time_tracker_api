package test.cdek.api.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // Секретный ключ для подписи токенов (из application.properties)
    @Value("${app.jwt.secret}")
    private String secret;

    // Время жизни токена в миллисекундах (из application.properties)
    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Генерирует новый JWT токен для пользователя
    // Параметры: username - имя пользователя (будет записано в subject)
    // Возвращает: подписанный JWT токен в виде строки
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getKey())
                .compact();
    }

    // Извлекает имя пользователя из JWT токена
    // Параметры: token - JWT токен
    // Возвращает: имя пользователя из поля subject
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Проверяет валидность JWT токена
    // Параметры: token - JWT токен для проверки
    // Возвращает: true если токен действительный и не истёк, false иначе
    public boolean isValid(String token) {
        try {
            extractUsername(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
