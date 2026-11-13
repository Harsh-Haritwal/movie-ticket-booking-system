package com.movieticket.servlet;

import com.movieticket.dao.BookingDao;
import com.movieticket.dao.ShowDao;
import com.movieticket.model.Show;
import com.movieticket.util.SessionUtil;
import com.movieticket.util.XmlUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Cancels a confirmed booking and releases its seats.
 */
@WebServlet("/cancel")
public class CancelServlet extends HttpServlet {

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

            String bookingId = req.getParameter("bookingId");
            synchronized (XmlUtil.DATA_LOCK) {
                BookingDao bookingDao = new BookingDao(dataDir());
                var booking = bookingDao.findById(bookingId);

                if (booking == null) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Booking not found");
                    return;
                }
                if (!booking.getUsername().equalsIgnoreCase(username)) {
                    resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Not your booking");
                    return;
                }

                if ("CONFIRMED".equalsIgnoreCase(booking.getStatus())) {
                    ShowDao showDao = new ShowDao(dataDir());
                    Show show = showDao.findById(booking.getShowId());
                    if (show == null) {
                        resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Show not found");
                        return;
                    }
                    if (!LocalDateTime.now().isBefore(showStart(show))) {
                        resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bookings cannot be cancelled after the show starts");
                        return;
                    }
                    java.util.List<String> originalSeats = new java.util.ArrayList<>(show.getBookedSeats());
                    java.util.List<String> updatedSeats = new java.util.ArrayList<>(originalSeats);
                    updatedSeats.removeAll(booking.getSeats());
                    showDao.updateBookedSeats(show.getId(), updatedSeats);
                    try {
                        bookingDao.cancel(bookingId);
                    } catch (Exception cancelFailure) {
                        showDao.updateBookedSeats(show.getId(), originalSeats);
                        throw cancelFailure;
                    }
                }
            }

            resp.sendRedirect(req.getContextPath() + "/my-bookings");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    private LocalDateTime showStart(Show show) {
        LocalDate date = LocalDate.parse(show.getDate());
        try {
            return LocalDateTime.of(date, LocalTime.parse(show.getTime(), DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)));
        } catch (Exception ignored) {
            return LocalDateTime.of(date, LocalTime.parse(show.getTime(), DateTimeFormatter.ofPattern("HH:mm")));
        }
    }
}
