package com.example.olimpo_service.service;

import com.example.olimpo_service.dto.LoginRequest;
import com.example.olimpo_service.dto.LoginResponse;
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
        // 1. Buscar usuario
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Validar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // 3. Obtener roles
        List<UserRole> roles = userRoleRepository.findByUser(user);
        Map<String, String> roleMap = roles.stream()
                .collect(Collectors.toMap(
                        UserRole::getMicroservice,
                        UserRole::getRole
                ));

        // 4. Generar JWT con roles por microservicio
        String jwt = jwtUtil.generateToken(user.getUsername(), roleMap);

        // 5. Generar y guardar ticket
        String ticket = UUID.randomUUID().toString();
        ticketRepository.saveTicket(user.getUsername(), ticket, Instant.now());

        // 6. Retornar JWT + Ticket
        return new LoginResponse(jwt, ticket);
    }

    public void logout(String ticket) {
        ticketRepository.invalidateTicket(ticket);
    }
}
