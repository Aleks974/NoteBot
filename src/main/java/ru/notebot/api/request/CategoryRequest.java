package ru.notebot.api.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Set;

@Getter
public class CategoryRequest {
    private final String name;

    @JsonCreator
    public CategoryRequest(@JsonProperty("name") String name) {
        this.name = name;
    }
}
