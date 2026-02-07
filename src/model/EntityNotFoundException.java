package model;

// Ошибка, если автобус или пассажир не найден в базе
public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
