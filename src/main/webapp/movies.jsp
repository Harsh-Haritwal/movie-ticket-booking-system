<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Now Showing — Movie Ticket Booking</title>
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <%@ include file="nav.jsp" %>
                <div class="wrap">
                    <header class="page-header">
                        <h1>Now showing</h1>
                        <p>${movies.size()} film(s) currently playing · choose one to see showtimes</p>
                    </header>
                    <div class="grid movie-grid">
                        <c:forEach var="m" items="${movies}">
                            <div class="card movie-card">
                                <div class="poster p-${m.id}"></div>
                                <div class="body">
                                    <h3>${m.title}</h3>
                                    <div class="meta"><span class="rating">${m.rating}</span> <span class="sep">/ 10</span> <span class="sep">${m.durationMins} min</span> <span class="sep">${m.language}</span></div>
                                    <div class="badges"><span class="badge">${m.genre}</span></div>
                                    <p class="synopsis">${m.description}</p>
                                    <a class="btn" href="${pageContext.request.contextPath}/shows?movieId=${m.id}">View showtimes</a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
        </body>

        </html>