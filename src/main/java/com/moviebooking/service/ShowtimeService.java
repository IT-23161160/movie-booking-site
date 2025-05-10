package com.moviebooking.service;

import com.moviebooking.model.Showtime;
import com.moviebooking.repository.ShowtimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// service/ShowtimeService.java
@Service
public class ShowtimeService {

    @Autowired
    private ShowtimeRepository showtimeRepository;

    public List<Showtime> getAll() {
        return showtimeRepository.findAll();
    }

    public void add(Showtime s) {
        showtimeRepository.save(s);
    }

    public void update(String id, Showtime s) {
        showtimeRepository.update(id, s);
    }

    public void delete(String id) {
        showtimeRepository.delete(id);
    }
}

