package ru.notebot.exception;

public class LinkNotFound extends RuntimeException {
    private final long id;

    public LinkNotFound(long id) {
        super(String.format("The link is not found, id: %d", id));
        this.id = id;
    }
}
