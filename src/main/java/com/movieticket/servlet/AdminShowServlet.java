package com.movieticket.servlet;

import com.movieticket.dao.ShowDao;
import com.movieticket.dao.MovieDao;
import com.movieticket.dao.BookingDao;
import com.movieticket.model.Movie;
import com.movieticket.model.Show;
import com.movieticket.util.IdGenerator;
import com.movieticket.util.SessionUtil;
import com.movieticket.util.SeatLayoutUtil;
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
 * Admin show management: list and add showtimes.
 */
@WebServlet("/admin/shows")
public class AdminShowServlet extends HttpServlet {

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
        if (session == null || !SessionUtil.canManageShows(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Showtime management permission required");
            return;
        }
        try {
            req.setAttribute("shows", new ShowDao(dataDir()).findAll());
            req.setAttribute("movies", new MovieDao(dataDir()).findAll());
            if ("edit".equalsIgnoreCase(req.getParameter("action"))) {
                Show show = new ShowDao(dataDir()).findById(req.getParameter("id"));
                if (show != null) {
                    req.setAttribute("show", show);
                    req.setAttribute("edit", true);
                }
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/manage-shows.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !SessionUtil.canManageShows(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return;
        }
        try {
            ShowDao dao = new ShowDao(dataDir());
            MovieDao movieDao = new MovieDao(dataDir());
            String action = req.getParameter("action");
            String id = req.getParameter("id");

            if ("delete".equalsIgnoreCase(action)) {
                if (id == null || dao.findById(id) == null) {
                    resp.sendRedirect(req.getContextPath() + "/admin/shows?error=Show+not+found");
                    return;
                }
                if (new BookingDao(dataDir()).hasBookingForShow(id)) {
                    resp.sendRedirect(req.getContextPath() + "/admin/shows?error=Cannot+delete+a+show+with+booking+history");
                    return;
                }
                dao.delete(id);
                resp.sendRedirect(req.getContextPath() + "/admin/shows?message=Showtime+deleted");
                return;
            }

            String movieId = req.getParameter("movieId");
            String theatre = req.getParameter("theatre");
            String screen = req.getParameter("screen");
            String date = req.getParameter("date");
            String time = req.getParameter("time");
            String price = req.getParameter("price");
            String totalSeats = req.getParameter("totalSeats");
            String seatLayout = req.getParameter("seatLayout");
            String rowPrices = req.getParameter("rowPrices");

            Movie movie = movieDao.findById(movieId);
            if (movie == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid movie");
                return;
            }

            int seatCount;
            double ticketPrice;
            try {
                if (seatLayout == null || seatLayout.isBlank()) {
                    seatCount = Integer.parseInt(totalSeats);
                    seatLayout = SeatLayoutUtil.defaultDeclaration(seatCount);
                } else {
                    seatCount = SeatLayoutUtil.seatsFor(seatLayout, 0).size();
                }
                java.util.Map<String, Double> priceMap = SeatLayoutUtil.pricesFor(rowPrices,
                        SeatLayoutUtil.rowsFor(seatLayout, seatCount), 0);
                ticketPrice = priceMap.values().stream().mapToDouble(Double::doubleValue).min().orElse(0);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/admin/shows?error=Price+and+seat+capacity+must+be+valid+numbers");
                return;
            } catch (IllegalArgumentException e) {
                resp.sendRedirect(req.getContextPath() + "/admin/shows?error="
                        + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
                return;
            }
            if (theatre == null || theatre.isBlank() || screen == null || screen.isBlank()
                    || date == null || date.isBlank() || time == null || time.isBlank()
                    || seatCount <= 0 || ticketPrice <= 0) {
                resp.sendRedirect(req.getContextPath() + "/admin/shows?error=Complete+all+fields+with+positive+price+and+seat+capacity");
                return;
            }

            Show existing = id == null || id.isBlank() ? null : dao.findById(id);
            if (existing != null && seatCount < existing.getBookedSeats().size()) {
                resp.sendRedirect(req.getContextPath() + "/admin/shows?action=edit&id=" + id
                        + "&error=Seat+capacity+cannot+be+less+than+already+booked+seats");
                return;
            }

            Show show = new Show(id == null || id.isBlank() ? IdGenerator.nextShowId() : id,
                    movieId, theatre, screen, date, time,
                    ticketPrice, seatCount,
                    existing == null ? new java.util.ArrayList<>() : existing.getBookedSeats());
            show.setSeatLayout(seatLayout.trim());
            show.setRowPrices(rowPrices.trim());
            if (existing == null) {
                dao.add(show);
            } else {
                dao.update(show);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/shows");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
