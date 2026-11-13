<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Manage Movies — Admin</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links">
                    <a href="${pageContext.request.contextPath}/admin">Admin Dashboard</a>
                    <a href="${pageContext.request.contextPath}/admin/movies" class="active">Manage Movies</a>
                    <c:if test="${sessionScope.role == 'admin'}"><a href="${pageContext.request.contextPath}/admin/shows">Showtimes &amp; Seats</a><a href="${pageContext.request.contextPath}/admin/bookings">Booked Tickets</a></c:if>
                    <span class="account-menu"><span class="user-chip">${sessionScope.user}</span><span class="account-dropdown"><a href="${pageContext.request.contextPath}/profile">Account Settings</a><a href="${pageContext.request.contextPath}/logout">Logout</a></span></span>
                </nav>
            </header>

            <main class="wrap">
                <header class="page-header">
                    <h1>Manage Movies</h1>
                    <p>Add, edit, or remove movies from the catalogue.</p>
                </header>

                <c:if test="${not empty param.error}">
                    <div class="alert error">${param.error}</div>
                </c:if>
                <c:if test="${not empty param.message}">
                    <div class="alert success">${param.message}</div>
                </c:if>

                <div class="admin-form-card">
                    <h3>${not empty edit ? 'Edit Movie' : 'Add New Movie'}</h3>
                    <form method="post" action="${pageContext.request.contextPath}/admin/movies">
                        <c:if test="${not empty edit}">
                            <input type="hidden" name="id" value="${movie.id}" />
                        </c:if>
                        <label>Title</label>
                        <input type="text" name="title" value="${not empty edit ? movie.title : ''}" required/>
                        <label>Genre</label>
                        <input type="text" name="genre" value="${not empty edit ? movie.genre : ''}" required/>
                        <label>Language</label>
                        <input type="text" name="language" value="${not empty edit ? movie.language : ''}" required/>
                        <label>Duration (min)</label>
                        <input type="number" name="duration" value="${not empty edit ? movie.duration : ''}" required/>
                        <label>Rating (0-10)</label>
                        <input type="number" name="rating" step="0.1" value="${not empty edit ? movie.rating : ''}" required/>
                        <label>Poster Path</label>
                        <input type="text" name="poster" value="${not empty edit ? movie.poster : ''}" placeholder="posters/m101.jpg" />
                        <label>Description</label>
                        <textarea name="description" rows="3" required>${not empty edit ? movie.description : ''}</textarea>
                        <div class="form-actions">
                            <button class="btn primary" type="submit">${not empty edit ? 'Update' : 'Add'} Movie</button>
                            <c:if test="${not empty edit}">
                                <a class="btn outline" href="${pageContext.request.contextPath}/admin/movies">Cancel</a>
                            </c:if>
                        </div>
                    </form>
                </div>

                <section class="admin-form-card">
                    <h3>Next: Configure Theatre and Seats</h3>
                    <p class="muted">Theatre, screen, ticket price, date, time, and seat capacity are configured per showtime, because one movie can run in multiple theatres and screens.</p>
                    <a class="btn outline" href="${pageContext.request.contextPath}/admin/shows">Add a Showtime</a>
                </section>

                <h2>Movie Catalogue</h2>
                <c:choose>
                    <c:when test="${empty movies}">
                        <p class="muted">No movies in the catalogue.</p>
                    </c:when>
                    <c:otherwise>
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Title</th>
                                    <th>Genre</th>
                                    <th>Language</th>
                                    <th>Duration</th>
                                    <th>Rating</th>
                                    <th class="text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="m" items="${movies}">
                                    <tr>
                                        <td>${m.id}</td>
                                        <td>${m.title}</td>
                                        <td>${m.genre}</td>
                                        <td>${m.language}</td>
                                        <td>${m.duration} min</td>
                                        <td>${m.rating} / 10</td>
                                        <td class="text-right">
                                            <a class="btn outline small" href="${pageContext.request.contextPath}/admin/movies?action=edit&amp;id=${m.id}">Edit</a>
                                            <form method="post" action="${pageContext.request.contextPath}/admin/movies/delete" style="display:inline" onsubmit="return confirm('Delete this movie?')">
                                                <input type="hidden" name="id" value="${m.id}" />
                                                <button class="btn outline small" type="submit">Delete</button>
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