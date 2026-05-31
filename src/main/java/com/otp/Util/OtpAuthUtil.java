package com.otp.Util;

import com.sun.net.httpserver.HttpExchange;
import io.jsonwebtoken.Claims;

public class OtpAuthUtil {
    public static Claims getClaims(HttpExchange exchange) {
        String token = AuthUtil.getBearerToken(exchange);
        if (token == null) {
            throw new RuntimeException("Missing token");
        }
        return JwtUtil.validateToken(token);
    }
}