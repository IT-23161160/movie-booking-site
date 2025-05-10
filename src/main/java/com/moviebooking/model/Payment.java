package com.moviebooking.model;

import java.time.LocalDateTime;

public class Payment {
    private String paymentId;
    private String userEmail;
    private String bookingId;
    private double amount;
    private LocalDateTime paymentTime;

    public Payment() {}

    public Payment(String paymentId, String userEmail, String bookingId, double amount, LocalDateTime paymentTime) {
        this.paymentId = paymentId;
        this.userEmail = userEmail;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentTime = paymentTime;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }
}

