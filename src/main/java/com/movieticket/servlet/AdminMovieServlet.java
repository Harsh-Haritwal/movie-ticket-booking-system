package com.movieticket.servlet;

import com.movieticket.dao.MovieDao;
import com.movieticket.model.Movie;
import com.movieticket.util.IdGenerator;
import com.movieticket.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Admin movie management: list, add, edit, delete movies.
 */
@WebServlet("/admin/movies")
public class AdminMovieServlet extends HttpServlet {

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
        if (session == null || !SessionUtil.canManageMovies(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Movie management permission required");
            return;
        }
        String action = req.getParameter("action");
        if ("edit".equalsIgnoreCase(action)) {
            String id = req.getParameter("id");
            try {
                Movie m = new MovieDao(dataDir()).findById(id);
                if (m != null) {
                    req.setAttribute("movie", m);
                    req.setAttribute("edit", true);
                }
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
        try {
            req.setAttribute("movies", new MovieDao(dataDir()).findAll());
        } catch (Exception e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/manage-movies.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if (!SessionUtil.canManageMovies(req.getSession(false))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Movie management permission required");
            return;
        }
        try {
            MovieDao dao = new MovieDao(dataDir());
            String id = req.getParameter("id");
            String title = req.getParameter("title");
            String genre = req.getParameter("genre");
            String language = req.getParameter("language");
            String duration = req.getParameter("duration");
            String rating = req.getParameter("rating");
            String poster = req.getParameter("poster");
            String description = req.getParameter("description");

            int durationMinutes;
            double movieRating;
            try {
                durationMinutes = Integer.parseInt(duration);
                movieRating = Double.parseDouble(rating);
            } catch (NumberFormatException e) {
                resp.sendRedirect(req.getContextPath() + "/admin/movies?error=Duration+and+rating+must+be+valid+numbers");
                return;
            }
            if (title == null || title.isBlank() || genre == null || genre.isBlank()
                    || language == null || language.isBlank() || description == null || description.isBlank()
                    || durationMinutes <= 0 || movieRating < 0 || movieRating > 10) {
                resp.sendRedirect(req.getContextPath() + "/admin/movies?error=Complete+all+fields+and+enter+a+rating+from+0+to+10");
                return;
            }

            if (id == null || id.isBlank()) {
                // Add new
                String movieId = IdGenerator.nextMovieId();
                Movie m = new Movie(movieId, title, genre, language,
                        durationMinutes, movieRating, importLocalPoster(poster, movieId), description);
                dao.add(m);
                resp.sendRedirect(req.getContextPath() + "/admin/movies?message=Movie+added+successfully");
            } else {
                // Edit existing
                Movie m = dao.findById(id);
                if (m != null) {
                    m.setTitle(title);
                    m.setGenre(genre);
                    m.setLanguage(language);
                    m.setDuration(durationMinutes);
                    m.setRating(movieRating);
                    m.setPoster(importLocalPoster(poster, id));
                    m.setDescription(description);
                    dao.update(m);
                    resp.sendRedirect(req.getContextPath() + "/admin/movies?message=Movie+updated+successfully");
                } else {
                    resp.sendRedirect(req.getContextPath() + "/admin/movies?error=Movie+not+found");
                }
            }
        } catch (IllegalArgumentException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/movies?error="
                    + java.net.URLEncoder.encode(e.getMessage(), java.nio.charset.StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    /** Copies an administrator-provided local image into the web app's posters folder. */
    private String importLocalPoster(String suppliedPath, String movieId) throws IOException {
        if (suppliedPath == null || suppliedPath.isBlank() || suppliedPath.trim().startsWith("posters/")) {
            return suppliedPath == null ? "" : suppliedPath.trim();
        }
        Path source = Path.of(suppliedPath.trim());
        if (!Files.isRegularFile(source)) {
            throw new IllegalArgumentException("Poster file was not found. Paste a valid local image path.");
        }
        String filename = source.getFileName().toString();
        int dot = filename.lastIndexOf('.');
        String extension = dot < 0 ? "" : filename.substring(dot).toLowerCase();
        if (!extension.matches("\\.(jpg|jpeg|png|gif|webp)")) {
            throw new IllegalArgumentException("Poster must be a JPG, PNG, GIF, or WEBP image.");
        }
        Path posterFolder = Path.of(getServletContext().getRealPath("/posters"));
        Files.createDirectories(posterFolder);
        Files.copy(source, posterFolder.resolve(movieId + extension), StandardCopyOption.REPLACE_EXISTING);
        return "posters/" + movieId + extension;
    }
}
