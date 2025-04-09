package com.example.olimpo_service.service;

import com.example.olimpo_service.model.User;
import com.example.olimpo_service.model.UserRole;
import com.example.olimpo_service.repository.UserRepository;
import com.example.olimpo_service.util.JwtUtil;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty() || !userOptional.get().getPassword().equals(password)) {
            throw new BadCredentialsException("Credenciales inválidas");
        }        

        User user = userOptional.get();

        // Extrae los roles por microservicio y convierte a Map<String, Object>
        var rolesMap = user.getRoles()
                .stream()
                .collect(Collectors.toMap(
                        UserRole::getMicroservice,
                        role -> (Object) role.getRole() // Conversión explícita a Object
                ));

        // Genera JWT con username y roles por microservicio
        return jwtUtil.generateToken(username, rolesMap);
    }

    public boolean hasRoleInMicroservice(String username, String microserviceName) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"))
                .getRoles().stream()
                .anyMatch(role -> role.getMicroservice().equalsIgnoreCase(microserviceName));
    }
}