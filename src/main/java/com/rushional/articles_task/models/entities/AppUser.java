package com.rushional.articles_task.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"articlesList", "password", "role", "enabled"})
public class AppUser {

    public AppUser() {}

    public AppUser(@NotNull String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        enabled = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;



    private String password;
    private String role;
    private boolean enabled;

    @OneToMany(mappedBy = "author")
    List<Article> articlesList;
}
