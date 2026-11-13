<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Booking confirmed</title>
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <%@ include file="nav.jsp" %>
                <div class="wrap">
                    <section class="confirm-hero">
                        <h1>Booking confirmed</h1>
                        <p>Your tickets are reserved. Please show this confirmation at the cinema entry.</p>
                    </section>
                    <div class="ticket">
                        <div class="ticket-head">
                            <span class="ticket-id">#${booking.id}</span>
                            <span class="badge status-${booking.status}">${booking.status}</span>
                        </div>
                        <div class="ticket-body">
                            <p><strong>Guest:</strong> ${booking.username}</p>
                            <p><strong>Show:</strong> ${booking.showId} — ${show.date} · ${show.time}</p>
                            <p><strong>Screen:</strong> ${show.screen}</p>
                            <p><strong>Seats:</strong> <span class="seat-list"><c:forEach var="s" items="${booking.seats}">${s}<c:if test="${not forEach.last}">, </c:if></c:forEach></span></p>
                            <p><strong>Total paid:</strong> Rs.${booking.amountLabel}</p>
                            <p class="dim">Booked on ${booking.bookingTime}</p>
                        </div>
                        <div class="ticket-foot">
                            <a class="btn" href="${pageContext.request.contextPath}/history">View my bookings</a>
                            <a class="btn ghost" href="${pageContext.request.contextPath}/movies">Book more</a>
                        </div>
                    </div>
                </div>
        </body>

        </html>