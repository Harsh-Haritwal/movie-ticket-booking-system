package com.movieticket.servlet;

import com.movieticket.dao.BookingDao;
import com.movieticket.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/** Read-only booking register for the head administrator and booking-viewer staff. */
@WebServlet("/admin/bookings")
public class AdminBookingsServlet extends HttpServlet {
    private File dataDir() { return new File(getServletContext().getRealPath("/WEB-INF/data")); }
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String role = SessionUtil.getRole(req.getSession(false));
        if (!("admin".equalsIgnoreCase(role) || "booking_viewer".equalsIgnoreCase(role))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Booking-view permission required"); return;
        }
        try {
            List<com.movieticket.model.Booking> bookings = new BookingDao(dataDir()).findAll();
            Collections.reverse(bookings);
            req.setAttribute("bookings", bookings);
            req.getRequestDispatcher("/WEB-INF/views/admin/bookings.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }
}
