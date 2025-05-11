package com.moviebooking.controller;

import com.moviebooking.model.Booking;
import com.moviebooking.model.Movie;
import com.moviebooking.service.BookingService;
import com.moviebooking.service.MovieService;
import com.moviebooking.service.ShowtimeService;
import com.moviebooking.service.TheatreService;
import com.moviebooking.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    private static final Logger logger = LoggerFactory.getLogger(BookingController.class);

    @Autowired
    private BookingService bookingService;
    @Autowired
    private MovieService movieService;
    @Autowired
    private TheatreService theatreService;
    @Autowired
    private ScreenService screenService;
    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping("/select-showtime")
    public String showShowtimeSelection(@RequestParam String movieId, Model model) {
        Optional<Movie> movie = movieService.getById(movieId);

        if (!movie.isPresent()) {
            logger.warn("Movie not found with ID: {}", movieId);
            model.addAttribute("error", "Movie not found");
            return "bookings/select-showtime"; // Return to template with error message
        }

        // If movie exists, add all attributes to model
        model.addAttribute("movie", movie.get());
        model.addAttribute("theatres", theatreService.getAll());
        model.addAttribute("screens", screenService.getAll());
        model.addAttribute("showtimes", showtimeService.getByMovieId(movieId));

        return "bookings/select-showtime";
    }

    @GetMapping("/select-seats")
    public String showSeatSelection(
            @RequestParam String movieId,
            @RequestParam String theatreId,
            @RequestParam String screenId,
            @RequestParam String showtimeId,
            Model model,
            @AuthenticationPrincipal User user) {

        // Handle unauthenticated users
        String userEmail = (user != null) ? user.getUsername() : "guest@example.com";

        model.addAttribute("movie", movieService.getById(movieId));
        model.addAttribute("theatre", theatreService.getById(theatreId));
        model.addAttribute("screen", screenService.getById(screenId));
        model.addAttribute("showtime", showtimeService.getById(showtimeId));
        model.addAttribute("seats", bookingService.getAvailableSeats(showtimeId));
        model.addAttribute("userEmail", userEmail);

        return "bookings/select-seats";
    }

    @PostMapping("/confirm")
    public String confirmBooking(
            @RequestParam String movieId,
            @RequestParam String theaterId,
            @RequestParam String screenId,
            @RequestParam String showtimeId,
            @RequestParam String userEmail,
            @RequestParam String seatNumber,
            @RequestParam String nic,
            Model model) {

        Booking booking = bookingService.createBooking(movieId, theaterId, screenId, showtimeId, userEmail, seatNumber, nic);
        model.addAttribute("booking", booking);
        return "redirect:/bookings/confirmation/" + booking.getBookingId();
    }

    @GetMapping("/confirmation/{bookingId}")
    public String showConfirmation(@PathVariable String bookingId, Model model) {
        model.addAttribute("booking", bookingService.getById(bookingId).orElseThrow());
        return "bookings/confirmation";
    }

    @GetMapping("/my")
    public String myBookings(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("bookings", bookingService.getByUser(user.getUsername()));
        return "bookings/my-bookings";
    }

    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable String bookingId) {
        bookingService.cancelBooking(bookingId);
        return "redirect:/bookings/my";
    }
}