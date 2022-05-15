package com.devpro.airj18bookingapp.models;

public class ResetPass {
    public String userEmail;
    public String newPassword;
    public String confirmNewPassword;

    public ResetPass(String userEmail, String newPassword, String confirmNewPassword) {
        this.userEmail = userEmail;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }
}
