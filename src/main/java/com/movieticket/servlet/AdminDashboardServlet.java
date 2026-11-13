package com.movieticket.servlet;

import com.movieticket.dao.BookingDao;
import com.movieticket.dao.MovieDao;
import com.movieticket.dao.ShowDao;
import com.movieticket.model.Booking;
import com.movieticket.model.Movie;
import com.movieticket.model.Show;
import com.movieticket.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Admin dashboard: summary counts and recent bookings.
 */
@WebServlet("/admin")
public class AdminDashboardServlet extends HttpServlet {

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !SessionUtil.isStaff(session)) {
            resp.sendRedirect(req.getContextPath() + "/login?next=/admin");
            return;
        }
        if ("movie_manager".equalsIgnoreCase(SessionUtil.getRole(session))) {
            resp.sendRedirect(req.getContextPath() + "/admin/movies");
            return;
        }
        if ("show_manager".equalsIgnoreCase(SessionUtil.getRole(session))) {
            resp.sendRedirect(req.getContextPath() + "/admin/shows");
            return;
        }
        if ("booking_viewer".equalsIgnoreCase(SessionUtil.getRole(session))) {
            resp.sendRedirect(req.getContextPath() + "/admin/bookings");
            return;
        }
        try {
            File dir = dataDir();
            req.setAttribute("movieCount", new MovieDao(dir).findAll().size());
            req.setAttribute("showCount", new ShowDao(dir).findAll().size());
            List<Booking> all = new BookingDao(dir).findAll();
            req.setAttribute("bookingCount", all.size());
            java.util.Collections.reverse(all);
            req.setAttribute("recentBookings", all.subList(0, Math.min(5, all.size())));
            req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
