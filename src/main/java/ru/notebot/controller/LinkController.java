package ru.notebot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.notebot.api.request.CategoryRequest;
import ru.notebot.api.request.LinkRequest;
import ru.notebot.service.ILinkService;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class LinkController {
    private final ILinkService linkService;

    public LinkController(ILinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/links")
    public ResponseEntity<?> getAllLinks() {
        log.trace("enter getLinks()");
        return ResponseEntity.ok(linkService.getAllLinks());
    }

    @GetMapping("/links/{id}")
    public ResponseEntity<?> getLink(@PathVariable long id) {
        log.trace("enter getLink()");
        return ResponseEntity.ok(linkService.getLink(id));
    }

    @PostMapping("/links")
    public ResponseEntity<?> newLink(@RequestBody LinkRequest linkRequest) {
        log.trace("enter newLink()");
        return ResponseEntity.created(linkService.newLink(linkRequest)).build();
    }

    @PutMapping("/links/{id}")
    public ResponseEntity<?> updateLink(@PathVariable long id, @RequestBody LinkRequest linkRequest) {
        log.trace("enter updateLink()");

        linkService.updateLink(id, linkRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/links/{id}")
    public ResponseEntity<?> deleteLink(@PathVariable long id) {
        log.trace("enter deleteLink()");

        linkService.deleteLink(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/links/categories")
    public ResponseEntity<?> getAllCategories() {
        log.trace("enter getAllCategories()");
        return ResponseEntity.ok(linkService.getAllCategories());
    }

    @GetMapping("/links/categories/{id}")
    public ResponseEntity<?> getCategory(@PathVariable long id) {
        log.trace("enter getCategory()");
        return ResponseEntity.ok(linkService.getCategory(id));
    }

    @PostMapping("/links/categories")
    public ResponseEntity<?> newCategory(@RequestBody CategoryRequest request) {
        log.trace("enter newCategory()");
        return ResponseEntity.created(linkService.newCategory(request)).build();
    }

    @DeleteMapping("/links/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id) {
        log.trace("enter deleteCategory()");
        linkService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
