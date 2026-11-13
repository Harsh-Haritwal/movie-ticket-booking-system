<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Booking Confirmed — Movie Ticket Booking</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Home</a>
                    <a href="${pageContext.request.contextPath}/movies">Movies</a>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <a href="${pageContext.request.contextPath}/my-bookings">My Bookings</a>
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
                <section class="confirm-hero">
                    <h1>Booking Confirmed</h1>
                    <p>Your tickets are reserved. Please show this confirmation at the cinema entry.</p>
                </section>

                <div class="ticket">
                    <div class="ticket-head">
                        <span class="ticket-id">#${booking.id}</span>
                        <span class="badge status-confirmed">${booking.status}</span>
                    </div>
                    <div class="ticket-body">
                        <p><strong>Guest:</strong> ${booking.username}</p>
                        <p><strong>Show:</strong> ${booking.showId}</p>
                        <p><strong>Date &amp; Time:</strong> ${show.date} · ${show.time}</p>
                        <p><strong>Screen:</strong> ${show.screen}</p>
                        <p><strong>Seats:</strong> <span class="seat-list">
                <c:forEach var="s" items="${booking.seats}" varStatus="seatStatus">${s}<c:if test="${not seatStatus.last}">, </c:if></c:forEach>
            </span></p>
                        <p><strong>Total Paid:</strong> Rs.${booking.amountLabel}</p>
                        <p class="muted small">Booked on ${booking.bookingTime}</p>
                    </div>
                    <div class="ticket-foot">
                        <a class="btn primary" href="${pageContext.request.contextPath}/my-bookings">View My Bookings</a>
                        <a class="btn outline" href="${pageContext.request.contextPath}/movies">Book More</a>
                    </div>
                </div>
            </main>

            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System. p>
            </footer>
        </body>

        </html>