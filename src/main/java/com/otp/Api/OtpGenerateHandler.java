package com.otp.Api;

import com.otp.Service.OTPService;
import com.otp.Util.OtpAuthUtil;
import com.otp.Util.OtpFileUtil;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OtpGenerateHandler implements HttpHandler {
    private final OTPService otpService = new OTPService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            OtpAuthUtil.getClaims(exchange);

            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            String userIdStr = getValue(body, "userId");
            String operationId = getValue(body, "operationId");

            int userId = Integer.parseInt(userIdStr);
            String code = otpService.generateOTP(userId, operationId);

            OtpFileUtil.save("userId=" + userId + ", operationId=" + operationId + ", code=" + code);

            byte[] response = ("OTP code generated: " + code).getBytes(StandardCharsets.UTF_8);
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

    private String getValue(String body, String key) {
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=");
            if (kv.length == 2 && kv[0].equals(key)) {
                return kv[1];
            }
        }
        return "";
    }
}