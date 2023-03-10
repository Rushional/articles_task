package com.rushional.articles_task.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
public class Article {

    public Article() {}

    public Article(String title, AppUser author, String content, ZonedDateTime postDate) {
        this.title = title;
        this.author = author;
        this.content = content;
        this.postDate = postDate;
    }

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(length = 100)
    @Size(max = 100)
    private String title;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private AppUser author;

    @NotNull
    private String content;

    @NotNull
    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    //By default Hibernate maps ZonedDateTime to TS w/out TZ, which is just plain wrong, hence the @Column annotation
    private ZonedDateTime postDate;
}
