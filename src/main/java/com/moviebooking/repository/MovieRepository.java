package com.moviebooking.repository;

import com.moviebooking.model.Movie;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MovieRepository {

    private final String filePath = "data/movies.txt";
    private final Path movieFile = Paths.get(filePath);
    private final String imageUploadDir = "uploads/images/";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MovieRepository() {
        ensureFileExists();
        ensureUploadDirExists();
    }

    private void ensureFileExists() {
        try {
            File file = new File(filePath);
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ensureUploadDirExists() {
        try {
            Files.createDirectories(Paths.get(imageUploadDir));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Movie> findAllSortedByReleaseDate() {
        List<Movie> movies = loadAll();
        insertionSortByDate(movies);
        return movies;
    }

    public void save(Movie movie, MultipartFile imageFile) throws IOException {
        // Save the image file
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Files.copy(imageFile.getInputStream(), Paths.get(imageUploadDir + fileName),
                    StandardCopyOption.REPLACE_EXISTING);
            movie.setImageName(fileName);
        }

        // Save movie data to text file
        try (BufferedWriter writer = Files.newBufferedWriter(movieFile, StandardOpenOption.APPEND)) {
            writer.write(format(movie));
            writer.newLine();
        }
    }

    public void update(String title, Movie updatedMovie) {
        List<Movie> all = loadAll();
        try (BufferedWriter writer = Files.newBufferedWriter(movieFile)) {
            for (Movie m : all) {
                if (m.getTitle().equalsIgnoreCase(title)) {
                    writer.write(format(updatedMovie));
                } else {
                    writer.write(format(m));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void delete(String title) {
        List<Movie> all = loadAll();
        try (BufferedWriter writer = Files.newBufferedWriter(movieFile)) {
            for (Movie m : all) {
                if (!m.getTitle().equalsIgnoreCase(title)) {
                    writer.write(format(m));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Movie> loadAll() {
        List<Movie> list = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(movieFile)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 6) {
                    list.add(new Movie(
                            parts[0], parts[1],
                            LocalDate.parse(parts[2], formatter),
                            Integer.parseInt(parts[3]),
                            parts[4],
                            Double.parseDouble(parts[5])
                    ));
                }
            }
        } catch (IOException e) {
            // Ignore if file doesn't exist
        }
        return list;
    }

    private String format(Movie m) {
        return String.join("|",
                m.getTitle(),
                m.getGenre(),
                m.getReleaseDate().format(formatter),
                String.valueOf(m.getDuration()),
                m.getImageName(),  // Now stores filename instead of URL
                String.valueOf(m.getTicketPrice())
        );
    }

    private void insertionSortByDate(List<Movie> movies) {
        for (int i = 1; i < movies.size(); i++) {
            Movie key = movies.get(i);
            int j = i - 1;
            while (j >= 0 && movies.get(j).getReleaseDate().isAfter(key.getReleaseDate())) {
                movies.set(j + 1, movies.get(j));
                j--;
            }
            movies.set(j + 1, key);
        }
    }
}

