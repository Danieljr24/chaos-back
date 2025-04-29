package com.example.chaosback.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.chaosback.dto.LoginRequest;
import com.example.chaosback.dto.RegisterRequest;
import com.example.chaosback.entity.Role;
import com.example.chaosback.entity.User;
import com.example.chaosback.repository.RoleRepository;
import com.example.chaosback.repository.UserRepository;
import com.example.chaosback.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository userRoleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;

    public AuthService(UserRepository userRepository, RoleRepository userRoleRepository, JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder, AuthenticationManager authManager) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
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
        .map(role -> role.getRoleName())
        .collect(Collectors.toList());
        
        return jwtUtil.generateToken(user.getUsername(), roles);
    }

    public String register(RegisterRequest request) {
        try {
            // Crear un nuevo usuario
            User user = new User();
            user.setUsername(request.getUsername());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        
            // Guardar el usuario primero
            userRepository.save(user);
        
            // Crear y asociar roles
            Role role = new Role();
            role.setRoleName("USER");
            role.setUser(user);
        
            // Guardar el rol después de asociarlo con el usuario
            userRoleRepository.save(role);
        
            return "Usuario registrado con éxito";
        } catch (Exception e) {
            // Loguear el error para depuración
            System.out.println("Error al registrar el usuario: " + e.getMessage());
            e.printStackTrace();
            return "Error al registrar el usuario";
        }
    }
    
    
    
    

    public void setTicketCookie(String token, HttpServletResponse response) {
        Cookie cookie = new Cookie("ticket", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);
    }
}
