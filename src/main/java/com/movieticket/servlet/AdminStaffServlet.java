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
import java.util.Set;

/** Head-admin assignment of limited staff roles. */
@WebServlet("/admin/staff")
public class AdminStaffServlet extends HttpServlet {
    private static final Set<String> ROLES = Set.of("user", "movie_manager", "show_manager", "booking_viewer");
    private File dataDir() { return new File(getServletContext().getRealPath("/WEB-INF/data")); }
    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isAdmin(req.getSession(false))) { resp.sendError(403, "Head administrator access required"); return; }
        try { req.setAttribute("users", new UserDao(dataDir()).findAll()); req.getRequestDispatcher("/WEB-INF/views/admin/manage-staff.jsp").forward(req, resp); }
        catch (Exception e) { throw new ServletException(e); }
    }
    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isAdmin(req.getSession(false))) { resp.sendError(403, "Head administrator access required"); return; }
        try {
            UserDao dao = new UserDao(dataDir()); User user = dao.findByUsername(req.getParameter("username")); String role = req.getParameter("role");
            if (user == null || !ROLES.contains(role)) { resp.sendRedirect(req.getContextPath()+"/admin/staff?error=Invalid+staff+assignment"); return; }
            user.setRole(role); dao.update(user); resp.sendRedirect(req.getContextPath()+"/admin/staff?message=Role+updated");
        } catch (Exception e) { throw new ServletException(e); }
    }
}
