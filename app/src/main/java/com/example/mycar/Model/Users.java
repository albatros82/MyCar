package com.example.mycar.Model;

public class Users {
    private String name, phone, pass, image;
    public  Users()
    {



    }

    public Users(String name, String phone, String pass, String image) {
        this.name = name;
        this.phone = phone;
        this.pass = pass;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

