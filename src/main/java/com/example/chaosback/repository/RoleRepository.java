package com.example.chaosback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaosback.entity.Role;
import com.example.chaosback.entity.User;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByUser(User user);
    Optional<Role> findByRoleName(String roleName);  
}
