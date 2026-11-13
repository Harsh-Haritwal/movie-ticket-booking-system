package com.movieticket.servlet;

import com.movieticket.dao.ShowDao;
import com.movieticket.model.Show;
import com.movieticket.util.SeatLayoutUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Seat selection page for a specific show.
 */
@WebServlet("/seats")
public class SeatServlet extends HttpServlet {

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
        try {
            String id = req.getParameter("showId");
            Show show = new ShowDao(dataDir()).findById(id);
            if (show == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Show not found");
                return;
            }
            req.setAttribute("show", show);
            req.setAttribute("allSeats", SeatLayoutUtil.seatsFor(show.getSeatLayout(), show.getTotalSeats()));
            req.setAttribute("seatRows", SeatLayoutUtil.rowsFor(show.getSeatLayout(), show.getTotalSeats()));
            req.setAttribute("rowPrices", SeatLayoutUtil.pricesFor(show.getRowPrices(),
                    SeatLayoutUtil.rowsFor(show.getSeatLayout(), show.getTotalSeats()), show.getPrice()));
            req.getRequestDispatcher("/WEB-INF/views/seat-select.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

}
