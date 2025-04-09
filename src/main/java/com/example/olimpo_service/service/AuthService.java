package com.example.olimpo_service.service;

import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.model.User;
import com.example.olimpo_service.model.UserRole;
import com.example.olimpo_service.repository.UserRepository;
import com.example.olimpo_service.util.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.List;
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

        var rolesMap = user.getRoles()
                .stream()
                .collect(Collectors.toMap(
                        UserRole::getMicroservice,
                        role -> (Object) role.getRole()
                ));

        return jwtUtil.generateToken(username, rolesMap);
    }

    public boolean hasRoleInMicroservice(String username, String microserviceName) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"))
                .getRoles().stream()
                .anyMatch(role -> role.getMicroservice().equalsIgnoreCase(microserviceName));
    }

    public void register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
    
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // puedes codificar con BCrypt si quieres
    
        List<UserRole> roles = request.getRoles().stream().map(r -> {
            UserRole ur = new UserRole();
            ur.setMicroservice(r.getMicroservice());
            ur.setRole(r.getRole());
            ur.setUser(user);
            return ur;
        }).toList();
    
        user.setRoles(roles);
    
        userRepository.save(user);
    }
    
}
