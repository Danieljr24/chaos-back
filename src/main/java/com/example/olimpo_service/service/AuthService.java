package com.example.olimpo_service.service;

import com.example.olimpo_service.dto.LoginRequest;
import com.example.olimpo_service.dto.LoginResponse;
import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.entities.User;
import com.example.olimpo_service.entities.UserRole;
import com.example.olimpo_service.repository.TicketRepository;
import com.example.olimpo_service.repository.UserRepository;
import com.example.olimpo_service.repository.UserRoleRepository;
import com.example.olimpo_service.util.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final TicketRepository ticketRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        List<UserRole> roles = userRoleRepository.findByUser(user);
        Map<String, String> roleMap = roles.stream()
                .collect(Collectors.toMap(
                        UserRole::getMicroservice,
                        UserRole::getRole
                ));

        String jwt = jwtUtil.generateToken(user.getUsername(), roleMap);
        String ticket = UUID.randomUUID().toString();
        ticketRepository.saveTicket(user.getUsername(), ticket, Instant.now());

        return new LoginResponse(jwt, ticket);
    }

    public void logout(String ticket) {
        ticketRepository.invalidateTicket(ticket);
    }

    @Transactional
    public void register(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        // Crear roles por microservicio
        for (Map.Entry<String, String> entry : request.getRoles().entrySet()) {
            UserRole userRole = new UserRole();
            userRole.setUser(user);
            userRole.setMicroservice(entry.getKey());
            userRole.setRole(entry.getValue());
            userRoleRepository.save(userRole);
        }
    }
}
