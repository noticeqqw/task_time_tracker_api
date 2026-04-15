package test.cdek.api.exception;

// Исключение, которое выбрасывается когда ресурс (задача, пользователь) не найден
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
