package com.movieticket.servlet;

import com.movieticket.dao.BookingDao;
import com.movieticket.dao.ShowDao;
import com.movieticket.model.Booking;
import com.movieticket.model.Show;
import com.movieticket.util.IdGenerator;
import com.movieticket.util.SessionUtil;
import com.movieticket.util.SeatLayoutUtil;
import com.movieticket.util.XmlUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Booking processing: validates seats, saves booking, updates show booked seats.
 */
@WebServlet("/book")
public class BookingServlet extends HttpServlet {

    private File dataDir() {
        String prop = System.getProperty("app.data.dir");
        if (prop != null) {
            return new File(prop);
        }
        String real = getServletContext().getRealPath("/WEB-INF/data");
        if (real != null && new File(real).exists()) {
            return new File(real);
        }
        return new File(getServletContext().getRealPath("/"), "WEB-INF/data");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            HttpSession session = req.getSession(false);
            String username = SessionUtil.getUsername(session);
            if (username == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please log in first.");
                return;
            }

            String showId = req.getParameter("showId");
            String[] seatParams = req.getParameterValues("seat");
            List<String> selected = seatParams == null ? List.of() : java.util.Arrays.asList(seatParams);

            if (showId == null || showId.isBlank() || selected.isEmpty()) {
                req.setAttribute("error", "Please select at least one seat.");
                forwardToSeats(req, resp, showId);
                return;
            }

            // Normalize and dedupe seats
            Set<String> seats = new LinkedHashSet<>();
            for (String s : selected) {
                if (s != null) {
                    seats.add(s.trim().toUpperCase());
                }
            }

            ShowDao showDao = new ShowDao(dataDir());
            synchronized (XmlUtil.DATA_LOCK) {
                // Reload inside the lock so two requests cannot reserve the same seat.
                Show show = showDao.findById(showId);
                if (show == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Show not found");
                    return;
                }

                List<String> validSeats = SeatLayoutUtil.seatsFor(show.getSeatLayout(), show.getTotalSeats());
                for (String s : seats) {
                    if (!validSeats.contains(s)) {
                        req.setAttribute("error", "Invalid seat: " + s);
                        forwardToSeats(req, resp, showId);
                        return;
                    }
                    if (show.isSeatBooked(s)) {
                        req.setAttribute("error", "Seat already booked: " + s);
                        forwardToSeats(req, resp, showId);
                        return;
                    }
                }

                java.util.Map<String, Double> rowPrices = SeatLayoutUtil.pricesFor(show.getRowPrices(),
                        SeatLayoutUtil.rowsFor(show.getSeatLayout(), show.getTotalSeats()), show.getPrice());
                double total = 0;
                for (String seat : seats) {
                    String row = seat.replaceAll("\\d+$", "");
                    total += rowPrices.get(row);
                }
                String id = IdGenerator.nextBookingId();
                String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                Booking booking = new Booking(id, username, username, showId,
                        new ArrayList<>(seats), total, "CONFIRMED", time);

                List<String> originalSeats = new ArrayList<>(show.getBookedSeats());
                List<String> updatedSeats = new ArrayList<>(originalSeats);
                updatedSeats.addAll(seats);
                showDao.updateBookedSeats(showId, updatedSeats);
                try {
                    new BookingDao(dataDir()).add(booking);
                } catch (Exception bookingFailure) {
                    showDao.updateBookedSeats(showId, originalSeats);
                    throw bookingFailure;
                }

                show.setBookedSeats(updatedSeats);
                req.setAttribute("booking", booking);
                req.setAttribute("show", show);
                req.getRequestDispatcher("/WEB-INF/views/confirmation.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private void forwardToSeats(HttpServletRequest req, HttpServletResponse resp, String showId)
            throws ServletException, IOException {
        try {
            if (showId != null) {
                Show show = new ShowDao(dataDir()).findById(showId);
                req.setAttribute("show", show);
                req.setAttribute("allSeats", SeatLayoutUtil.seatsFor(show.getSeatLayout(), show.getTotalSeats()));
                req.setAttribute("seatRows", SeatLayoutUtil.rowsFor(show.getSeatLayout(), show.getTotalSeats()));
            }
            req.getRequestDispatcher("/WEB-INF/views/seat-select.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private List<String> generateSeats(int totalSeats) {
        java.util.List<String> seats = new java.util.ArrayList<>();
        int cols = 10;
        int rows = (int) Math.ceil(totalSeats / (double) cols);
        int n = 0;
        for (int r = 0; r < rows && n < totalSeats; r++) {
            char row = (char) ('A' + r);
            for (int c = 1; c <= cols && n < totalSeats; c++, n++) {
                seats.add("" + row + c);
            }
        }
        return seats;
    }
}
