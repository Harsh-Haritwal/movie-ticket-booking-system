<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <nav class="nav">
            <a class="brand" href="${pageContext.request.contextPath}/">🎬 <span>MovieTicket</span></a>
            <div class="links">
                <a class="pill" href="${pageContext.request.contextPath}/movies">Movies</a>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <a class="pill" href="${pageContext.request.contextPath}/history">My Bookings</a>
                        <span class="who" title="Signed in">👤 ${sessionScope.user}</span>
                        <a class="pill" href="${pageContext.request.contextPath}/logout">Sign out</a>
                    </c:when>
                    <c:otherwise>
                        <a class="pill" href="${pageContext.request.contextPath}/login?next=/history">My Bookings</a>
                        <a class="pill" href="${pageContext.request.contextPath}/login">Login</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </nav>