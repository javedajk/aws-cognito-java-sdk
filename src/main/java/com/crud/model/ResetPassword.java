package com.crud.model;

public class ResetPassword {
    private String email;
    private String verificationCode;
    private String oldPassword;
    private String newPassword;
    private String accessToken;
    private boolean phone_number_verify;
    private boolean email_verify;

    public boolean isPhone_number_verify() {
        return phone_number_verify;
    }

    public void setPhone_number_verify(boolean phone_number_verify) {
        this.phone_number_verify = phone_number_verify;
    }

    public boolean isEmail_verify() {
        return email_verify;
    }

    public void setEmail_verify(boolean email_verify) {
        this.email_verify = email_verify;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
