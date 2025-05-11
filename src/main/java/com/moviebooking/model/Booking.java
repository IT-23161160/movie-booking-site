package com.moviebooking.model;

import java.time.LocalDateTime;

public class Booking {
    private String bookingId;
    private String movieId;
    private String theaterId;
    private String screenId;// Added field
    private String showtimeId;
    private String userEmail;
    private String seatNumber;
    private String nic;
    private LocalDateTime bookingTime;

    public Booking() {}

    public Booking(String bookingId, String movieId, String theaterId,String screenId, String showtimeId,
                   String userEmail, String seatNumber, String nic,
                   LocalDateTime bookingTime) {
        this.bookingId = bookingId;
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.screenId = screenId;
        this.showtimeId = showtimeId;
        this.userEmail = userEmail;
        this.seatNumber = seatNumber;
        this.nic = nic;
        this.bookingTime = bookingTime;
    }

    // Getters and Setters for all fields
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(String showtimeId) {
        this.showtimeId = showtimeId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(LocalDateTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(String theaterId) {
        this.theaterId = theaterId;
    }

    public String getScreenId() {
        return screenId;
    }

    public void setScreenId(String screenId) {
        this.screenId = screenId;
    }
}