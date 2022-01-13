package ru.notebot.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Set;

@Getter
public class LinkRequest {
    private final String uri;
    private final long catId;
    private final Set<String> tags;
    private final String comment;

    @JsonCreator
    public LinkRequest(@JsonProperty("uri") String uri,
                       @JsonProperty("cat_id") long catId,
                       @JsonProperty("tags") Set<String> tags,
                       @JsonProperty("comment")  String comment) {
        this.uri = uri;
        this.catId = catId;
        this.tags = tags;
        this.comment = comment;
    }
}
