package com.moviebooking.service;

import com.moviebooking.model.Booking;
import com.moviebooking.repository.BookingRepository;
import com.moviebooking.util.SeatFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatFileUtil seatUtil;

    private final Queue<Booking> bookingQueue = new LinkedList<>();

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public List<Booking> getByUser(String email) {
        return getAll().stream().filter(b -> b.getUserEmail().equalsIgnoreCase(email)).toList();
    }

    public void book(String showtimeId, String userEmail, String seatNumber, String nic) {
        String bookingId = UUID.randomUUID().toString().substring(0, 6);
        LocalDateTime now = LocalDateTime.now();

        Booking booking = new Booking(bookingId, showtimeId, userEmail, seatNumber, nic, now);
        bookingRepository.save(booking);
        bookingQueue.offer(booking);
        seatUtil.markSeatAsBooked(showtimeId, seatNumber);
    }

    public void cancel(String bookingId) {
        bookingRepository.delete(bookingId);
    }

    public List<String> getAvailableSeats(String showtimeId) {
        return seatUtil.getAvailableSeats(showtimeId);
    }

    public void initializeSeatsForShowtime(String showtimeId, int totalSeats) {
        seatUtil.initializeSeats(showtimeId, totalSeats);
    }
}

