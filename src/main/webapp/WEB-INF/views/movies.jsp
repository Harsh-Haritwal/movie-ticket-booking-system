<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Movies — Movie Ticket Booking</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Home</a>
                    <a href="${pageContext.request.contextPath}/movies" class="active">Movies</a>
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
                <header class="page-header">
                    <h1>Now Showing</h1>
                    <p>${movies.size()} film(s) currently playing</p>
                    <form class="search-form" action="${pageContext.request.contextPath}/movies" method="get">
                        <input type="text" name="q" value="${query}" placeholder="Search by title, genre, or language" />
                        <button class="btn primary" type="submit">Search</button>
                    </form>
                </header>

                <c:choose>
                    <c:when test="${empty movies}">
                        <p class="muted center">No movies found matching your search.</p>
                    </c:when>
                    <c:otherwise>
                        <div class="movie-grid">
                            <c:forEach var="m" items="${movies}">
                                <div class="movie-card">
                                    <div class="poster">
                                        <img src="${pageContext.request.contextPath}/${m.poster}" alt="${m.title}" onerror="this.style.display='none';this.nextElementSibling.style.display='flex'" />
                                        <div class="poster-fallback" style="${empty m.poster ? 'display:flex' : 'display:none'}">
                                            <span class="poster-icon">🎬</span>
                                        </div>
                                    </div>
                                    <div class="body">
                                        <h3>${m.title}</h3>
                                        <div class="meta">
                                            <span>${m.genre}</span>
                                            <span class="dot">·</span>
                                            <span>${m.language}</span>
                                            <span class="dot">·</span>
                                            <span>${m.duration} min</span>
                                            <span class="dot">·</span>
                                            <span class="rating">${m.rating} / 10</span>
                                        </div>
                                        <p class="synopsis">${m.description}</p>
                                        <a class="btn primary" href="${pageContext.request.contextPath}/movie?id=${m.id}">View Showtimes</a>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </main>

            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.</p>
            </footer>
        </body>

        </html>