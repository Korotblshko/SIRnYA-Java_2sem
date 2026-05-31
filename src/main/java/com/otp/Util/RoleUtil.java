package com.otp.Util;

import com.sun.net.httpserver.HttpExchange;
import io.jsonwebtoken.Claims;

public class RoleUtil {
    public static String getRole(HttpExchange exchange) {
        String token = AuthUtil.getBearerToken(exchange);
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Missing token");
        }
        Claims claims = JwtUtil.validateToken(token);
        String role = claims.get("role", String.class);
        if (role == null) {
            throw new RuntimeException("Role is missing");
        }
        return role;
    }
}