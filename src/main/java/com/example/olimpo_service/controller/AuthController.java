package com.example.olimpo_service.controller;

import com.example.olimpo_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");

        String token = authService.authenticate(username, password);

        if (username == null || password == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username y password son obligatorios"));
        }

        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/has-role")
    public ResponseEntity<Map<String, Boolean>> hasRole(
            @RequestParam String username,
            @RequestParam String microservice
    ) {
        boolean hasRole = authService.hasRoleInMicroservice(username, microservice);
        return ResponseEntity.ok(Map.of("hasRole", hasRole));
    }
}
