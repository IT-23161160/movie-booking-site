package com.moviebooking.controller;

import com.moviebooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("/{showtimeId}")
    public String showBookingPage(@PathVariable String showtimeId, Model model) {
        model.addAttribute("showtimeId", showtimeId);
        model.addAttribute("seats", bookingService.getAvailableSeats(showtimeId));
        return "bookings/book";
    }

    @PostMapping("/book")
    public String book(@RequestParam String showtimeId,
                       @RequestParam String seatNumber,
                       @RequestParam String nic,
                       Principal principal) {
        bookingService.book(showtimeId, principal.getName(), seatNumber, nic);
        return "redirect:/bookings/my";
    }

    @GetMapping("/my")
    public String myBookings(Model model, Principal principal) {
        model.addAttribute("myBookings", bookingService.getByUser(principal.getName()));
        return "bookings/my";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable String id) {
        bookingService.cancel(id);
        return "redirect:/bookings/my";
    }
}

