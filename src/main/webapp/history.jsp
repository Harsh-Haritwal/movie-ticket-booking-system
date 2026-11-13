<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>My Bookings</title>
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <%@ include file="nav.jsp" %>
                <div class="wrap">
                    <c:choose>
                        <c:when test="${not signedIn}">
                            <header class="page-header">
                                <h1>My bookings</h1>
                                <p>You are not signed in, so there are no bookings to show.</p>
                            </header>
                            <div class="signin-box">
                                <p>Sign in to view and manage your bookings.</p>
                                <a class="btn" href="${pageContext.request.contextPath}/login?next=/history">Sign in</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <header class="page-header">
                                <h1>My bookings</h1>
                                <p>Signed in as <strong>${username}</strong> · ${bookings.size()} booking(s)</p>
                            </header>
                            <c:choose>
                                <c:when test="${empty bookings}">
                                    <p class="err">No bookings yet — <a href="${pageContext.request.contextPath}/movies">browse movies</a> to book your first ticket.</p>
                                </c:when>
                                <c:otherwise>
                                    <table class="history-table">
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
                                                    <td>${mv.title}</td>
                                                    <td>${sh.date} · ${sh.time}</td>
                                                    <td>${sh.screen}</td>
                                                    <td>${b.seatList}</td>
                                                    <td>Rs.${b.amountLabel}</td>
                                                    <td><span class="badge status-${b.status}">${b.status}</span></td>
                                                    <td class="text-right">
                                                        <c:if test="${b.status == 'CONFIRMED'}">
                                                            <form method="post" action="${pageContext.request.contextPath}/cancel" style="display:inline" onsubmit="return confirm('Cancel booking ${b.id}? Seats will be released.')">
                                                                <input type="hidden" name="bookingId" value="${b.id}" />
                                                                <button class="btn ghost" type="submit">Cancel</button></form>
                                                        </c:if>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>
                </div>
        </body>

        </html>