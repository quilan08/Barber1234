package com.example.barberchair.Model;

public class ModelUser {
    public  String image;
    public String email;
    public String phone;
    public String cover;
    public String userid;
    public String name;


    public ModelUser() {
    }


    public ModelUser(String image, String email, String phone, String cover, String userid, String name) {
        this.image = image;
        this.email = email;
        this.phone = phone;
        this.cover = cover;
        this.userid = userid;
        this.name = name;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }






}
