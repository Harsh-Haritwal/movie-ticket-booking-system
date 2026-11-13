<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Select Seats — Movie Ticket Booking</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
            <script src="${pageContext.request.contextPath}/js/app.js" defer></script>
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
                <header class="page-header">
                    <h1>Select Seats — ${show.id}</h1>
                    <p>${show.theatre} · ${show.screen} · ${show.date} · ${show.time} · Row-wise ticket prices apply</p>
                    <p class="muted"><strong>${show.availableSeats}</strong> of <strong>${show.totalSeats}</strong> seats available</p>
                </header>

                <c:if test="${not empty error}">
                    <div class="alert error">${error}</div>
                </c:if>

                <form id="seatForm" method="post" action="${pageContext.request.contextPath}/book" data-price="${show.price}">
                    <input type="hidden" name="showId" value="${show.id}" />

                    <div class="screen-wrap">
                        <div class="screen">SCREEN</div>
                        <div class="legend">
                            <span><span class="dot available"></span>Available</span>
                            <span><span class="dot booked"></span>Booked</span>
                            <span><span class="dot selected"></span>Selected</span>
                        </div>
                        <div class="seats-grid">
                            <c:forEach var="row" items="${seatRows}">
                                <div class="seat-row">
                                    <span class="seat-row-label">${row.key}</span>
                                    <div class="seat-row-seats">
                                        <c:forEach var="seat" items="${row.value}" varStatus="seatStatus">
                                            <c:if test="${seatStatus.index == row.value.size() div 2}"><span class="aisle" aria-hidden="true"></span></c:if>
                                            <c:choose>
                                                <c:when test="${show.bookedSeats.contains(seat)}">
                                                    <span class="seat booked" title="Rs.${rowPrices[row.key]}">${seat}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <label class="seat available">
                                            <input type="checkbox" name="seat" value="${seat}" data-price="${rowPrices[row.key]}"/>
                                            ${seat}
                                        </label>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </div>
                                    <span class="seat-row-price">Rs.${rowPrices[row.key]}</span>
                                    <span class="seat-row-label seat-row-label-right">${row.key}</span>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <div class="bookbar">
                        <c:choose>
                            <c:when test="${not empty sessionScope.user}">
                                <span class="user-chip">Booking as ${sessionScope.user}</span>
                                <input type="hidden" name="user" value="${sessionScope.user}" />
                            </c:when>
                            <c:otherwise>
                                <span class="muted small">Sign in to reserve the seats you select.</span>
                            </c:otherwise>
                        </c:choose>
                        <span class="summary"><span id="count">0</span> selected · <strong>Rs.<span id="total">0.00</span></strong></span>
                        <c:choose>
                            <c:when test="${not empty sessionScope.user}">
                                <button class="btn primary" id="bookBtn" type="submit" disabled>Book Selected Seats</button>
                            </c:when>
                            <c:otherwise>
                                <a class="btn primary" href="${pageContext.request.contextPath}/login?next=/seats%3FshowId%3D${show.id}">Login to Book</a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </form>
            </main>

            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.</p>
            </footer>
        </body>

        </html>