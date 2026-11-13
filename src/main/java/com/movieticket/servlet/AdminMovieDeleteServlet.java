package com.movieticket.servlet;

import com.movieticket.dao.MovieDao;
import com.movieticket.dao.ShowDao;
import com.movieticket.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * Deletes a movie (admin only).
 */
@WebServlet("/admin/movies/delete")
public class AdminMovieDeleteServlet extends HttpServlet {

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
        HttpSession session = req.getSession(false);
        if (session == null || !SessionUtil.canManageMovies(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Movie management permission required");
            return;
        }
        try {
            String id = req.getParameter("id");
            if (id != null && !id.isBlank()) {
                if (!new ShowDao(dataDir()).findByMovie(id).isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/admin/movies?error=Delete+the+movie%27s+showtimes+before+deleting+the+movie");
                    return;
                }
                new MovieDao(dataDir()).delete(id);
            }
            resp.sendRedirect(req.getContextPath() + "/admin/movies");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
