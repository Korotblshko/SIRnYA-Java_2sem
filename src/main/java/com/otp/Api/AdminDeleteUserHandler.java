package com.otp.Api;

import com.otp.Dao.UserDAO;
import com.otp.Util.RoleUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AdminDeleteUserHandler implements HttpHandler {
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"DELETE".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            String role = RoleUtil.getRole(exchange);
            if (!"ADMIN".equals(role)) {
                byte[] response = "Forbidden".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(403, response.length);
                exchange.getResponseBody().write(response);
                return;
            }

            String query = exchange.getRequestURI().getQuery();
            int userId = Integer.parseInt(getQueryParam(query, "id"));

            userDAO.deleteUserWithCodes(userId);

            byte[] response = "User deleted".getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, response.length);
            exchange.getResponseBody().write(response);
        } catch (Exception e) {
            byte[] response = e.getMessage().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(400, response.length);
            exchange.getResponseBody().write(response);
        } finally {
            exchange.close();
        }
    }

    private String getQueryParam(String query, String key) {
        if (query == null) return "";
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2 && kv[0].equals(key)) {
                return kv[1];
            }
        }
        return "";
    }
}