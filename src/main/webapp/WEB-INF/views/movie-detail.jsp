<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>${movie.title} — Movie Ticket Booking</title>
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
                    <c:when test="${not empty movie}">
                        <section class="movie-detail">
                            <div class="detail-poster">
                                <img src="${pageContext.request.contextPath}/${movie.poster}" alt="${movie.title}" onerror="this.style.display='none';this.nextElementSibling.style.display='flex'" />
                                <div class="poster-fallback" style="${empty movie.poster ? 'display:flex' : 'display:none'}">
                                    <span class="poster-icon">🎬</span>
                                </div>
                            </div>
                            <div class="detail-info">
                                <h1>${movie.title}</h1>
                                <div class="detail-meta">
                                    <span><strong>Genre:</strong> ${movie.genre}</span>
                                    <span><strong>Language:</strong> ${movie.language}</span>
                                    <span><strong>Duration:</strong> ${movie.duration} min</span>
                                    <span><strong>Rating:</strong> ${movie.rating} / 10</span>
                                </div>
                                <p class="detail-synopsis">${movie.description}</p>
                                <c:choose>
                                    <c:when test="${not empty shows and not empty shows}">
                                        <h3>Available Shows</h3>
                                        <div class="show-list">
                                            <c:forEach var="s" items="${shows}">
                                                <a class="show-card" href="${pageContext.request.contextPath}/seats?showId=${s.id}">
                                                    <span class="show-theatre">${s.theatre}</span>
                                                    <span class="show-screen">${s.screen}</span>
                                                    <span class="show-date">${s.date}</span>
                                                    <span class="show-time">${s.time}</span>
                                                    <span class="show-price">Rs.${s.price}</span>
                                                    <span class="show-seats">${s.availableSeats}/${s.totalSeats} seats left</span>
                                                </a>
                                            </c:forEach>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="muted">No shows scheduled for this movie.</p>
                                    </c:otherwise>
                                </c:choose>
                                <a class="btn outline" href="${pageContext.request.contextPath}/movies">Back to Movies</a>
                            </div>
                        </section>
                    </c:when>
                    <c:otherwise>
                        <div class="alert error">Movie not found.</div>
                        <a class="btn primary" href="${pageContext.request.contextPath}/movies">Back to Movies</a>
                    </c:otherwise>
                </c:choose>
            </main>

            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.</p>
            </footer>
        </body>

        </html>