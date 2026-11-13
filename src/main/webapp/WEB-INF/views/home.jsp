<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Movie Ticket Booking — Home</title>
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
                            <a href="${pageContext.request.contextPath}/profile">Account Settings</a>
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
                <section class="hero">
                    <div class="hero-text">
                        <span class="kicker">Now Showing</span>
                        <h1>Book your seat in seconds</h1>
                        <p>Choose a movie, pick your showtime, select your seats, and get an instant confirmation.</p>
                        <div class="hero-actions">
                            <a class="btn primary" href="${pageContext.request.contextPath}/movies">Browse Movies</a>
                            <c:choose>
                                <c:when test="${not empty sessionScope.user}">
                                    <a class="btn outline" href="${pageContext.request.contextPath}/my-bookings">My Bookings</a>
                                </c:when>
                                <c:otherwise>
                                    <a class="btn outline" href="${pageContext.request.contextPath}/login">Login</a>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <div class="hero-stats">
                            <div class="stat"><strong>${movieCount}</strong><span class="muted">movies</span></div>
                            <div class="stat"><strong>${showCount}</strong><span class="muted">showtimes</span></div>
                        </div>
                    </div>
                </section>

                <section class="features">
                    <h2>How it works</h2>
                    <div class="steps">
                        <div class="step">
                            <span class="num">01</span>
                            <h3>Pick a movie</h3>
                            <p>Browse currently showing films with genre, rating, language, and duration.</p>
                        </div>
                        <div class="step">
                            <span class="num">02</span>
                            <h3>Choose show &amp; seats</h3>
                            <p>See live seat availability and watch the total price update as you select seats.</p>
                        </div>
                        <div class="step">
                            <span class="num">03</span>
                            <h3>Confirm booking</h3>
                            <p>Receive a booking confirmation and manage your tickets from My Bookings.</p>
                        </div>
                    </div>
                </section>

                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <p class="muted center">Signed in as <strong>${sessionScope.user}</strong>.
                            <a href="${pageContext.request.contextPath}/logout">Logout</a></p>
                    </c:when>
                    <c:otherwise>
                        <div class="signin-box">
                            <p>New here? Create an account to save your bookings.</p>
                            <a class="btn primary" href="${pageContext.request.contextPath}/register">Register</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </main>

            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.
                </p>
            </footer>
        </body>

        </html>