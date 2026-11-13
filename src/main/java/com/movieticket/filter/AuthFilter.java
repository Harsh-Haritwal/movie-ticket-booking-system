package com.movieticket.filter;

import com.movieticket.util.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Controls access to user and admin pages based on session role.
 * - /admin/* requires admin role
 * - other protected paths require any logged-in user
 */
@WebFilter({
    "/movie", "/seats", "/book", "/cancel", "/my-bookings", "/admin/*"
})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        String path = req.getServletPath();

        boolean isAdminPath = path.startsWith("/admin");

        if (session == null || !SessionUtil.isLoggedIn(session)) {
            resp.sendRedirect(req.getContextPath() + "/login?next=" + path);
            return;
        }

        if (isAdminPath && !SessionUtil.isStaff(session)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return;
        }

        // Administrators use the management workflow only; they cannot make customer bookings.
        if (!isAdminPath && !"/profile".equals(path) && SessionUtil.isStaff(session)) {
            resp.sendRedirect(req.getContextPath() + "/admin");
            return;
        }

        chain.doFilter(request, response);
    }
}
