<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Showtimes — ${movie.title}</title>
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <%@ include file="nav.jsp" %>
                <div class="wrap">
                    <header class="page-header">
                        <div>
                            <h1>${movie.title}</h1>
                            <p class="dim">${movie.genre} · ${movie.language} · ${movie.durationMins} min · ${movie.rating} / 10</p>
                        </div>
                    </header>
                    <c:if test="${empty shows}">
                        <p class="err">No shows found for this movie.</p>
                    </c:if>
                    <table class="shows-table">
                        <thead>
                            <tr>
                                <th>Show</th>
                                <th>Screen</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Price</th>
                                <th>Seats available</th>
                                <th class="text-right">Book</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="s" items="${shows}">
                                <tr>
                                    <td><span class="show-id">${s.id}</span></td>
                                    <td>${s.screen}</td>
                                    <td>${s.date}</td>
                                    <td>${s.time}</td>
                                    <td><strong>Rs.${s.priceLabel}</strong></td>
                                    <td><span class="seats-left">${s.availableCount} / ${s.totalSeats}</span></td>
                                    <td class="text-right"><a class="btn" href="${pageContext.request.contextPath}/seats?showId=${s.id}">Select seats</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
        </body>

        </html>