package com.moviebooking.repository;

import com.moviebooking.model.Payment;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {

    private final String filePath = "data/payments.txt";
    private final Path paymentFile = Paths.get(filePath);
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public PaymentRepository() {
        ensureFileExists();
    }

    private void ensureFileExists() {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) parentDir.mkdirs();
            if (!file.exists()) file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(Payment p) {
        try (BufferedWriter writer = Files.newBufferedWriter(paymentFile, StandardOpenOption.APPEND)) {
            writer.write(format(p));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Payment> findAll() {
        List<Payment> payments = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(paymentFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                payments.add(new Payment(
                        parts[0], parts[1], parts[2], parts[3], // paymentId, userName, userEmail, phone
                        parts[4], Double.parseDouble(parts[5]), // bookingId, amount
                        parts[6], parts[7], // cardNumber, cardType
                        LocalDateTime.parse(parts[8], dtf), parts[9] // paymentTime, status
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return payments;
    }

    private String format(Payment payment) {
        return String.join("|",
                payment.getPaymentId(),
                payment.getUserName(),
                payment.getUserEmail(),
                payment.getPhone(),
                payment.getBookingId(),
                String.valueOf(payment.getAmount()),
                payment.getCardNumber(),
                payment.getCardType(),
                payment.getPaymentTime().format(dtf),
                payment.getStatus()
        );
    }
}

