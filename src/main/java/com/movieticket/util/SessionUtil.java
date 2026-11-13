package com.movieticket.util;

import jakarta.servlet.http.HttpSession;

/**
 * Helper methods for managing the logged-in user in the HTTP session.
 */
public final class SessionUtil {

    public static final String SESSION_USER = "user";

    private SessionUtil() {
    }

    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute(SESSION_USER) != null;
    }

    public static String getUsername(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object attr = session.getAttribute(SESSION_USER);
        return attr == null ? null : String.valueOf(attr);
    }

    public static String getRole(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object attr = session.getAttribute("role");
        return attr == null ? null : String.valueOf(attr);
    }

    public static void login(HttpSession session, String username, String role) {
        session.setAttribute(SESSION_USER, username);
        session.setAttribute("role", role);
    }

    public static void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }

    public static boolean isAdmin(HttpSession session) {
        return "admin".equalsIgnoreCase(getRole(session));
    }

    public static boolean isStaff(HttpSession session) {
        return isStaffRole(getRole(session));
    }

    public static boolean isStaffRole(String role) {
        return "admin".equalsIgnoreCase(role) || "movie_manager".equalsIgnoreCase(role)
                || "show_manager".equalsIgnoreCase(role) || "booking_viewer".equalsIgnoreCase(role);
    }

    public static boolean canManageMovies(HttpSession session) {
        String role = getRole(session);
        return "admin".equalsIgnoreCase(role) || "movie_manager".equalsIgnoreCase(role);
    }

    public static boolean canManageShows(HttpSession session) {
        String role = getRole(session);
        return "admin".equalsIgnoreCase(role) || "show_manager".equalsIgnoreCase(role);
    }
}
