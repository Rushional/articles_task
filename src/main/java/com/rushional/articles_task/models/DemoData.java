package com.rushional.articles_task.models;

import com.rushional.articles_task.models.entities.AppUser;
import com.rushional.articles_task.models.entities.Article;
import com.rushional.articles_task.models.repositories.ArticleRepository;
import com.rushional.articles_task.models.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DemoData {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @EventListener
public void appReady(ApplicationReadyEvent event) {
        AppUser appUserAlice = new AppUser("Alice");
        userRepository.save(appUserAlice);
        AppUser appUserBob = new AppUser("Bob");
        userRepository.save(appUserBob);

//        Article article1 = new Article();
//        articleRepository.save(article1);
//        article1.setTitle("On the nature of assessment tests");
//        article1.setAppUser(appUserAlice);
//        article1.setContent("I don't like them!");
//        article1.setPostDate(LocalDateTime.now());
        Article article1 = new Article(
            "On the nature of assessment tests",
            appUserAlice,
            "I don't like them!",
            LocalDateTime.now()
        );
        articleRepository.save(article1);
        Article article2 = new Article(
            "Writing test data is very relaxing",
            appUserAlice,
            "I like it!",
            LocalDateTime.now()
        );
        articleRepository.save(article2);
        Article article3 = new Article(
            "Top 10 longest bridges in the village I was born in",
            appUserBob,
            "1) The only bridge we've got. 2) That's it",
            LocalDateTime.now()
        );
        articleRepository.save(article3);
    }
}