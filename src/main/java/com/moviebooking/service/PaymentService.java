package com.moviebooking.service;

import com.moviebooking.model.Payment;
import com.moviebooking.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    public Payment processPayment(String userName, String userEmail, String phone,
                                  String bookingId, double amount,
                                  String cardNumber, String cardType) {
        String paymentId = UUID.randomUUID().toString().substring(0, 8);
        Payment payment = new Payment(
                paymentId, userName, userEmail, phone,
                bookingId, amount,
                cardNumber.substring(cardNumber.length() - 4), // Store only last 4 digits
                cardType,
                LocalDateTime.now(),
                "SUCCESS"
        );

        paymentRepository.save(payment);
        return payment;
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByUser(String email) {
        return paymentRepository.findAll().stream()
                .filter(p -> p.getUserEmail().equalsIgnoreCase(email))
                .toList();
    }
}

