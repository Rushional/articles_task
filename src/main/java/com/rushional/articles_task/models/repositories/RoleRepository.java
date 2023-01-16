package com.rushional.articles_task.models.repositories;

import com.rushional.articles_task.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
