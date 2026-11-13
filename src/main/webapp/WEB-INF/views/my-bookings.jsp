<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>My Bookings — Movie Ticket Booking</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Home</a>
                    <a href="${pageContext.request.contextPath}/movies">Movies</a>
                    <a href="${pageContext.request.contextPath}/my-bookings" class="active">My Bookings</a>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <c:if test="${sessionScope.role == 'admin'}"><a href="${pageContext.request.contextPath}/admin">Admin Panel</a></c:if>
                            <span class="account-menu"><span class="user-chip">${sessionScope.user}</span><span class="account-dropdown"><a href="${pageContext.request.contextPath}/profile">Account Settings</a><a href="${pageContext.request.contextPath}/logout">Logout</a></span></span>
                            <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        </c:when>
                        <c:otherwise>
                            <span class="login-menu"><span class="login-trigger">Login</span><span class="login-dropdown"><a href="${pageContext.request.contextPath}/login?mode=user">User Login</a><a href="${pageContext.request.contextPath}/login?mode=admin">Admin Login</a></span></span>
                        </c:otherwise>
                    </c:choose>
                </nav>
            </header>

            <main class="wrap">
                <c:choose>
                    <c:when test="${empty bookings}">
                        <header class="page-header">
                            <h1>My Bookings</h1>
                            <p>No bookings yet.</p>
                        </header>
                        <div class="alert muted">You have not booked any tickets yet.
                            <a href="${pageContext.request.contextPath}/movies">Browse movies</a> to book your first ticket.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <header class="page-header">
                            <h1>My Bookings</h1>
                            <p>Signed in as <strong>${username}</strong> · ${bookings.size()} booking(s)</p>
                        </header>
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Booking</th>
                                    <th>Movie</th>
                                    <th>Showtime</th>
                                    <th>Screen</th>
                                    <th>Seats</th>
                                    <th>Amount</th>
                                    <th>Status</th>
                                    <th class="text-right">Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="b" items="${bookings}">
                                    <c:set var="sh" value="${showMap[b.showId]}" />
                                    <c:set var="mv" value="${movieMap[sh.movieId]}" />
                                    <tr>
                                        <td><span class="show-id">#${b.id}</span></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty mv}">${mv.title}</c:when>
                                                <c:otherwise>Removed movie</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty sh}">${sh.date} · ${sh.time}</c:when>
                                                <c:otherwise>Show unavailable</c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>${not empty sh ? sh.screen : '-'}</td>
                                        <td>${b.seatList}</td>
                                        <td>Rs.${b.amountLabel}</td>
                                        <td><span class="badge status-${b.statusCss}">${b.status}</span></td>
                                        <td class="text-right">
                                            <c:if test="${b.status == 'CONFIRMED'}">
                                                <form method="post" action="${pageContext.request.contextPath}/cancel" onsubmit="return confirm('Cancel this booking? Seats will be released.')">
                                                    <input type="hidden" name="bookingId" value="${b.id}" />
                                                    <button class="btn outline small" type="submit">Cancel</button>
                                                </form>
                                            </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </main>

            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.</p>
            </footer>
        </body>

        </html>