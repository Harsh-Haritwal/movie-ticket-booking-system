<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Movie Ticket Booking — Home</title>
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <%@ include file="nav.jsp" %>
                <div class="wrap">
                    <section class="hero">
                        <div class="hero-text">
                            <span class="kicker">Now showing</span>
                            <h1>Book your next seat in seconds</h1>
                            <p>Pick a film, choose your showtime, select seats visually, and receive an instant confirmation — all stored in simple XML files.</p>
                            <div class="hero-actions">
                                <a class="btn" href="${pageContext.request.contextPath}/movies">Browse movies</a>
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user}">
                                        <a class="btn ghost" href="${pageContext.request.contextPath}/history">My bookings</a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="btn ghost" href="${pageContext.request.contextPath}/login?next=/history">My bookings</a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div class="hero-stats">
                                <div class="stat"><strong>${movieCount}</strong><span class="dim">movies live</span></div>
                                <div class="stat"><strong>${showCount}</strong><span class="dim">shows this week</span></div>
                                <div class="stat"><strong>XML</strong><span class="dim">storage — no DB</span></div>
                            </div>
                        </div>
                    </section>

                    <section class="how-it-works">
                        <h2>How it works</h2>
                        <div class="steps">
                            <div class="step"><span class="num">01</span>
                                <h3>Pick a movie</h3>
                                <p>Browse now-showing films with genre, rating, language, and duration at a glance.</p>
                            </div>
                            <div class="step"><span class="num">02</span>
                                <h3>Choose show &amp; seats</h3>
                                <p>See live seat availability, pick your spots, and watch the price total update.</p>
                            </div>
                            <div class="step"><span class="num">03</span>
                                <h3>Confirm &amp; manage</h3>
                                <p>Get a ticket stub and manage your bookings — cancel with one click.</p>
                            </div>
                        </div>
                    </section>

                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <p class="dim center">Signed in as <strong>${sessionScope.user}</strong> — your tickets are saved under this name.
                                <a href="${pageContext.request.contextPath}/logout">Sign out</a></p>
                        </c:when>
                        <c:otherwise>
                            <div class="signin-box">
                                <p>New here? Sign in to save your bookings.</p>
                                <a class="btn" href="${pageContext.request.contextPath}/login">Sign in</a>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
        </body>

        </html>