<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Staff Roles</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links"><a href="${pageContext.request.contextPath}/admin">Admin Dashboard</a><a href="${pageContext.request.contextPath}/admin/staff" class="active">Staff Roles</a><a href="${pageContext.request.contextPath}/profile">Account Settings</a><a href="${pageContext.request.contextPath}/logout">Logout</a></nav>
            </header>
            <main class="wrap">
                <header class="page-header">
                    <h1>Staff Role Assignment</h1>
                    <p>Only the head administrator can assign restricted back-office roles.</p>
                </header>
                <c:if test="${not empty param.message}">
                    <div class="alert success">${param.message}</div>
                </c:if>
                <c:if test="${not empty param.error}">
                    <div class="alert error">${param.error}</div>
                </c:if>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>User</th>
                            <th>Email</th>
                            <th>Assigned Role</th>
                            <th class="text-right">Save</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td>${u.username}</td>
                                <td>${u.email}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.role == 'admin'}">Head Administrator</c:when>
                                        <c:otherwise>
                                            <form method="post" action="${pageContext.request.contextPath}/admin/staff"><input type="hidden" name="username" value="${u.username}" /><select name="role"><option value="user" ${u.role == 'user' ? 'selected' : ''}>Normal User</option><option value="movie_manager" ${u.role == 'movie_manager' ? 'selected' : ''}>Movie Manager</option><option value="show_manager" ${u.role == 'show_manager' ? 'selected' : ''}>Showtime Manager</option><option value="booking_viewer" ${u.role == 'booking_viewer' ? 'selected' : ''}>Booking Viewer</option></select></td>
                                <td class="text-right"><button class="btn primary small">Assign</button></form>
                                    </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </main>
            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.</p>
            </footer>
        </body>

        </html>