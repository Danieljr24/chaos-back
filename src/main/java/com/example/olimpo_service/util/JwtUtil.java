package com.example.olimpo_service.util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private Key key;
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 5; // 5 horas

    @PostConstruct
    public void init() {
        byte[] secretBytes = new byte[64]; // 512 bits = seguro para HMAC-SHA-512
        new SecureRandom().nextBytes(secretBytes);
        this.key = Keys.hmacShaKeyFor(secretBytes);

        String encodedKey = Base64.getEncoder().encodeToString(secretBytes);
        System.out.println("🔐 Clave secreta generada dinámicamente (solo para pruebas): " + encodedKey);
    }

    public String generateToken(String username, Map<String, Object> rolesByMicroservice) {
        return Jwts.builder()
                .setSubject(username)
                .addClaims(rolesByMicroservice)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
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
