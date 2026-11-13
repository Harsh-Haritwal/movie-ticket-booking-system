package com.movieticket.servlet;

import com.movieticket.dao.UserDao;
import com.movieticket.model.User;
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

/**
 * Registration page (GET) and registration processing (POST).
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String confirm = req.getParameter("confirmPassword");
            String email = req.getParameter("email");

            if (username == null || username.trim().isEmpty()
                    || password == null || password.isEmpty()
                    || email == null || email.trim().isEmpty()) {
                req.setAttribute("error", "All fields are required.");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }

            if (!password.equals(confirm)) {
                req.setAttribute("error", "Passwords do not match.");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }

            UserDao dao = new UserDao(dataDir());
            if (dao.usernameExists(username)) {
                req.setAttribute("error", "Username already exists.");
                req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
                return;
            }

            User user = new User(IdGenerator.nextUserId(), username.trim(), password,
                    email.trim(), "user");
            dao.add(user);

            req.setAttribute("message", "Registration successful. Please log in.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
