package com.rushional.articles_task.controllers;

import com.rushional.articles_task.controllers.exceptions.ArticleNotFoundException;
import com.rushional.articles_task.models.DailyArticlesCountRow;
import com.rushional.articles_task.models.entities.Article;
import com.rushional.articles_task.models.repositories.ArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ArticleController {
    private final ArticleRepository repository;
    private final ArticleRepresentationAssembler representationAssembler;
    private final PagedResourcesAssembler<Article> pagedResourcesAssembler;

    public ArticleController(ArticleRepository repository, ArticleRepresentationAssembler representationAssembler,
                             PagedResourcesAssembler<Article> pagedResourcesAssembler) {
        this.repository = repository;
        this.representationAssembler = representationAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/articles")
    ResponseEntity<?> all(Pageable pageable) {
        Page<Article> articlesPage = repository.findAll(pageable);

        PagedModel<EntityModel<Article>> collModel = pagedResourcesAssembler
            .toModel(articlesPage, representationAssembler);

        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @GetMapping("/articles/{id}")
    EntityModel<Article> one(@PathVariable Long id) {
        Article card = repository.findById(id)
            .orElseThrow(() -> new ArticleNotFoundException(id));
        return representationAssembler.toModel(card);
    }

    @PostMapping("/articles")
    ResponseEntity<?> newArticle(@RequestBody Article newArticle) {
        EntityModel<Article> entityModel = representationAssembler.toModel(repository.save(newArticle));

        return ResponseEntity //
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
            .body(entityModel);
    }

    @GetMapping("/posts_counts")
    @ResponseBody
    public List<DailyArticlesCountRow> postCounts() {
        return repository.getDailyPostCountsForPastWeek();
    }
}
