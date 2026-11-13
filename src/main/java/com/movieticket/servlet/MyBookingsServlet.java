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
 * My Bookings page: lists the logged-in user's bookings.
 */
@WebServlet("/my-bookings")
public class MyBookingsServlet extends HttpServlet {

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
        if (session == null || !SessionUtil.isLoggedIn(session)) {
            resp.sendRedirect(req.getContextPath() + "/login?next=/my-bookings");
            return;
        }
        try {
            String username = SessionUtil.getUsername(session);
            List<Booking> bookings = new BookingDao(dataDir()).findByUser(username);

            // Build lookup maps for displaying movie/show details in the table
            java.util.Map<String, com.movieticket.model.Show> showMap = new java.util.LinkedHashMap<>();
            java.util.Map<String, com.movieticket.model.Movie> movieMap = new java.util.LinkedHashMap<>();
            for (Booking b : bookings) {
                com.movieticket.model.Show s = new ShowDao(dataDir()).findById(b.getShowId());
                if (s != null) {
                    showMap.put(b.getShowId(), s);
                    movieMap.put(s.getMovieId(), new MovieDao(dataDir()).findById(s.getMovieId()));
                }
            }

            req.setAttribute("bookings", bookings);
            req.setAttribute("showMap", showMap);
            req.setAttribute("movieMap", movieMap);
            req.setAttribute("username", username);
            req.getRequestDispatcher("/WEB-INF/views/my-bookings.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
