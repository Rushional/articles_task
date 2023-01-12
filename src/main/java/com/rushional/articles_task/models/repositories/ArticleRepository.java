package com.rushional.articles_task.models.repositories;

import com.rushional.articles_task.models.entities.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
