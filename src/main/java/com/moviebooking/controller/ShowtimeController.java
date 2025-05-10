package com.moviebooking.controller;

import com.moviebooking.model.Showtime;
import com.moviebooking.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/showtimes")
public class ShowtimeController {

    @Autowired
    private ShowtimeService showtimeService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("showtimes", showtimeService.getAll());
        return "showtimes/list";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("showtime", new Showtime());
        return "showtimes/add";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public String add(@ModelAttribute Showtime s) {
        showtimeService.add(s);
        return "redirect:/showtimes";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable String id, Model model) {
        Showtime found = showtimeService.getAll().stream().filter(s -> s.getShowtimeId().equalsIgnoreCase(id)).findFirst().orElse(null);
        model.addAttribute("showtime", found);
        return "showtimes/edit";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/edit")
    public String edit(@ModelAttribute Showtime s) {
        showtimeService.update(s.getShowtimeId(), s);
        return "redirect:/showtimes";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        showtimeService.delete(id);
        return "redirect:/showtimes";
    }
}

