package com.otp.Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class OtpFileUtil {
    private static final Path FILE_PATH = Paths.get("output", "otp.txt");

    public static void save(String text) {
        try {
            Files.createDirectories(FILE_PATH.getParent());
            Files.writeString(
                    FILE_PATH,
                    text + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Cannot write OTP to file", e);
        }
    }
}