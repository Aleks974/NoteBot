package ru.notebot.service;

import ru.notebot.api.request.CategoryRequest;
import ru.notebot.api.request.LinkRequest;
import ru.notebot.model.Category;
import ru.notebot.model.Link;

import java.net.URI;
import java.util.Optional;

public interface ILinkService {
    Link getLink(long id);

    Link[] getAllLinks();

    URI newLink(LinkRequest linkRequest);

    void updateLink(long id, LinkRequest linkRequest);

    void deleteLink(long id);

    Category[] getAllCategories();

    Category getCategory(long id);

    URI newCategory(CategoryRequest request);

    void deleteCategory(long id);
}
