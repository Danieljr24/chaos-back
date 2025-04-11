package com.example.olimpo_service.repository;

import com.example.olimpo_service.entities.User;
import com.example.olimpo_service.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    List<UserRole> findByUser(User user);
}
