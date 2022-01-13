package ru.notebot.exception;

public class CategoryNotFound extends RuntimeException {
    private final long id;

    public CategoryNotFound(long id) {
        super(String.format("The category is not found, id: %d", id));
        this.id = id;
    }
}
