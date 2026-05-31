package com.otp.Service;

import com.otp.Dao.OTPCodeDAO;
import com.otp.Dao.OTPConfigDAO;
import com.otp.Model.OTPCode;
import com.otp.Model.OTPConfig;
import com.otp.Util.OtpCodeGenerator;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class OTPService {
    private final OTPCodeDAO otpCodeDAO = new OTPCodeDAO();
    private final OTPConfigDAO otpConfigDAO = new OTPConfigDAO();

    public String generateOTP(int userId, String operationId) throws SQLException {
        OTPConfig config = otpConfigDAO.getConfig();
        String code = OtpCodeGenerator.generateCode(config.getCodeLength());

        OTPCode otpCode = new OTPCode();
        otpCode.setCode(code);
        otpCode.setUserId(userId);
        otpCode.setOperationId(operationId);
        otpCode.setStatus("ACTIVE");
        otpCode.setCreatedAt(LocalDateTime.now());
        otpCode.setExpiresAt(LocalDateTime.now().plusSeconds(config.getLifetimeSeconds()));

        otpCodeDAO.createOTPCode(otpCode);
        return code;
    }

    public boolean validateOTP(String code) throws SQLException {
        OTPCode otpCode = otpCodeDAO.findByCode(code);
        if (otpCode == null) {
            return false;
        }

        if (!"ACTIVE".equals(otpCode.getStatus())) {
            return false;
        }

        if (otpCode.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpCodeDAO.updateStatus(otpCode.getId(), "EXPIRED");
            return false;
        }

        otpCodeDAO.updateStatus(otpCode.getId(), "USED");
        return true;
    }
}