package com.otp.Dao;

import com.otp.Model.OTPCode;
import com.otp.Util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OTPCodeDAO {
    private static final Logger logger = LoggerFactory.getLogger(OTPCodeDAO.class);

    public void createOTPCode(OTPCode otpCode) throws SQLException {
        String sql = "INSERT INTO otp_codes (code, user_id, operation_id, status, expires_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, otpCode.getCode());
            stmt.setInt(2, otpCode.getUserId());
            stmt.setString(3, otpCode.getOperationId());
            stmt.setString(4, otpCode.getStatus());
            stmt.setTimestamp(5, Timestamp.valueOf(otpCode.getExpiresAt()));

            stmt.executeUpdate();
            logger.info("OTP code created for user {}: {}", otpCode.getUserId(), otpCode.getCode());
        }
    }

    public OTPCode findByCode(String code) throws SQLException {
        String sql = "SELECT * FROM otp_codes WHERE code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                OTPCode otpCode = new OTPCode(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getInt("user_id"),
                        rs.getString("operation_id"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("expires_at").toLocalDateTime(),
                        rs.getTimestamp("used_at") != null ? rs.getTimestamp("used_at").toLocalDateTime() : null
                );
                return otpCode;
            }
        }
        return null;
    }

    public void updateStatus(int codeId, String status) throws SQLException {
        String sql = "UPDATE otp_codes SET status = ?, used_at = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setTimestamp(2, status.equals("USED") ? Timestamp.valueOf(LocalDateTime.now()) : null);
            stmt.setInt(3, codeId);
            stmt.executeUpdate();
            logger.info("OTP code {} status updated to: {}", codeId, status);
        }
    }

    public void markExpired() throws SQLException {
        String sql = "UPDATE otp_codes SET status = 'EXPIRED' WHERE status = 'ACTIVE' AND expires_at < ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            int rows = stmt.executeUpdate();
            logger.info("Marked {} OTP codes as EXPIRED", rows);
        }
    }

    public List<OTPCode> findByUserId(int userId) throws SQLException {
        List<OTPCode> codes = new ArrayList<>();
        String sql = "SELECT * FROM otp_codes WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OTPCode otpCode = new OTPCode(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getInt("user_id"),
                        rs.getString("operation_id"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("expires_at").toLocalDateTime(),
                        rs.getTimestamp("used_at") != null ? rs.getTimestamp("used_at").toLocalDateTime() : null
                );
                codes.add(otpCode);
            }
        }
        return codes;
    }

    public void deleteByUserId(int userId) throws SQLException {
        String sql = "DELETE FROM otp_codes WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }
}