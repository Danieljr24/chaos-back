package com.example.olimpo_service.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private Key key;
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 5; // 5 horas

    @PostConstruct
    public void init() {
        // Clave fija para pruebas (mínimo 32 bytes para HS256)
        String secret = "clave-secreta-super-larga-para-pruebas-jwt-1234567890";
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(String username, Map<String, Object> rolesByMicroservice) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .addClaims(rolesByMicroservice)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(key)
                    .compact();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Error al generar el token", e);
        }
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Map<String, Object> extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Token inválido", e);
        }
    }
}
