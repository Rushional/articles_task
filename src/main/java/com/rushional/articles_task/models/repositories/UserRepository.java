package com.rushional.articles_task.models.repositories;

import com.rushional.articles_task.models.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<AppUser, Long> {
}
