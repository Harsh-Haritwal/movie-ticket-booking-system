<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Sign in — Movie Ticket Booking</title>
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <%@ include file="nav.jsp" %>
                <div class="wrap">
                    <div class="auth-card">
                        <h1>Sign in</h1>
                        <c:if test="${not empty error}">
                            <p class="err">${error}</p>
                        </c:if>
                        <form method="post" action="${pageContext.request.contextPath}/login">
                            <input type="hidden" name="next" value="${param.next}" />
                            <label>Username</label>
                            <input name="username" placeholder="Your username" autocomplete="username" required/>
                            <label>Password</label>
                            <input type="password" name="password" placeholder="Your password" autocomplete="current-password" required/>
                            <button class="btn" type="submit">Sign in</button>
                        </form>
                        <p class="dim small">New here? <a href="${pageContext.request.contextPath}/register">Create an account</a>.</p>
                    </div>
                </div>
        </body>

        </html>