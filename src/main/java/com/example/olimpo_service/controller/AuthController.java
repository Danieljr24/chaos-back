package com.example.olimpo_service.controller;

import com.example.olimpo_service.dto.LoginRequest;
import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request, HttpServletResponse response) {
        String token = authService.login(request);
        authService.setTicketCookie(token, response); // Establece cookie con ticket
        return token;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }
}
