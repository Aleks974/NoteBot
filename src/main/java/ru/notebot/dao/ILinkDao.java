package ru.notebot.dao;

import ru.notebot.api.request.CategoryRequest;
import ru.notebot.api.request.LinkRequest;
import ru.notebot.model.Category;
import ru.notebot.model.Link;

import java.util.Optional;
import java.util.Set;

public interface ILinkDao {
    Optional<Link> findLinkById(long id);

    Link[] findAllLinks();

    Optional<Long> findLinkId(long categoryId, String link);

    long saveNewLink(LinkRequest linkRequest);

    void updateCatId(long linkId, long categoryIdNew);

    void updateTags(long linkId, Set<String> tagsNew);

    void updateComment(long linkId, String commentNew);

    void deleteLink(long id);

    Category[] getAllCategories();

    Optional<Category> findCategoryById(long id);

    Optional<Category> findCategoryByName(String catName);

    long saveNewCategory(CategoryRequest request);

    void deleteCategory(long id);
}
