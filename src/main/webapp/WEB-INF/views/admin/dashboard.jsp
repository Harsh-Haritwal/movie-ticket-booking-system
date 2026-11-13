<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Admin Dashboard — Movie Ticket Booking</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links">
                    <a href="${pageContext.request.contextPath}/admin" class="active">Admin Dashboard</a>
                    <c:if test="${sessionScope.role == 'admin'}"><a href="${pageContext.request.contextPath}/admin/staff">Staff Roles</a></c:if>
                    <a href="${pageContext.request.contextPath}/profile">Account Settings</a>
                    <span class="account-menu"><span class="user-chip">${sessionScope.user}</span><span class="account-dropdown"><a href="${pageContext.request.contextPath}/profile">Account Settings</a><a href="${pageContext.request.contextPath}/logout">Logout</a></span></span>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </nav>
            </header>

            <main class="wrap">
                <header class="page-header">
                    <h1>Admin Dashboard</h1>
                    <p>Manage movies, showtimes, and view bookings.</p>
                </header>

                <div class="admin-stats">
                    <div class="stat-card">
                        <strong>${movieCount}</strong><span class="muted">Movies</span>
                    </div>
                    <div class="stat-card">
                        <strong>${showCount}</strong><span class="muted">Showtimes</span>
                    </div>
                    <div class="stat-card">
                        <strong>${bookingCount}</strong><span class="muted">Total Bookings</span>
                    </div>
                </div>

                <section class="admin-section">
                    <h2>Recent Bookings</h2>
                    <c:choose>
                        <c:when test="${empty recentBookings}">
                            <p class="muted">No bookings yet.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Booking</th>
                                        <th>User</th>
                                        <th>Show</th>
                                        <th>Seats</th>
                                        <th>Amount</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="b" items="${recentBookings}">
                                        <tr>
                                            <td><span class="show-id">#${b.id}</span></td>
                                            <td>${b.username}</td>
                                            <td>${b.showId}</td>
                                            <td>${b.seats}</td>
                                            <td>Rs.${b.totalAmount}</td>
                                            <td><span class="badge status-${b.status.toLowerCase()}">${b.status}</span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </section>

                <div class="admin-actions">
                    <c:if test="${sessionScope.role == 'admin' or sessionScope.role == 'movie_manager'}"><a class="btn primary" href="${pageContext.request.contextPath}/admin/movies">Manage Movies</a></c:if>
                    <c:if test="${sessionScope.role == 'admin' or sessionScope.role == 'show_manager'}"><a class="btn primary" href="${pageContext.request.contextPath}/admin/shows">Manage Showtimes</a></c:if>
                    <c:if test="${sessionScope.role == 'admin'}"><a class="btn outline" href="${pageContext.request.contextPath}/admin/bookings">View All Bookings</a></c:if>
                </div>
            </main>

            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.</p>
            </footer>
        </body>

        </html>