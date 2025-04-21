package com.example.olimpo_service.service;

import com.example.olimpo_service.dto.LoginRequest;
import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.entities.User;
import com.example.olimpo_service.repository.UserRepository;
import com.example.olimpo_service.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
    }

    public String login(LoginRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var roles = user.getRoles().stream()
                .collect(Collectors.toMap(
                        r -> r.getRoleName(),
                        r -> "true"
                ));

        return jwtUtil.generateToken(user.getUsername(), roles);
    }

    public String register(RegisterRequest request) {
        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return "Usuario registrado con éxito";
    }

    public void setTicketCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("ticket", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 día
        response.addCookie(cookie);
    }
}
