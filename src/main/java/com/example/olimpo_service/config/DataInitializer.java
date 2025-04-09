package com.example.olimpo_service.config;

import com.example.olimpo_service.model.User;
import com.example.olimpo_service.model.UserRole;
import com.example.olimpo_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // Si ya existe, no lo crea otra vez
        if (userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("1234"); // Para pruebas, sin encriptar

            UserRole role = new UserRole();
            role.setMicroservice("themis");
            role.setRole("ADMIN");
            role.setUser(user);

            user.setRoles(Set.of(role));

            userRepository.save(user);
            System.out.println("✅ Usuario admin creado con rol ADMIN en 'themis'");
        }
    }

    public void run(String... args) {
        // Si ya existe, no lo crea otra vez
        if (userRepository.findByUsername("admin").isEmpty()) {
            User user = new User();
            user.setUsername("zeus");
            user.setpassword("rayo");

            UserRole role = new UserRole();
            role.setMicroservice("themis");
            role.setRole("ADMIN");
            role.setUser(user);

            user.setRoles(Set.of(role));

            userRepository.save(user);
            System.out.println("✅ Usuario admin creado con rol ADMIN en 'themis'");
        }
    }
}


