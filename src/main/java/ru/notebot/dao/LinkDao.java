package ru.notebot.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.notebot.api.request.CategoryRequest;
import ru.notebot.api.request.LinkRequest;
import ru.notebot.model.Category;
import ru.notebot.model.Link;

import java.sql.PreparedStatement;
import java.util.*;
import java.util.function.Supplier;

@Slf4j
@Repository
public class LinkDao implements ILinkDao {
    private final JdbcTemplate jdbcTemplate;
    private final LinkRowMapper linkRowMapper = new LinkRowMapper();
    private final CategoryRowMapper categoryRowMapper = new CategoryRowMapper();

    private static final String SQL_INSERT_LINK = "INSERT INTO links (cat_id, uri) VALUES (?, ?)";
    private static final String SQL_UPDATE_CATEGORY_ID = "UPDATE links SET cat_id = ? WHERE id = ?";
    private static final String SQL_FIND_ID_BY_CAT_AND_LINK = "SELECT id FROM links WHERE cat_id = ? AND uri = ? LIMIT 1";
    private static final String SQL_FIND_LINK_BY_ID = "SELECT * " +
                                                        "FROM links li " +
                                                        "JOIN categories ca ON li.cat_id = ca.id " +
                                                        "LEFT JOIN links2tags lt ON li.id = lt.link_id " +
                                                        "LEFT JOIN tags t ON lt.tag_id = t.id " +
                                                        "LEFT JOIN comments co ON li.id = co.id " +
                                                        "WHERE li.id = ? ";
    private static final String SQL_FIND_ALL_LINKS = "SELECT * " +
                                                        "FROM links li " +
                                                        "JOIN categories ca ON li.cat_id = ca.id " +
                                                        "LEFT JOIN links2tags lt ON li.id = lt.link_id " +
                                                        "LEFT JOIN tags t ON lt.tag_id = t.id " +
                                                        "LEFT JOIN comments co ON li.id = co.id " +
                                                        "ORDER BY ca.id ASC, li.id ASC";
    private static final String SQL_DELETE_TAGS_MAPPING_BY_ID = "DELETE FROM links2tags WHERE link_id = ?";
    private static final String SQL_INSERT_TAG = "INSERT INTO tags (tag_name) VALUES (?)";
    private static final String SQL_INSERT_TAG_MAPPING = "INSERT INTO links2tags (link_id, tag_id) VALUES (?, ?)";
    private static final String SQL_DELETE_COMMENT_BY_ID = "DELETE FROM comments WHERE link_id = ?";
    private static final String SQL_INSERT_COMMENT = "INSERT INTO comments (id, comment) VALUES (?, ?)";
    private static final String SQL_DELETE_LINK_BY_ID = "DELETE FROM links WHERE id = ?";

    private static final String SQL_FIND_ALL_CATEGORIES = "SELECT * FROM categories ORDER BY id ASC";
    private static final String SQL_FIND_CATEGORY_BY_ID = "SELECT * FROM categories WHERE id = ?";
    private static final String SQL_FIND_CATEGORY_BY_NAME = "SELECT * FROM categories WHERE cat_name = ?";
    private static final String SQL_INSERT_CATEGORY = "INSERT INTO categories (cat_name) VALUES (?)";
    private static final String SQL_DELETE_CATEGORY_BY_ID = "DELETE FROM categories WHERE id = ?";

    public LinkDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Link> findLinkById(long id) {
        log.trace("enter findLinkById(): id: {}", id);

        LinkRowCallbackHandler rowCallbackHandler = new LinkRowCallbackHandler();
        jdbcTemplate.query(SQL_FIND_LINK_BY_ID, rowCallbackHandler, id);
        List<Link> links = rowCallbackHandler.getLinks();
        if (links.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(links.get(0));
        }
    }

    @Override
    public Link[] findAllLinks() {
        LinkRowCallbackHandler rowCallbackHandler = new LinkRowCallbackHandler();
        jdbcTemplate.query(SQL_FIND_ALL_LINKS, rowCallbackHandler);
        List<Link> links = rowCallbackHandler.getLinks();
        return links.toArray(new Link[0]);
    }

    @Override
    public Optional<Long> findLinkId(long catId, String uri) {
        log.trace("enter findLinkId(): catId: {}, link: {}", catId, uri);
        return findData(SQL_FIND_ID_BY_CAT_AND_LINK, (rs, i) -> rs.getLong("id"), catId, uri);
    }

    @Override
    public long saveNewLink(LinkRequest linkRequest) {
        log.trace("enter saveNewLink()");

        Objects.requireNonNull(linkRequest, "linkRequest is null");

        long catId = linkRequest.getCatId();
        String uri = linkRequest.getUri();
        Set<String> tags = linkRequest.getTags();
        String comment = linkRequest.getComment();
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_LINK, new String[]{"id"});
            ps.setLong(1, catId);
            ps.setString(2, uri);
            return ps;
        }, generatedKeyHolder);

        long id = Objects.requireNonNull(generatedKeyHolder.getKey(), "generated id is null").longValue();
        log.trace("saveNewLink(): id: {}", id);

        if (tags != null) {
            saveNewTags(id, tags);
        }
        if (comment != null) {
            saveNewComment(id, comment);
        }
        return id;
    }

    @Override
    public void updateCatId(long linkId, long catIdNew) {
        log.trace("enter updateCategoryId(): linkId: {}, categoryIdNew: {}", linkId, catIdNew);

        jdbcTemplate.update(SQL_UPDATE_CATEGORY_ID, catIdNew, linkId);
    }

    @Override
    public void updateTags(long linkId, Set<String> tagsNew) {
        log.trace("enter updateTags()");
        Objects.requireNonNull(tagsNew, "tagsNew is null");

        deleteTagsMapping(linkId);
        if (tagsNew.size() != 0) {
            saveNewTags(linkId, tagsNew);
        }
    }

    private void deleteTagsMapping(long linkId) {
        log.trace("enter deleteTagsMapping(): linkId: {}", linkId);
        jdbcTemplate.update(SQL_DELETE_TAGS_MAPPING_BY_ID, linkId);
    }

    private void saveNewTags(long linkId, Set<String> tags) {
        log.trace("enter saveNewTags(): linkId: {}, tagsNew: {}", linkId, tags);

        for(String tag : tags) {
            long newTagId = saveNewTag(linkId, tag);
            saveNewTagMapping(linkId, newTagId);
        }
    }

    private long saveNewTag(long linkId, String tag) {
        log.trace("enter saveNewTag(): linkId: {}, tag: {}", linkId, tag);
        
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_TAG, new String[]{"id"});
            ps.setString(1, tag);
            return ps;
        }, generatedKeyHolder);
        return Objects.requireNonNull(generatedKeyHolder.getKey(), "generated id is null").longValue();
    }

    private void saveNewTagMapping(long linkId, long tagId) {
        log.trace("enter saveNewTagMapping(): linkId: {}, tagId: {}", linkId, tagId);
        jdbcTemplate.update(SQL_INSERT_TAG_MAPPING, linkId, tagId);
    }


    @Override
    public void updateComment(long linkId, String commentNew) {
        log.trace("enter updateComment()");

        deleteComment(linkId);
        saveNewComment(linkId, commentNew);
    }

    private void deleteComment(long linkId) {
        log.trace("enter deleteComment(): linkId: {}", linkId);
        jdbcTemplate.update(SQL_DELETE_COMMENT_BY_ID, linkId);
    }

    private void saveNewComment(long linkId, String comment) {
        log.trace("enter saveNewComment(): linkId: {}, comment: {}", comment);
        jdbcTemplate.update(SQL_INSERT_COMMENT, linkId, comment);
    }

    @Override
    public void deleteLink(long linkId) {
        log.trace("enter deleteLink(): linkId: {}", linkId);
        jdbcTemplate.update(SQL_DELETE_LINK_BY_ID, linkId);
    }

    @Override
    public Category[] getAllCategories() {
        List<Category> categories = jdbcTemplate.query(SQL_FIND_ALL_CATEGORIES, categoryRowMapper);
        return categories.toArray(new Category[0]);
    }

    @Override
    public Optional<Category> findCategoryById(long id) {
        return findData(SQL_FIND_CATEGORY_BY_ID, categoryRowMapper, id);
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return findData(SQL_FIND_CATEGORY_BY_NAME, categoryRowMapper, name);
    }

    @Override
    public long saveNewCategory(CategoryRequest request) {
        log.trace("enter saveNewCategory()");
        Objects.requireNonNull(request, "request is null");

        String name = request.getName();
        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_INSERT_CATEGORY, new String[]{"id"});
            ps.setString(1, name);
            return ps;
        }, generatedKeyHolder);

        long id = Objects.requireNonNull(generatedKeyHolder.getKey(), "generated id is null").longValue();
        log.trace("saveNewCategory(): id: {}", id);
        return id;
    }

    @Override
    public void deleteCategory(long id) {
        log.trace("enter deleteCategory(): id: {}", id);
        jdbcTemplate.update(SQL_DELETE_CATEGORY_BY_ID, id);
    }

    private <T> Optional<T> findData(String sql, RowMapper<T> rowMapper, Object...args) {
        List<T> dataList = jdbcTemplate.query(sql, rowMapper, args);
        if (dataList != null && !dataList.isEmpty()) {
            return Optional.of(dataList.get(0));
        } else {
            return Optional.empty();
        }
    }

}
