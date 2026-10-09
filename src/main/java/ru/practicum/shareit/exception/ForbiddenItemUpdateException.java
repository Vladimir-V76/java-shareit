package ru.practicum.shareit.exception;

public class ForbiddenItemUpdateException extends RuntimeException {
    public ForbiddenItemUpdateException(String message) {
        super(message);
    }
}
