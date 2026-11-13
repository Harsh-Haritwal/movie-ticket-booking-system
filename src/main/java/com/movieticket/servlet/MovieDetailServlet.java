package com.movieticket.servlet;

import com.movieticket.dao.MovieDao;
import com.movieticket.dao.ShowDao;
import com.movieticket.model.Movie;
import com.movieticket.model.Show;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Movie details page showing poster, description, and available shows.
 */
@WebServlet("/movie")
public class MovieDetailServlet extends HttpServlet {

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
            String id = req.getParameter("id");
            MovieDao movieDao = new MovieDao(dataDir());
            Movie movie = movieDao.findById(id);
            if (movie == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Movie not found");
                return;
            }
            List<Show> shows = new ShowDao(dataDir()).findByMovie(id);
            req.setAttribute("movie", movie);
            req.setAttribute("shows", shows);
            req.getRequestDispatcher("/WEB-INF/views/movie-detail.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
