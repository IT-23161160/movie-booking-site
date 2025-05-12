package com.moviebooking.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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

import java.util.List;
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
            return "bookings/select-showtime";
        }

        // Get all necessary data
        List<Theatre> theatres = theatreService.getAll();
        List<Screen> screens = screenService.getAll();
        List<Showtime> showtimes = showtimeService.getByMovieId(movieId);

        // Add attributes to model
        model.addAttribute("movie", movie.get());
        model.addAttribute("theatres", theatres);

        // Convert collections to JSON for JavaScript processing
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Register JavaTimeModule for proper LocalDate/LocalTime serialization
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            model.addAttribute("screensJson", mapper.writeValueAsString(screens));
            model.addAttribute("showtimesJson", mapper.writeValueAsString(showtimes));
        } catch (JsonProcessingException e) {
            logger.error("Error converting to JSON", e);
            // Fallback - template will handle empty data
            model.addAttribute("screensJson", "[]");
            model.addAttribute("showtimesJson", "[]");
        }

        return "bookings/select-showtime";
    }

    @GetMapping("/select-seats")
    public String showSeatSelection(
            @RequestParam String movieId,
            @RequestParam String theatreId,
            @RequestParam String screenId,
            @RequestParam String showtimeId,
            Model model) {

        Optional<Movie> movie = movieService.getById(movieId);
        Optional<Theatre> theatre = theatreService.getById(theatreId);
        Optional<Screen> screen = screenService.getById(screenId);
        Optional<Showtime> showtime = showtimeService.getById(showtimeId);

        if (!movie.isPresent() || !theatre.isPresent() || !screen.isPresent() || !showtime.isPresent()) {
            return "redirect:/movies?error=invalid_selection";
        }

        seatFileUtil.initializeSeats(showtimeId, screen.get().getTotalSeats());

        model.addAttribute("movie", movie.get());
        model.addAttribute("theater", theatre.get());
        model.addAttribute("screen", screen.get());
        model.addAttribute("showtime", showtime.get());
        model.addAttribute("allSeats", seatFileUtil.getAllSeats(showtimeId));
        model.addAttribute("bookedSeats", bookingService.getBookedSeats(showtimeId));

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


    @PostMapping("/cancel/{bookingId}")
    public String cancelBooking(@PathVariable String bookingId) {
        bookingService.cancelBooking(bookingId);
        return "redirect:/bookings/my";
    }
}