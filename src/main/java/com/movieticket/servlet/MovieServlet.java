package com.movieticket.servlet;

import com.movieticket.dao.MovieDao;
import com.movieticket.model.Movie;
import com.movieticket.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Movie listing page with optional search by title, genre, or language.
 */
@WebServlet("/movies")
public class MovieServlet extends HttpServlet {

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
        if (SessionUtil.isStaff(req.getSession(false))) {
            resp.sendRedirect(req.getContextPath() + "/admin");
            return;
        }
        try {
            String query = req.getParameter("q");
            List<Movie> movies;
            if (query != null && !query.trim().isEmpty()) {
                String q = query.trim().toLowerCase();
                movies = new ArrayList<>();
                for (Movie m : new MovieDao(dataDir()).findAll()) {
                    if (m.getTitle().toLowerCase().contains(q)
                            || m.getGenre().toLowerCase().contains(q)
                            || m.getLanguage().toLowerCase().contains(q)) {
                        movies.add(m);
                    }
                }
            } else {
                movies = new MovieDao(dataDir()).findAll();
            }
            req.setAttribute("movies", movies);
            req.setAttribute("query", query);
            req.getRequestDispatcher("/WEB-INF/views/movies.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
