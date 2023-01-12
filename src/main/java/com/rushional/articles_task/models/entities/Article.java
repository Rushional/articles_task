package com.rushional.articles_task.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Article {

    public Article() {}

    public Article(String title, AppUser appUser, String content, LocalDateTime postDate) {
        this.title = title;
        this.appUser = appUser;
        this.content = content;
        this.postDate = postDate;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser appUser;

    private String content;

    private LocalDateTime postDate;
}
