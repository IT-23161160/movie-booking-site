package com.moviebooking.service;

import com.moviebooking.model.Screen;
import com.moviebooking.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreenService {

    @Autowired
    private ScreenRepository screenRepository;

    public List<Screen> getAll() {
        return screenRepository.findAll();
    }

    public void add(Screen s) {
        screenRepository.save(s);
    }

    public void update(String id, Screen s) {
        screenRepository.update(id, s);
    }

    public void delete(String id) {
        screenRepository.delete(id);
    }
}
