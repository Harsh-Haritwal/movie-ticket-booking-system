package com.movieticket.servlet;

import com.movieticket.dao.MovieDao;
import com.movieticket.dao.ShowDao;
import com.movieticket.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Home page: shows featured movies and recent shows.
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {

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
            File dir = dataDir();
            req.setAttribute("movieCount", new MovieDao(dir).findAll().size());
            req.setAttribute("showCount", new ShowDao(dir).findAll().size());
            req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
