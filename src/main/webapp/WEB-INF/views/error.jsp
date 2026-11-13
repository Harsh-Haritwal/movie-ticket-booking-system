<%@ page contentType="text/html;charset=UTF-8" %>
    <!DOCTYPE html>
    <html lang="en">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>Error — Movie Ticket Booking</title>
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
                <h1>Error</h1>
                <div class="alert error">
                    <strong>${pageContext.request.getAttribute('message')}</strong>
                </div>
                <a class="btn primary" href="${pageContext.request.contextPath}/">Return Home</a>
            </div>
        </main>

        <footer class="site-footer">
            <p>&copy; 2026 Movie Ticket Booking System.</p>
        </footer>
    </body>

    </html>