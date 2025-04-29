package com.example.chaosback.data;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.chaosback.entity.Role;
import com.example.chaosback.entity.User;
import com.example.chaosback.repository.RoleRepository;
import com.example.chaosback.repository.UserRepository;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // 🔒 Para cifrar contraseñas

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User zeus = createUser("zeus", "12345");
            User dani = createUser("dani", "1234");

            createRole(zeus, "ADMIN");
            createRole(dani, "USER");

            System.out.println("Usuarios y roles creados correctamente.");
        }
    }

    private User createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // 🔐 Encryptar contraseña
        user.setRoles(new ArrayList<>());
        return userRepository.save(user);
    }

    private void createRole(User user, String roleName) {
        Role userRole = new Role();
        userRole.setRoleName(roleName);
        userRole.setUser(user);

        user.getRoles().add(userRole); // Asocia el rol al usuario

        userRoleRepository.save(userRole);
    }
}
