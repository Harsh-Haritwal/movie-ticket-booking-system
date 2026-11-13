package com.movieticket.util;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Parses simple row declarations such as {@code A:12, B:10}. */
public final class SeatLayoutUtil {

    public static final int MAX_SEATS_PER_ROW = 12;

    private SeatLayoutUtil() {
    }

    public static List<String> seatsFor(String declaration, int fallbackTotalSeats) {
        List<String> seats = new ArrayList<>();
        for (List<String> rowSeats : rowsFor(declaration, fallbackTotalSeats).values()) {
            seats.addAll(rowSeats);
        }
        return seats;
    }

    public static Map<String, List<String>> rowsFor(String declaration, int fallbackTotalSeats) {
        if (declaration == null || declaration.isBlank()) {
            declaration = defaultDeclaration(fallbackTotalSeats);
        }

        Map<String, List<String>> rowsAndSeats = new LinkedHashMap<>();
        Set<String> rows = new LinkedHashSet<>();
        for (String part : declaration.trim().split("[,;\\n]+")) {
            String[] rowAndCount = part.trim().split(":");
            if (rowAndCount.length != 2) {
                throw new IllegalArgumentException("Use the format A:12, B:10 for the seat declaration.");
            }
            String row = rowAndCount[0].trim().toUpperCase(Locale.ENGLISH);
            int count;
            try {
                count = Integer.parseInt(rowAndCount[1].trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Each seat row must have a valid seat count.");
            }
            if (!row.matches("[A-Z]{1,3}") || count < 1 || count > MAX_SEATS_PER_ROW || !rows.add(row)) {
                throw new IllegalArgumentException("Rows must be unique letters with between 1 and "
                        + MAX_SEATS_PER_ROW + " seats.");
            }
            List<String> rowSeats = new ArrayList<>();
            for (int seat = 1; seat <= count; seat++) {
                rowSeats.add(row + seat);
            }
            rowsAndSeats.put(row, rowSeats);
        }
        if (rowsAndSeats.isEmpty()) {
            throw new IllegalArgumentException("Declare at least one seat row.");
        }
        return rowsAndSeats;
    }

    /** Parses a per-row price declaration such as {@code A:180, B:220}. */
    public static Map<String, Double> pricesFor(String declaration, Map<String, List<String>> rows,
                                                double fallbackPrice) {
        Map<String, Double> prices = new LinkedHashMap<>();
        if (declaration == null || declaration.isBlank()) {
            for (String row : rows.keySet()) {
                prices.put(row, fallbackPrice);
            }
            return prices;
        }
        for (String part : declaration.trim().split("[,;\\n]+")) {
            String[] rowAndPrice = part.trim().split(":");
            if (rowAndPrice.length != 2) {
                throw new IllegalArgumentException("Use the format A:180, B:220 for row prices.");
            }
            String row = rowAndPrice[0].trim().toUpperCase(Locale.ENGLISH);
            try {
                double price = Double.parseDouble(rowAndPrice[1].trim());
                if (price <= 0 || !rows.containsKey(row) || prices.put(row, price) != null) {
                    throw new IllegalArgumentException("Each declared row needs one positive ticket price.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Each row price must be a valid positive number.");
            }
        }
        if (prices.size() != rows.size()) {
            throw new IllegalArgumentException("Set a price for every seat row.");
        }
        return prices;
    }

    public static String defaultPriceDeclaration(String layout, int totalSeats, double price) {
        List<String> parts = new ArrayList<>();
        for (String row : rowsFor(layout, totalSeats).keySet()) {
            parts.add(row + ":" + String.format(Locale.ENGLISH, "%.2f", price));
        }
        return String.join(", ", parts);
    }

    public static String defaultDeclaration(int totalSeats) {
        if (totalSeats < 1) {
            return "";
        }
        List<String> parts = new ArrayList<>();
        int remaining = totalSeats;
        for (int row = 0; remaining > 0; row++) {
            int seats = Math.min(10, remaining);
            parts.add(String.valueOf((char) ('A' + row)) + ":" + seats);
            remaining -= seats;
        }
        return String.join(", ", parts);
    }

}
