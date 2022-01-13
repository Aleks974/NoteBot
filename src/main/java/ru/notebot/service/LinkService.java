package ru.notebot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.notebot.api.request.CategoryRequest;
import ru.notebot.api.request.LinkRequest;
import ru.notebot.dao.ILinkDao;
import ru.notebot.exception.CategoryAlreadyExists;
import ru.notebot.exception.CategoryNotFound;
import ru.notebot.exception.LinkAlreadyExists;
import ru.notebot.exception.LinkNotFound;
import ru.notebot.model.Category;
import ru.notebot.model.Link;
import ru.notebot.service.util.ApiUriHelper;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.*;

@Slf4j
@Service
public class LinkService implements ILinkService {
    private final ILinkDao linkDao;
    private final ApiUriHelper apiUriHelper;

    public LinkService(ILinkDao linkDao, ApiUriHelper apiUriHelper) {
        this.linkDao = linkDao;
        this.apiUriHelper = apiUriHelper;
    }

    @Override
    public Link getLink(long id) {
        log.trace("enter getLink()");
        return linkDao.findLinkById(id).orElseThrow(() -> new LinkNotFound(id));
    }

    @Override
    public Link[] getAllLinks() {
        log.trace("enter getAllLinks()");
        return linkDao.findAllLinks();
    }

    @Override
    public URI newLink(LinkRequest request) {
        log.trace("enter newLink()");

        LinkRequest sanitizedRequest = sanitizeLinkRequest(Objects.requireNonNull(request, "request is  null"));

        long catId = sanitizedRequest.getCatId();
        String uri = sanitizedRequest.getUri();
        linkDao.findLinkId(catId, uri).ifPresent(linkId -> {
            throw new LinkAlreadyExists(linkId, catId, uri);
        });

        // ToDo check catid

        long linkId = linkDao.saveNewLink(sanitizedRequest);
        return apiUriHelper.uriFrom("links", String.valueOf(linkId));
    }

    @Override
    public void updateLink(long id, LinkRequest request) {
        log.trace("enter updateLink()");
        LinkRequest sanitizedRequest = sanitizeLinkRequest(Objects.requireNonNull(request, "request is  null"));

        Link linkFromDb = linkDao.findLinkById(id).orElseThrow(() -> new LinkNotFound(id));
        updateLinkChanges(id, linkFromDb, sanitizedRequest);
    }

    private void updateLinkChanges(long linkId, Link linkFromDb, LinkRequest linkRequest) {
        log.trace("enter updateLinkChanges()");

        final long CAT_NOT_DEFINED = 0;
        long catIdNew = linkRequest.getCatId();
        if (catIdNew != CAT_NOT_DEFINED) {
            long catIdFromDb = linkFromDb.getCatId();
            if (catIdNew != catIdFromDb) {
                // ToDo check catid
                linkDao.updateCatId(linkId, catIdNew);
            }
        }

        Set<String> tagsNew = linkRequest.getTags();
        if (tagsNew != null) {
            Set<String> tagsFromDb = linkFromDb.getTags();
            if (!tagsNew.equals(tagsFromDb)) {
                linkDao.updateTags(linkId, tagsNew);
            }
        }

        String commentNew = linkRequest.getComment();
        if (commentNew != null) {
            String commentFromDb = linkFromDb.getComment();
            if (!commentNew.equals(commentFromDb)) {
                linkDao.updateComment(linkId, commentNew);
            }
        }
    }

    @Override
    public void deleteLink(long id) {
        log.trace("enter deleteLink()");

        linkDao.findLinkById(id).orElseThrow(() -> new LinkNotFound(id));
        linkDao.deleteLink(id);
    }

    @Override
    public Category[] getAllCategories() {
        log.trace("enter getAllCategories()");
        return linkDao.getAllCategories();
    }

    @Override
    public Category getCategory(long id) {
        log.trace("enter getCategory()");
        return linkDao.findCategoryById(id).orElseThrow(() -> new CategoryNotFound(id));
    }

    @Override
    public URI newCategory(CategoryRequest request) {
        log.trace("enter newCategory()");

        CategoryRequest sanitizedRequest = sanitizeCategoryRequest(request);
        String catName = sanitizedRequest.getName();
        linkDao.findCategoryByName(catName).ifPresent(category -> {
            throw new CategoryAlreadyExists(category.getId(), catName);
        });
        long id = linkDao.saveNewCategory(request);
        return apiUriHelper.uriFrom("links", "categories", String.valueOf(id));
    }

    @Override
    public void deleteCategory(long id) {
        log.trace("enter deleteCategory()");
        linkDao.findCategoryById(id).orElseThrow(() -> new CategoryNotFound(id));

        // ToDo check link existence
        linkDao.deleteCategory(id);
    }

    // ToDo
    private void updateCategory(long id) {
        log.trace("enter updateCategory()");
        linkDao.findCategoryById(id).orElseThrow(() -> new CategoryNotFound(id));
        
    }

    private LinkRequest sanitizeLinkRequest(LinkRequest linkRequest) {
        return linkRequest;
    }
    private CategoryRequest sanitizeCategoryRequest(CategoryRequest request) {
        return request;
    }



}
