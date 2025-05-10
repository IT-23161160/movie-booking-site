package com.moviebooking.service;

import com.moviebooking.model.Movie;
import com.moviebooking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

// service/MovieService.java
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAll() {
        return movieRepository.findAllSortedByReleaseDate();
    }

    public void add(Movie movie, MultipartFile imageFile) throws IOException {
        movieRepository.save(movie, imageFile);
    }

    public void update(String title, Movie updatedMovie) {
        movieRepository.update(title, updatedMovie);
    }

    public void delete(String title) {
        movieRepository.delete(title);
    }
}

