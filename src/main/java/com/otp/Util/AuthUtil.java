package com.otp.Util;

import com.sun.net.httpserver.HttpExchange;

public class AuthUtil {
    public static String getBearerToken(HttpExchange exchange) {
        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        if (auth == null || !auth.startsWith("Bearer ")) {
            return null;
        }
        return auth.substring(7);
    }
}