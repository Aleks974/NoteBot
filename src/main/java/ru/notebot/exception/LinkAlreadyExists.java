package ru.notebot.exception;

public class LinkAlreadyExists extends RuntimeException {
    private final long id;
    private final long catId;
    private final String uri;

    public LinkAlreadyExists(long id, long catId, String uri) {
        super(String.format("The Link has already existed in the category: id: %d, catId: %d, uri: %s",
                id, catId, uri));
        this.id = id;
        this.catId = catId;
        this.uri = uri;
    }
}
