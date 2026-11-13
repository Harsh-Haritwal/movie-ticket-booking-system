package com.movieticket.servlet;

import com.movieticket.dao.UserDao;
import com.movieticket.model.User;
import com.movieticket.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/** Lets a signed-in account update its contact email and password. */
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    private File dataDir() {
        String real = getServletContext().getRealPath("/WEB-INF/data");
        return real != null ? new File(real) : new File(getServletContext().getRealPath("/"), "WEB-INF/data");
    }

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = new UserDao(dataDir()).findByUsername(SessionUtil.getUsername(req.getSession(false)));
            req.setAttribute("account", user);
            req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
        } catch (Exception e) { throw new ServletException(e); }
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            UserDao dao = new UserDao(dataDir());
            User user = dao.findByUsername(SessionUtil.getUsername(req.getSession(false)));
            String email = req.getParameter("email");
            String current = req.getParameter("currentPassword");
            String next = req.getParameter("newPassword");
            String confirm = req.getParameter("confirmPassword");
            if (user == null || email == null || email.isBlank()) {
                resp.sendRedirect(req.getContextPath() + "/profile?error=Email+is+required"); return;
            }
            if (next != null && !next.isBlank()) {
                if (!user.getPassword().equals(current) || !next.equals(confirm)) {
                    resp.sendRedirect(req.getContextPath() + "/profile?error=Password+details+do+not+match"); return;
                }
                user.setPassword(next);
            }
            user.setEmail(email.trim());
            dao.update(user);
            resp.sendRedirect(req.getContextPath() + "/profile?message=Account+details+updated");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
