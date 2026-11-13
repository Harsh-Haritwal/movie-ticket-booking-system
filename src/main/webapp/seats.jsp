<%@ page contentType="text/html;charset=UTF-8" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <title>Select Seats — ${show.id}</title>
            <meta name="viewport" content="width=device-width, initial-scale=1" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
        </head>

        <body>
            <%@ include file="nav.jsp" %>
                <div class="wrap">
                    <header class="page-header">
                        <div>
                            <h1>Select seats — ${show.id}</h1>
                            <p>${show.screen} · ${show.date} · ${show.time} · <strong>Rs.${show.priceLabel}</strong> per ticket · <strong>${show.availableCount}</strong> seats available</p>
                        </div>
                    </header>
                    <c:if test="${not empty error}">
                        <p class="err">${error}</p>
                    </c:if>
                    <form id="bookForm" method="post" action="${pageContext.request.contextPath}/book">
                        <input type="hidden" name="showId" value="${show.id}" />
                        <div class="screen-wrap">
                            <div class="screen">SCREEN</div>
                            <div class="legend">
                                <span><span class="dot d-a"></span>Available</span>
                                <span><span class="dot d-b"></span>Booked</span>
                                <span><span class="dot d-s"></span>Selected</span>
                            </div>
                            <div class="seats">
                                <c:forEach var="seat" items="${allSeats}">
                                    <c:choose>
                                        <c:when test="${show.bookedSeats.contains(seat)}"><span class="seat booked">${seat}</span></c:when>
                                        <c:otherwise><label class="seat" data-seat="${seat}"><input type="checkbox" name="seat" value="${seat}"/>${seat}</label></c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </div>
                            <div class="bookbar">
                                <c:choose>
                                    <c:when test="${not empty sessionScope.user}">
                                        <span class="who" style="flex:1">Booking as <b>${sessionScope.user}</b></span>
                                        <input type="hidden" name="user" value="${sessionScope.user}" />
                                    </c:when>
                                    <c:otherwise>
                                        <input type="text" name="user" placeholder="Your name (signs you in)" />
                                        <span class="dim small">You will be signed in as this name</span>
                                    </c:otherwise>
                                </c:choose>
                                <span class="total"><span id="count">0</span> selected · <strong>Rs.<span id="total">0</span></strong></span>
                                <button id="bookBtn" class="btn" type="submit" disabled>Book selected seats</button>
                            </div>
                        </div>
                    </form>
                </div>
                <script>
                    (function() {
                        var price = parseFloat("${show.priceLabel}");
                        var boxes = document.querySelectorAll('input[name="seat"]');
                        var countEl = document.getElementById('count'),
                            totalEl = document.getElementById('total'),
                            btn = document.getElementById('bookBtn');

                        function upd() {
                            var n = 0;
                            boxes.forEach(function(b) {
                                b.closest('label.seat').classList.toggle('sel', b.checked);
                                if (b.checked) n++;
                            });
                            countEl.textContent = n;
                            totalEl.textContent = (n * price).toFixed(0);
                            btn.disabled = (n === 0);
                            btn.textContent = n === 0 ? "Book selected seats" : ("Book " + n + " seat" + (n > 1 ? "s" : ""));
                        }
                        boxes.forEach(function(b) {
                            b.addEventListener('change', upd);
                        });
                        upd();
                    })();
                </script>
        </body>

        </html>