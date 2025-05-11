package com.moviebooking.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class SeatFileUtil {
    private static final Logger logger = LoggerFactory.getLogger(SeatFileUtil.class);
    private static final String DATA_DIR = "data/";

    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            logger.error("Failed to create data directory", e);
        }
    }

    public void initializeSeats(String showtimeId, int totalSeats) {
        Path seatFile = Paths.get(DATA_DIR + "seats_" + showtimeId + ".txt");
        try {
            if (!Files.exists(seatFile)) {
                List<String> seats = new ArrayList<>();
                for (int i = 1; i <= totalSeats; i++) {
                    seats.add("S" + i + "|available");
                }
                Files.write(seatFile, seats);
                logger.info("Created seat file for showtime: {}", showtimeId);
            }
        } catch (IOException e) {
            logger.error("Failed to initialize seats for showtime: " + showtimeId, e);
        }
    }

    public List<String> getAvailableSeats(String showtimeId) {
        Path seatFile = Paths.get(DATA_DIR + "seats_" + showtimeId + ".txt");
        List<String> available = new ArrayList<>();

        try {
            if (!Files.exists(seatFile)) {
                logger.warn("Seat file not found for showtime: {}", showtimeId);
                return available;
            }

            for (String line : Files.readAllLines(seatFile)) {
                String[] parts = line.split("\\|");
                if (parts.length > 1 && "available".equalsIgnoreCase(parts[1])) {
                    available.add(parts[0]);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading seat file for showtime: " + showtimeId, e);
        }
        return available;
    }

    public void markSeatAsBooked(String showtimeId, String seat) {
        Path seatFile = Paths.get(DATA_DIR + "seats_" + showtimeId + ".txt");
        try {
            if (!Files.exists(seatFile)) {
                logger.error("Seat file not found for showtime: {}", showtimeId);
                return;
            }

            List<String> updated = new ArrayList<>();
            for (String line : Files.readAllLines(seatFile)) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(seat)) {
                    updated.add(parts[0] + "|booked");
                } else {
                    updated.add(line);
                }
            }
            Files.write(seatFile, updated, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            logger.error("Error updating seat status for showtime: " + showtimeId, e);
        }
    }

    public List<String> getAllSeats(String showtimeId) {
        Path seatFile = Paths.get(DATA_DIR + "seats_" + showtimeId + ".txt");
        List<String> allSeats = new ArrayList<>();

        try {
            if (!Files.exists(seatFile)) {
                logger.warn("Seat file not found for showtime: {}", showtimeId);
                return allSeats;
            }

            for (String line : Files.readAllLines(seatFile)) {
                allSeats.add(line.split("\\|")[0]);
            }
        } catch (IOException e) {
            logger.error("Error reading all seats for showtime: " + showtimeId, e);
        }
        return allSeats;
    }
}