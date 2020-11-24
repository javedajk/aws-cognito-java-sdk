package com.crud.model;

public class UserAttribute {
    private String accessToken;
    private String email;
    private String phonenumber;
    private boolean allowphonenumber;
    private boolean allowemail;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isAllowphonenumber() {
        return allowphonenumber;
    }

    public void setAllowphonenumber(boolean allowphonenumber) {
        this.allowphonenumber = allowphonenumber;
    }

    public boolean isAllowemail() {
        return allowemail;
    }

    public void setAllowemail(boolean allowemail) {
        this.allowemail = allowemail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
