package com.example.olimpo_service.controller;

import com.example.olimpo_service.dto.LoginRequest;
import com.example.olimpo_service.dto.LoginResponse;
import com.example.olimpo_service.dto.RegisterRequest;
import com.example.olimpo_service.service.AuthService;
import com.example.olimpo_service.util.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("Usuario registrado correctamente.");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
            HttpServletResponse response) {

        LoginResponse loginResponse = authService.login(request);

        CookieUtil.addJwtCookie(response, loginResponse.getJwt());

        response.setHeader("Ticket", loginResponse.getTicket());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Ticket") String ticket,
            HttpServletResponse response) {

        authService.logout(ticket);

        CookieUtil.clearJwtCookie(response);

        return ResponseEntity.ok("Sesión cerrada correctamente.");
    }
}
