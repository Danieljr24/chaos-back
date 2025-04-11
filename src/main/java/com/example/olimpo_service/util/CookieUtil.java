package com.example.olimpo_service.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {

    private static final String JWT_COOKIE_NAME = "SESSION_ID";

    public static void addJwtCookie(HttpServletResponse response, String jwt) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, jwt);
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Actívalo solo si estás en HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1 hora, puedes ajustarlo

        response.addCookie(cookie);
    }

    public static void clearJwtCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(JWT_COOKIE_NAME, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true); // Igual que en addJwtCookie
        cookie.setPath("/");
        cookie.setMaxAge(0); // Eliminar inmediatamente

        response.addCookie(cookie);
    }
}
