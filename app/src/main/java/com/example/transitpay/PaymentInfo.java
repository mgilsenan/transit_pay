package com.example.transitpay;

public class PaymentInfo {
    String name, cardNumber, expireDate, cvc;

    public PaymentInfo(String name, String cardNumber, String expireDate, String cvc) {
        this.name = name;
        this.cardNumber = cardNumber;
        this.expireDate = expireDate;
        this.cvc = cvc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    @Override
    public String toString() {
        return "PaymentInfo{" +
                "name='" + name + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", expireDate='" + expireDate + '\'' +
                ", cvc='" + cvc + '\'' +
                '}';
    }
}
