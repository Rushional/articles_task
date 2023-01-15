package com.rushional.articles_task.models.repositories;

import com.rushional.articles_task.models.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    @Query("SELECT u FROM AppUser u WHERE u.username = :username")
    public AppUser getUserByUsername(@Param("username") String username);
}
