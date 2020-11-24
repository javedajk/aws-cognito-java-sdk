package com.crud.model;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class ImageUpload implements Serializable{

    private String imagename;
    private String base64;

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
}
