package ru.notebot.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@ControllerAdvice
public class AdviceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<?> handleDataAccessException(DataAccessException ex, WebRequest request) {
        log.error("enter to handleDataAccessException(), message: {}", ex.getMessage());
        logStackTrace(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<?> handleNullPointerException(NullPointerException ex, WebRequest request) {
        log.error("enter to handleNullPointerException(), message: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(LinkAlreadyExists.class)
    protected ResponseEntity<?> handleLinkAlreadyExists(LinkAlreadyExists ex, WebRequest request) {
        log.warn("enter to handleLinkAlreadyExists(), message: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(LinkNotFound.class)
    protected ResponseEntity<?> handleLinkNotFound(LinkNotFound ex, WebRequest request) {
        log.warn("enter to handleLinkNotFound(), message: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(CategoryAlreadyExists.class)
    protected ResponseEntity<?> handleCategoryAlreadyExists(CategoryAlreadyExists ex, WebRequest request) {
        log.warn("enter to handleCategoryAlreadyExists(), message: {}", ex.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(CategoryNotFound.class)
    protected ResponseEntity<?> handleCategoryNotFound(CategoryNotFound ex, WebRequest request) {
        log.warn("enter to handleCategoryNotFound(), message: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }

    ////////////

    private void logStackTrace(DataAccessException ex) {
        Throwable exc = ex;
        do {
            log.trace(exc.getMessage());
            for (StackTraceElement st : exc.getStackTrace()) {
                log.trace(st.toString());
            }
        } while ((exc = exc.getCause()) != null);
    }
}
