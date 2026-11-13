<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Account Settings</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links">
                    <c:choose>
                        <c:when test="${sessionScope.role == 'admin'}"><a href="${pageContext.request.contextPath}/admin">Admin Dashboard</a></c:when>
                        <c:otherwise><a href="${pageContext.request.contextPath}/">Home</a><a href="${pageContext.request.contextPath}/my-bookings">My Bookings</a></c:otherwise>
                    </c:choose><a href="${pageContext.request.contextPath}/logout">Logout</a></nav>
            </header>
            <main class="wrap">
                <div class="auth-card profile-card">
                    <h1>Account Settings</h1>
                    <c:if test="${not empty param.error}">
                        <div class="alert error">${param.error}</div>
                    </c:if>
                    <c:if test="${not empty param.message}">
                        <div class="alert success">${param.message}</div>
                    </c:if>
                    <form method="post" action="${pageContext.request.contextPath}/profile"><label>Username</label><input value="${account.username}" disabled/><label>Contact Email</label><input type="email" name="email" value="${account.email}" required/>
                        <hr/>
                        <p class="muted small">Leave the password fields empty to keep your current password.</p><label>Current Password</label><input type="password" name="currentPassword" autocomplete="current-password" /><label>New Password</label><input type="password"
                            name="newPassword" autocomplete="new-password" /><label>Confirm New Password</label><input type="password" name="confirmPassword" autocomplete="new-password" /><button class="btn primary" type="submit">Save Changes</button></form>
                </div>
            </main>
            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.</p>
            </footer>
        </body>

        </html>