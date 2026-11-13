package com.movieticket.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a confirmed ticket booking made by a user.
 */
public class Booking {
    private String id;
    private String userId;
    private String username;
    private String showId;
    private List<String> seats;
    private double totalAmount;
    private String status;
    private String bookingTime;

    public Booking() {
        seats = new ArrayList<>();
    }

    public Booking(String id, String userId, String username, String showId,
                   List<String> seats, double totalAmount, String status, String bookingTime) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.showId = showId;
        this.seats = seats != null ? seats : new ArrayList<>();
        this.totalAmount = totalAmount;
        this.status = status;
        this.bookingTime = bookingTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShowId() {
        return showId;
    }

    public void setShowId(String showId) {
        this.showId = showId;
    }

    public List<String> getSeats() {
        return seats;
    }

    public void setSeats(List<String> seats) {
        this.seats = seats != null ? seats : new ArrayList<>();
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getSeatList() {
        return String.join(", ", seats);
    }

    public String getAmountLabel() {
        return String.format(java.util.Locale.ENGLISH, "%.2f", totalAmount);
    }

    /** CSS-safe version of the booking state for JSP display. */
    public String getStatusCss() {
        return status == null ? "unknown" : status.toLowerCase(java.util.Locale.ENGLISH);
    }
}
