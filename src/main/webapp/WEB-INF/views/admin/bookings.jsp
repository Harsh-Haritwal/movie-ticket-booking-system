<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Booked Tickets</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links"><a href="${pageContext.request.contextPath}/admin/bookings" class="active">Booked Tickets</a>
                    <c:if test="${sessionScope.role == 'admin'}"><a href="${pageContext.request.contextPath}/admin">Admin Dashboard</a><a href="${pageContext.request.contextPath}/admin/staff">Staff Roles</a></c:if><span class="account-menu"><span class="user-chip">${sessionScope.user}</span>
                    <span class="account-dropdown"><a href="${pageContext.request.contextPath}/profile">Account Settings</a><a href="${pageContext.request.contextPath}/logout">Logout</a></span>
                    </span>
                </nav>
            </header>
            <main class="wrap">
                <header class="page-header">
                    <h1>All Booked Tickets</h1>
                    <p>Read-only booking and cancellation history.</p>
                </header>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Booking</th>
                            <th>User</th>
                            <th>Show</th>
                            <th>Seats</th>
                            <th>Amount</th>
                            <th>Status</th>
                            <th>Booked At</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="b" items="${bookings}">
                            <tr>
                                <td><span class="show-id">#${b.id}</span></td>
                                <td>${b.username}</td>
                                <td>${b.showId}</td>
                                <td>${b.seatList}</td>
                                <td>Rs.${b.amountLabel}</td>
                                <td><span class="badge status-${b.statusCss}">${b.status}</span></td>
                                <td>${b.bookingTime}</td>
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