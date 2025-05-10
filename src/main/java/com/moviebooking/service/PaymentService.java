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

    public void pay(String userEmail, String bookingId, double amount) {
        String id = UUID.randomUUID().toString().substring(0, 6);
        Payment payment = new Payment(id, userEmail, bookingId, amount, LocalDateTime.now());
        paymentRepository.save(payment);
    }

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public List<Payment> getByUser(String email) {
        return getAll().stream()
                .filter(p -> p.getUserEmail().equalsIgnoreCase(email))
                .toList();
    }
}

