package model;

// Второе кастомное исключение для проверки корректности данных
public class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}