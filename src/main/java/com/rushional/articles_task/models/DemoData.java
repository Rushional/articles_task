package com.rushional.articles_task.models;

import com.rushional.articles_task.models.entities.AppUser;
import com.rushional.articles_task.models.entities.Article;
import com.rushional.articles_task.models.entities.Role;
import com.rushional.articles_task.models.repositories.ArticleRepository;
import com.rushional.articles_task.models.repositories.RoleRepository;
import com.rushional.articles_task.models.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;

@Component
public class DemoData {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private RoleRepository roleRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {
        Role adminRole = new Role();
        adminRole.setName("ADMIN");
        roleRepository.save(adminRole);

        Role userRole = new Role();
        userRole.setName("USER");
        roleRepository.save(userRole);

        ArrayList<Role> adminRoles = new ArrayList<>();
        adminRoles.add(adminRole);

        ArrayList<Role> userRoles = new ArrayList<>();
        userRoles.add(userRole);

        AppUser appUserAlice = new AppUser(
            "Alice the Admin",
            "$2a$08$h2d3z4Gt5v/kxPDt7HEv6uJljfpJs0dvW10RaaG4K4/uCVtKUMNYO",
            adminRoles);
        userRepository.save(appUserAlice);

        AppUser appUserBob = new AppUser(
            "Bob the User",
            "$2a$08$8OmRBHdOps991FnF2ApWPeuJdwsRimcBvjiE2Q1OgYd5KxCSNNhfq",
            userRoles);
        userRepository.save(appUserBob);

        Article article1 = new Article(
            "On the nature of assessment tests",
            appUserAlice,
            "I don't like them!",
            ZonedDateTime.now()
        );
        articleRepository.save(article1);
        Article article2 = new Article(
            "Writing test data is very relaxing",
            appUserAlice,
            "I like it!",
            ZonedDateTime.now()
        );
        articleRepository.save(article2);
        Article article3 = new Article(
            "Top 10 longest bridges in the village I was born in",
            appUserBob,
            "1) The only bridge we've got. 2) That's it",
            ZonedDateTime.now()
        );
        articleRepository.saveAndFlush(article3);
    }
}
