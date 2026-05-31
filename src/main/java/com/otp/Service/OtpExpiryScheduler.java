package com.otp.Service;

import com.otp.Dao.OTPCodeDAO;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OtpExpiryScheduler {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final OTPCodeDAO otpCodeDAO = new OTPCodeDAO();

    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                otpCodeDAO.markExpired();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public void stop() {
        scheduler.shutdown();
    }
}