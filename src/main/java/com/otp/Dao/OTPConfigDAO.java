package com.otp.Dao;

import com.otp.Model.OTPConfig;
import com.otp.Util.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class OTPConfigDAO {
    private static final Logger logger = LoggerFactory.getLogger(OTPConfigDAO.class);

    public OTPConfig getConfig() throws SQLException {
        String sql = "SELECT * FROM otp_config LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                OTPConfig config = new OTPConfig(
                        rs.getInt("id"),
                        rs.getInt("code_length"),
                        rs.getInt("lifetime_seconds")
                );
                logger.info("Config loaded: length={}, lifetime={}s",
                        config.getCodeLength(), config.getLifetimeSeconds());
                return config;
            }
        }
        return null;
    }

    public void updateConfig(int codeLength, int lifetimeSeconds) throws SQLException {
        String sql = "UPDATE otp_config SET code_length = ?, lifetime_seconds = ? WHERE id = 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codeLength);
            stmt.setInt(2, lifetimeSeconds);
            stmt.executeUpdate();
            logger.info("Config updated: length={}, lifetime={}s", codeLength, lifetimeSeconds);
        }
    }
}