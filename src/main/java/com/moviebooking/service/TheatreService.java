package com.moviebooking.service;

import com.moviebooking.model.Theatre;
import com.moviebooking.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    @Autowired
    private TheatreRepository theatreRepository;

    public List<Theatre> getAll() {
        return theatreRepository.findAll();
    }

    public void add(Theatre t) {
        theatreRepository.save(t);
    }

    public void update(String id, Theatre t) {
        theatreRepository.update(id, t);
    }

    public void delete(String id) {
        theatreRepository.delete(id);
    }
}

