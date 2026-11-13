/* Movie Ticket Booking — seat selection behavior */
(function() {
    var form = document.getElementById('seatForm');
    if (!form) return;

    var boxes = form.querySelectorAll('input[name="seat"]');
    var countEl = document.getElementById('count');
    var totalEl = document.getElementById('total');
    var btn = document.getElementById('bookBtn');

    function update() {
        var n = 0;
        var total = 0;
        boxes.forEach(function(b) {
            var label = b.closest('label.seat');
            if (label) {
                label.classList.toggle('selected', b.checked);
            }
            if (b.checked) {
                n++;
                total += parseFloat(b.getAttribute('data-price')) || 0;
            }
        });
        if (countEl) countEl.textContent = n;
        if (totalEl) totalEl.textContent = total.toFixed(2);
        if (btn) {
            btn.disabled = (n === 0);
            btn.textContent = n === 0 ? 'Book Selected Seats' : ('Book ' + n + ' Seat' + (n > 1 ? 's' : ''));
        }
    }

    boxes.forEach(function(b) {
        b.addEventListener('change', update);
    });

    update();
})();