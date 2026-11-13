<%@ page contentType="text/html;charset=UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Register — Movie Ticket Booking</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    </head>

    <body>
        <header class="navbar">
            <div class="brand">MovieTicket</div>
            <nav class="nav-links">
                <a href="${pageContext.request.contextPath}/">Home</a>
                <a href="${pageContext.request.contextPath}/login">Login</a>
            </nav>
        </header>

        <main class="wrap">
            <div class="auth-card">
                <h1>Create Account</h1>
                <c:if test="${not empty error}">
                    <div class="alert error">${error}</div>
                </c:if>
                <c:if test="${not empty message}">
                    <div class="alert success">${message}</div>
                </c:if>
                <form method="post" action="${pageContext.request.contextPath}/register">
                    <label>Username</label>
                    <input type="text" name="username" required/>
                    <label>Email</label>
                    <input type="email" name="email" required/>
                    <label>Password</label>
                    <input type="password" name="password" required/>
                    <label>Confirm Password</label>
                    <input type="password" name="confirmPassword" required/>
                    <button class="btn primary" type="submit">Register</button>
                </form>
                <p class="muted small">Already have an account? <a href="${pageContext.request.contextPath}/login">Sign In</a></p>
            </div>
        </main>

        <footer class="site-footer">
            <p>&copy; 2026 Movie Ticket Booking System.</p>
        </footer>
    </body>

    </html>