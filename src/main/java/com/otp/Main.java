package com.otp;

import com.otp.Api.LoginHandler;
import com.otp.Api.OtpGenerateHandler;
import com.otp.Api.RegisterHandler;
import com.sun.net.httpserver.HttpServer;

import com.otp.Api.AdminUsersHandler;
import com.otp.Api.AdminDeleteUserHandler;

import com.otp.Api.OtpValidateHandler;
import com.otp.Service.OtpExpiryScheduler;


import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/register", new RegisterHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/otp/generate", new OtpGenerateHandler());
        server.setExecutor(null);
        server.start();

        OtpExpiryScheduler expiryScheduler = new OtpExpiryScheduler();
        expiryScheduler.start();

        server.createContext("/otp/validate", new OtpValidateHandler());
        server.createContext("/admin/users", new AdminUsersHandler());
        server.createContext("/admin/delete-user", new AdminDeleteUserHandler());

        System.out.println("Server started on port 8080");
    }
}