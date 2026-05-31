package com.otp.Model;

public class OTPConfig {
    private int id;
    private int codeLength;
    private int lifetimeSeconds;

    public OTPConfig() {}

    public OTPConfig(int id, int codeLength, int lifetimeSeconds) {
        this.id = id;
        this.codeLength = codeLength;
        this.lifetimeSeconds = lifetimeSeconds;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCodeLength() { return codeLength; }
    public void setCodeLength(int codeLength) { this.codeLength = codeLength; }

    public int getLifetimeSeconds() { return lifetimeSeconds; }
    public void setLifetimeSeconds(int lifetimeSeconds) { this.lifetimeSeconds = lifetimeSeconds; }
}