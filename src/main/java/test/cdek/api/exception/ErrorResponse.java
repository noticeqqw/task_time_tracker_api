package test.cdek.api.exception;

import java.time.LocalDateTime;

// DTO для отправки информации об ошибке клиенту
public class ErrorResponse {

    // HTTP статус код ошибки
    private int status;

    // Сообщение об ошибке
    private String message;

    // Время возникновения ошибки
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
