package com.example.transitpay;

public class User {
    String name, email, phone, password;
    boolean nfcActive, paymentStatus;
    PaymentInfo paymentInfo;

    public User() {
        // default;
    }


    public User(String name, String email, String phone, String password) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.nfcActive = false;
        this.paymentStatus = false;
    }

    public void copy(User copy) {
        this.name = copy.getName();
        this.email = copy.getEmail();
        this.phone = copy.getPhone();
        this.password = copy.getPassword();
        this.nfcActive = copy.isNfcActive();
        this.paymentStatus = copy.isPaymentStatus();
        this.paymentInfo = copy.getPaymentInfo();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isNfcActive() {
        return nfcActive;
    }

    public void setNfcActive(boolean nfcActive) {
        this.nfcActive = nfcActive;
    }

    public boolean isPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", nfcActive=" + nfcActive +
                ", paymentStatus=" + paymentStatus +
                ", paymentInfo=" + paymentInfo +
                '}';
    }
}
