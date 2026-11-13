package com.movieticket.model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import com.movieticket.util.SeatLayoutUtil;

/**
 * Represents a single showtime of a movie in a theatre.
 */
public class Show {
    private String id;
    private String movieId;
    private String theatre;
    private String screen;
    private String date;
    private String time;
    private double price;
    private int totalSeats;
    private String seatLayout;
    private String rowPrices;
    private List<String> bookedSeats;

    public Show() {
        bookedSeats = new ArrayList<>();
    }

    public Show(String id, String movieId, String theatre, String screen,
                String date, String time, double price, int totalSeats, List<String> bookedSeats) {
        this.id = id;
        this.movieId = movieId;
        this.theatre = theatre;
        this.screen = screen;
        this.date = date;
        this.time = time;
        this.price = price;
        this.totalSeats = totalSeats;
        this.bookedSeats = bookedSeats != null ? bookedSeats : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTheatre() {
        return theatre;
    }

    public void setTheatre(String theatre) {
        this.theatre = theatre;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /** Returns a value accepted by an HTML time input while preserving legacy AM/PM XML data. */
    public String getTimeInput() {
        if (time == null || time.isBlank()) {
            return "";
        }
        try {
            return LocalTime.parse(time, DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH))
                    .format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception ignored) {
            return time;
        }
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getSeatLayout() {
        return seatLayout == null || seatLayout.isBlank()
                ? SeatLayoutUtil.defaultDeclaration(totalSeats)
                : seatLayout;
    }

    public void setSeatLayout(String seatLayout) {
        this.seatLayout = seatLayout;
    }

    public String getRowPrices() {
        return rowPrices == null || rowPrices.isBlank()
                ? SeatLayoutUtil.defaultPriceDeclaration(getSeatLayout(), totalSeats, price)
                : rowPrices;
    }

    public void setRowPrices(String rowPrices) {
        this.rowPrices = rowPrices;
    }

    public List<String> getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(List<String> bookedSeats) {
        this.bookedSeats = bookedSeats != null ? bookedSeats : new ArrayList<>();
    }

    public int getAvailableSeats() {
        return totalSeats - bookedSeats.size();
    }

    public boolean isSeatBooked(String seat) {
        return bookedSeats.contains(seat);
    }
}
