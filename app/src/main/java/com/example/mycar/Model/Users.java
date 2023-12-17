package com.example.mycar.Model;

public class Users {
    private String name, phone, pass;
    public  Users()
    {



    }

    public Users(String name, String phone, String pass) {
        this.name = name;
        this.phone = phone;
        this.pass = pass;
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

