package com.example.transitpay.User;

public class User {
    // we no longer save password in real time database because authentication has it
    public String name, email, phone, password;

    public User() {
        // default;
    }


    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;

    }

    public void copy(User copy) {
        this.name = copy.getName();
        this.email = copy.getEmail();
        this.phone = copy.getPhone();

    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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






    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}