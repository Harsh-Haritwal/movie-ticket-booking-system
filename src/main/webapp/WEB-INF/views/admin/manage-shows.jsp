<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Manage Showtimes — Admin</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links">
                    <a href="${pageContext.request.contextPath}/admin">Admin Dashboard</a>
                    <c:if test="${sessionScope.role == 'admin'}"><a href="${pageContext.request.contextPath}/admin/movies">Manage Movies</a><a href="${pageContext.request.contextPath}/admin/bookings">Booked Tickets</a></c:if>
                    <a href="${pageContext.request.contextPath}/admin/shows" class="active">Showtimes &amp; Seats</a>
                    <span class="account-menu"><span class="user-chip">${sessionScope.user}</span><span class="account-dropdown"><a href="${pageContext.request.contextPath}/profile">Account Settings</a><a href="${pageContext.request.contextPath}/logout">Logout</a></span></span>
                </nav>
            </header>

            <main class="wrap">
                <header class="page-header">
                    <h1>Manage Showtimes and Seats</h1>
                    <p>Add each movie's theatre, screen, schedule, price, and seat capacity.</p>
                </header>

                <c:if test="${not empty param.error}">
                    <div class="alert error">${param.error}</div>
                </c:if>
                <c:if test="${not empty param.message}">
                    <div class="alert success">${param.message}</div>
                </c:if>

                <div class="admin-form-card">
                    <h3>${not empty edit ? 'Edit Showtime' : 'Add New Showtime'}</h3>
                    <form method="post" action="${pageContext.request.contextPath}/admin/shows">
                        <c:if test="${not empty edit}">
                            <input type="hidden" name="id" value="${show.id}" />
                        </c:if>
                        <label>Movie</label>
                        <select name="movieId" required>
                <c:forEach var="m" items="${movies}">
                    <option value="${m.id}" ${not empty edit and show.movieId == m.id ? 'selected' : ''}>${m.title}</option>
                </c:forEach>
            </select>
                        <label>Theatre</label>
                        <input type="text" name="theatre" value="${not empty edit ? show.theatre : ''}" required/>
                        <label>Screen</label>
                        <input type="text" name="screen" value="${not empty edit ? show.screen : ''}" required/>
                        <label>Date</label>
                        <input type="date" name="date" value="${not empty edit ? show.date : ''}" required/>
                        <label>Time</label>
                        <input type="time" name="time" value="${not empty edit ? show.timeInput : ''}" required/>
                        <input type="hidden" name="price" value="${not empty edit ? show.price : '0'}" />
                        <input type="hidden" name="totalSeats" value="${not empty edit ? show.totalSeats : '0'}" />
                        <label>Seat Declaration</label>
                        <input type="text" name="seatLayout" value="${not empty edit ? show.seatLayout : ''}" placeholder="A:12, B:10, C:8" required title="Use A:12, B:10. A row can contain at most 12 seats." />
                        <p class="field-help">Set each row and its seat count. Example: <strong>A:12, B:10</strong>. Maximum: <strong>12 seats per row</strong>. Seat capacity is calculated automatically.</p>
                        <label>Ticket Price Per Row (Rs.)</label>
                        <input type="text" name="rowPrices" value="${not empty edit ? show.rowPrices : ''}" placeholder="A:180, B:220, C:250" required/>
                        <p class="field-help">Set one price for every declared row. Example: <strong>A:180, B:220</strong>.</p>
                        <div class="form-actions">
                            <button class="btn primary" type="submit">${not empty edit ? 'Update Showtime' : 'Add Showtime'}</button>
                            <c:if test="${not empty edit}">
                                <a class="btn outline" href="${pageContext.request.contextPath}/admin/shows">Cancel</a>
                            </c:if>
                        </div>
                    </form>
                </div>

                <h2>Existing Showtimes</h2>
                <c:choose>
                    <c:when test="${empty shows}">
                        <p class="muted">No showtimes scheduled.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Movie</th>
                                    <th>Theatre</th>
                                    <th>Screen</th>
                                    <th>Date</th>
                                    <th>Time</th>
                                    <th>Price</th>
                                    <th>Seats</th>
                                    <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="s" items="${shows}">
                                    <tr>
                                        <td>${s.id}</td>
                                        <td>${s.movieId}</td>
                                        <td>${s.theatre}</td>
                                        <td>${s.screen}</td>
                                        <td>${s.date}</td>
                                        <td>${s.time}</td>
                                        <td>Rs.${s.price}</td>
                                        <td>${s.availableSeats}/${s.totalSeats}</td>
                                        <td class="text-right">
                                            <a class="btn outline small" href="${pageContext.request.contextPath}/admin/shows?action=edit&amp;id=${s.id}">Edit</a>
                                            <form method="post" action="${pageContext.request.contextPath}/admin/shows" style="display:inline" onsubmit="return confirm('Delete this showtime?')">
                                                <input type="hidden" name="id" value="${s.id}" />
                                                <button class="btn outline small" type="submit" name="action" value="delete">Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </main>

            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.</p>
            </footer>
        </body>

        </html>