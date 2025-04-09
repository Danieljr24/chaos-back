package com.example.olimpo_service.repository;

import com.example.olimpo_service.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    List<UserRole> findByUserUsernameAndMicroservice(String username, String microservice);
}