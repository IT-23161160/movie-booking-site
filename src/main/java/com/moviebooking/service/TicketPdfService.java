package com.moviebooking.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.moviebooking.model.Booking;
import com.moviebooking.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class TicketPdfService {

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

    public byte[] generateTicketPdf(Booking booking) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);

        document.open();

        // Add title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        Paragraph title = new Paragraph("MOVIE TICKET", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Add movie details
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font contentFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);

        addTableRow(table, "Movie:",movieService.getById(booking.getMovieId()).get().getTitle(), headerFont, contentFont);
        addTableRow(table, "Theatre:", theatreService.getById(booking.getTheaterId()).get().getLocation(), headerFont, contentFont);
        addTableRow(table, "Screen:", screenService.getById(booking.getScreenId()).get().getScreenName(), headerFont, contentFont);
        addTableRow(table, "Date:", showtimeService.getById(booking.getShowtimeId()).get().getDate().toString(), headerFont, contentFont);
        addTableRow(table, "Time:", showtimeService.getById(booking.getShowtimeId()).get().getTime().format(DateTimeFormatter.ofPattern("HH:mm")), headerFont, contentFont);
        addTableRow(table, "Seats:", booking.getSeatNumber(), headerFont, contentFont);
        addTableRow(table, "Booking ID:", booking.getBookingId(), headerFont, contentFont);

        document.add(table);

        // Add thank you message
        Paragraph thanks = new Paragraph("Enjoy the show!", contentFont);
        thanks.setAlignment(Element.ALIGN_CENTER);
        thanks.setSpacingBefore(20);
        document.add(thanks);

        document.close();
        return outputStream.toByteArray();
    }

    private void addTableRow(PdfPTable table, String header, String content, Font headerFont, Font contentFont) {
        PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
        headerCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell contentCell = new PdfPCell(new Phrase(content, contentFont));
        contentCell.setBorder(Rectangle.NO_BORDER);

        table.addCell(headerCell);
        table.addCell(contentCell);
    }
}