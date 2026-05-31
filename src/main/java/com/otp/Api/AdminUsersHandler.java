package com.otp.Api;

import com.otp.Dao.UserDAO;
import com.otp.Model.User;
import com.otp.Util.RoleUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class AdminUsersHandler implements HttpHandler {
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
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

            List<User> users = userDAO.findAllUsersExceptAdmin();
            StringBuilder sb = new StringBuilder();
            for (User user : users) {
                sb.append(user.getId())
                        .append(" | ")
                        .append(user.getLogin())
                        .append(" | ")
                        .append(user.getRole())
                        .append("\n");
            }

            byte[] response = sb.toString().getBytes(StandardCharsets.UTF_8);
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
}