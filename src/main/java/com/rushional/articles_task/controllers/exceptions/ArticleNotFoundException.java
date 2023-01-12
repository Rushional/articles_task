package com.rushional.articles_task.controllers.exceptions;

public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(Long id) {
        super("Could not find article " + id);
    }
}
