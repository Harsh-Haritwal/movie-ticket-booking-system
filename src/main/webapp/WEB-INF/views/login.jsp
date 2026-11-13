<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib prefix="c" uri="jakarta.tags.core" %>
        <!DOCTYPE html>
        <html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <title>Login — Movie Ticket Booking</title>
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <header class="navbar">
                <div class="brand">MovieTicket</div>
                <nav class="nav-links">
                    <a href="${pageContext.request.contextPath}/">Home</a>
                </nav>
            </header>

            <main class="wrap">
                <div class="auth-card">
                    <h1>${param.mode == 'admin' ? 'Admin Sign In' : 'Sign In'}</h1>
                    <c:if test="${not empty error}">
                        <div class="alert error">${error}</div>
                    </c:if>
                    <c:if test="${not empty message}">
                        <div class="alert success">${message}</div>
                    </c:if>
                    <form method="post" action="${pageContext.request.contextPath}/login">
                        <input type="hidden" name="next" value="${param.next}" />
                        <input type="hidden" name="mode" value="${param.mode}" />
                        <label>Username</label>
                        <input type="text" name="username" autocomplete="username" required/>
                        <label>Password</label>
                        <input type="password" name="password" autocomplete="current-password" required/>
                        <button class="btn primary" type="submit">Sign In</button>
                    </form>
                    <p class="muted small">New here? <a href="${pageContext.request.contextPath}/register">Register</a></p>
                </div>
            </main>

            <footer class="site-footer">
                <p>&copy; 2026 Movie Ticket Booking System.</p>
            </footer>
        </body>

        </html>