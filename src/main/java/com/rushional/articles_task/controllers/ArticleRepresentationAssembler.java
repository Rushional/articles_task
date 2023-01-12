package com.rushional.articles_task.controllers;

import com.rushional.articles_task.models.entities.Article;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ArticleRepresentationAssembler implements RepresentationModelAssembler<Article, EntityModel<Article>> {
    @Override
    public EntityModel<Article> toModel(Article card) {

        return EntityModel.of(card, //
            linkTo(methodOn(ArticleController.class).one(card.getId())).withSelfRel(),
            linkTo(methodOn(ArticleController.class).all()).withRel("cards"));
    }

}
