package com.moviebooking.repository;

import com.moviebooking.model.Booking;
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
import java.util.Optional;

@Repository
public class BookingRepository {

    private final String filePath = "data/bookings.txt";
    private final Path bookingFile = Paths.get(filePath);
    private final DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public BookingRepository() {
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

    public List<Booking> findAll() {
        List<Booking> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(bookingFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\\|");
                list.add(new Booking(
                        p[0],  // bookingId
                        p[1],  // movieId
                        p[2],  // theaterId
                        p[3],  // screenId (new)
                        p[4],  // showtimeId
                        p[5],  // userEmail
                        p[6],  // seatNumber
                        p[7],  // nic
                        LocalDateTime.parse(p[8], dtFormatter)  // bookingTime
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<Booking> findById(String bookingId) {
        return findAll().stream()
                .filter(b -> b.getBookingId().equals(bookingId))
                .findFirst();
    }

    public void save(Booking booking) {
        try (BufferedWriter writer = Files.newBufferedWriter(bookingFile, StandardOpenOption.APPEND)) {
            writer.write(format(booking));
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String bookingId) {
        List<Booking> all = findAll();
        try (BufferedWriter writer = Files.newBufferedWriter(bookingFile)) {
            for (Booking b : all) {
                if (!b.getBookingId().equalsIgnoreCase(bookingId)) {
                    writer.write(format(b));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Booking> findByShowtime(String showtimeId) {
        return findAll().stream()
                .filter(b -> b.getShowtimeId().equals(showtimeId))
                .toList();
    }

    public List<Booking> findByUserEmail(String email) {
        return findAll().stream()
                .filter(b -> b.getUserEmail().equalsIgnoreCase(email))
                .toList();
    }

    private String format(Booking b) {
        return String.join("|",
                b.getBookingId(),
                b.getMovieId(),
                b.getTheaterId(),
                b.getScreenId(),  // Added screenId
                b.getShowtimeId(),
                b.getUserEmail(),
                b.getSeatNumber(),
                b.getNic(),
                b.getBookingTime().format(dtFormatter)
        );
    }
}