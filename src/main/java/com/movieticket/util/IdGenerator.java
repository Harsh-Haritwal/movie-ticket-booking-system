package com.movieticket.util;

import java.util.UUID;

/**
 * Generates simple unique IDs for bookings, movies, shows, and users.
 */
public final class IdGenerator {

    private IdGenerator() {
    }

    public static String nextBookingId() {
        return "BK" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public static String nextMovieId() {
        return "M" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    public static String nextShowId() {
        return "S" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    public static String nextUserId() {
        return "U" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
