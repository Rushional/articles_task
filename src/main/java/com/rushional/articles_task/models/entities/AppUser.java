package com.rushional.articles_task.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"articlesList", "password", "roles", "enabled"})
public class AppUser {

    public AppUser() {}

    public AppUser(@NotNull String username, String password, Collection<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        enabled = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
            name = "user_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    private String password;
    private boolean enabled;

    @OneToMany(mappedBy = "author")
    List<Article> articlesList;
}
