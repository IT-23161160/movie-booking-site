package com.moviebooking.controller;

import com.moviebooking.model.*;
import com.moviebooking.service.BookingService;
import com.moviebooking.service.MovieService;
import com.moviebooking.service.ShowtimeService;
import com.moviebooking.service.TheatreService;
import com.moviebooking.service.ScreenService;
import com.moviebooking.util.SeatFileUtil;
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
    @Autowired
    private SeatFileUtil seatFileUtil;

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
            Model model) {

        // Get and validate all required entities
        Optional<Movie> movie = movieService.getById(movieId);
        Optional<Theatre> theatre = theatreService.getById(theatreId);
        Optional<Screen> screen = screenService.getById(screenId);
        Optional<Showtime> showtime = showtimeService.getById(showtimeId);

        if (!movie.isPresent() || !theatre.isPresent() || !screen.isPresent() || !showtime.isPresent()) {
            return "redirect:/movies?error=invalid_selection";
        }

        // Initialize seats for this showtime if they don't exist
        seatFileUtil.initializeSeats(showtimeId, screen.get().getTotalSeats());

        model.addAttribute("movie", movie.get());
        model.addAttribute("theater", theatre.get());
        model.addAttribute("screen", screen.get());
        model.addAttribute("showtime", showtime.get());
        model.addAttribute("seats", bookingService.getAvailableSeats(showtimeId));

        return "bookings/select-seats";
    }

    @PostMapping("/confirm")
    public String confirmBooking(
            @RequestParam String movieId,
            @RequestParam String theaterId,
            @RequestParam String screenId,
            @RequestParam String showtimeId,
            @RequestParam String customerEmail,
            @RequestParam String seatNumber,
            @RequestParam String nic,
            Model model) {

        try {
            Booking booking = bookingService.createBooking(
                    showtimeId, movieId, theaterId, screenId,
                    customerEmail, seatNumber, nic
            );

            return "redirect:/bookings/confirmation/" + booking.getBookingId();
        } catch (IllegalStateException e) {
            // Redirect back with error message
            return "redirect:/bookings/select-seats?movieId=" + movieId +
                    "&theaterId=" + theaterId + "&screenId=" + screenId +
                    "&showtimeId=" + showtimeId + "&error=seat_taken";
        }
    }

    @GetMapping("/confirmation/{bookingId}")
    public String showConfirmation(@PathVariable String bookingId, Model model) {
        Optional<Booking> booking = bookingService.getById(bookingId);
        Optional<Movie> movie = movieService.getById(booking.get().getMovieId());
        Optional<Screen> screen = screenService.getById(booking.get().getScreenId());
        Optional<Theatre> theatre = theatreService.getById(booking.get().getTheaterId());
        Optional<Showtime> showtime = showtimeService.getById(booking.get().getShowtimeId());
        model.addAttribute("booking", bookingService.getById(bookingId).orElseThrow());
        model.addAttribute("movie", movie.get());
        model.addAttribute("screen", screen.get());
        model.addAttribute("theatre", theatre.get());
        model.addAttribute("showtime", showtime.get());
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