package ru.notebot.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Category {
    private final Long id;
    private final String name;

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
