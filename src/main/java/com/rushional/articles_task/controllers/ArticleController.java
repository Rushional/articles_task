package com.rushional.articles_task.controllers;

import com.rushional.articles_task.controllers.exceptions.ArticleNotFoundException;
import com.rushional.articles_task.models.entities.Article;
import com.rushional.articles_task.models.repositories.ArticleRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ArticleController {
    private final ArticleRepository repository;
    private final ArticleRepresentationAssembler assembler;

    public ArticleController(ArticleRepository repository, ArticleRepresentationAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/articles")
    CollectionModel<EntityModel<Article>>all() {
        List<EntityModel<Article>> articles = repository.findAll().stream() //
            .map(assembler::toModel) //
            .collect(Collectors.toList());

        return CollectionModel.of(articles, linkTo(methodOn(ArticleController.class).all()).withSelfRel());
    }

    @GetMapping("/articles/{id}")
    EntityModel<Article> one(@PathVariable Long id) {
        Article card = repository.findById(id)
            .orElseThrow(() -> new ArticleNotFoundException(id));
        return assembler.toModel(card);
    }

    @PostMapping("/articles")
    ResponseEntity<?> newArticle(@RequestBody Article newArticle) {
        EntityModel<Article> entityModel = assembler.toModel(repository.save(newArticle));

        return ResponseEntity //
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
            .body(entityModel);
    }
}
