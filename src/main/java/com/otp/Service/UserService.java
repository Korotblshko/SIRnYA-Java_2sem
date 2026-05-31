package com.otp.Service;

import com.otp.Dao.UserDAO;
import com.otp.Model.User;
import com.otp.Util.PasswordUtil;

import java.sql.SQLException;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public void register(String login, String password, String role) throws SQLException {
        User existing = userDAO.findByLogin(login);
        if (existing != null) {
            throw new RuntimeException("User already exists");
        }

        if ("ADMIN".equals(role) && userDAO.isAdminExists()) {
            throw new RuntimeException("Admin already exists");
        }

        String hash = PasswordUtil.hashPassword(password);
        User user = new User();
        user.setLogin(login);
        user.setPasswordHash(hash);
        user.setRole(role);
        userDAO.createUser(user);
    }

    public User login(String login, String password) throws SQLException {
        User user = userDAO.findByLogin(login);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!PasswordUtil.checkPassword(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }
}