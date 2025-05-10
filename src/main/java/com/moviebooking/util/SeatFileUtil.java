package com.moviebooking.util;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class SeatFileUtil {

    public void initializeSeats(String showtimeId, int totalSeats) {
        String seatFilePath = "data/seats_" + showtimeId + ".txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(seatFilePath))) {
            for (int i = 1; i <= totalSeats; i++) {
                String seat = "S" + i;
                writer.write(seat + "|available");
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAvailableSeats(String showtimeId) {
        String seatFilePath = "data/seats_" + showtimeId + ".txt";
        List<String> available = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(seatFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if ("available".equalsIgnoreCase(parts[1])) {
                    available.add(parts[0]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return available;
    }

    public void markSeatAsBooked(String showtimeId, String seat) {
        String seatFilePath = "data/seats_" + showtimeId + ".txt";
        List<String> updated = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(seatFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts[0].equals(seat)) {
                    updated.add(parts[0] + "|booked");
                } else {
                    updated.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(seatFilePath))) {
            for (String s : updated) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

