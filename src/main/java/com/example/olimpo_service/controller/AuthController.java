package com.example.olimpo_service.controller;

import com.example.olimpo_service.dto.LoginRequest;
import com.example.olimpo_service.dto.LoginResponse;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
                                                HttpServletResponse response) {
        // Delegar login al AuthService
        LoginResponse loginResponse = authService.login(request);

        // Establecer JWT como cookie segura
        CookieUtil.addJwtCookie(response, loginResponse.getJwt());

        // También podrías agregar el ticket como header si lo deseas en el cliente
        response.setHeader("Ticket", loginResponse.getTicket());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Ticket") String ticket,
                                            HttpServletResponse response) {
        // Invalida el ticket
        authService.logout(ticket);

        // Borra cookie JWT
        CookieUtil.clearJwtCookie(response);

        return ResponseEntity.ok("Sesión cerrada correctamente.");
    }
}
