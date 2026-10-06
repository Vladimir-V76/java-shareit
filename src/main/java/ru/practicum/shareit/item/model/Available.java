package ru.practicum.shareit.item.model;

public enum Available {
    BUSY, FREE;

    public static Available from(String available) {
        return switch (available.toLowerCase()) {
            case "busy" -> BUSY;
            case "free" -> FREE;
            default -> null;
        };
    }
}
