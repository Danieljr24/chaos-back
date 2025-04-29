package com.example.chaos_back.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chaos_back.entity.Role;
import com.example.chaos_back.entity.User;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByUser(User user);
    Optional<Role> findByRoleName(String roleName);  
}
