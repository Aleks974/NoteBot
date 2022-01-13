package ru.notebot.exception;

public class CategoryAlreadyExists extends RuntimeException {
    private final long id;
    private final String name;

    public CategoryAlreadyExists(long id, String name) {
        super(String.format("The category has already existed: id: %d, name: %s", id, name));
        this.id = id;
        this.name = name;
    }
}
