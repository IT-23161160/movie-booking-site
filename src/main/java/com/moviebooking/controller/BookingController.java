package com.moviebooking.controller;

import com.moviebooking.model.*;
import com.moviebooking.service.BookingService;
import com.moviebooking.service.MovieService;
import com.moviebooking.service.ShowtimeService;
import com.moviebooking.service.TheatreService;
import com.moviebooking.service.ScreenService;
import com.moviebooking.util.SeatFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String showShowtimeSelection(
            @RequestParam String movieId,
            @RequestParam(required = false) String theatreId,
            @RequestParam(required = false) String screenId,
            Model model) {

        Optional<Movie> movie = movieService.getById(movieId);
        if (!movie.isPresent()) {
            return "redirect:/movies";
        }

        model.addAttribute("movie", movie.get());
        model.addAttribute("theatres", theatreService.getAll());

        if (theatreId != null) {
            model.addAttribute("selectedTheatreId", theatreId);
            model.addAttribute("screens", screenService.getAll());

            if (screenId != null) {
                model.addAttribute("selectedScreenId", screenId);
                model.addAttribute("showtimes", showtimeService.getByMovieId(movieId));
            }
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
            @RequestParam String seatNumber,
            Model model) {

        try {
            Booking booking = bookingService.createBooking(
                    showtimeId, movieId, theaterId, screenId,
                    seatNumber
            );

            return "redirect:/payments/new/" + booking.getBookingId();
        } catch (IllegalStateException e) {
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