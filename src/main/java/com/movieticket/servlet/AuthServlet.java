package com.movieticket.servlet;

import com.movieticket.dao.UserDao;
import com.movieticket.model.User;
import com.movieticket.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

/**
 * Login page (GET) and login processing (POST).
 */
public class AuthServlet extends HttpServlet {

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
        if (SessionUtil.isLoggedIn(session)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String next = req.getParameter("next");

            UserDao dao = new UserDao(dataDir());
            User user = dao.findByUsername(username);

            if (user != null && user.getPassword().equals(password)) {
                HttpSession session = req.getSession(true);
                SessionUtil.login(session, user.getUsername(), user.getRole());
                String target = (next != null && !next.isEmpty()) ? next : "/";
                resp.sendRedirect(req.getContextPath() + target);
            } else {
                req.setAttribute("error", "Invalid username or password.");
                req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
