var selectedRating = 0;
var reviewList = [];

function setRating(n) {
    selectedRating = n;
    document.querySelectorAll('#reviewStarsInput .star-input').forEach(function(s, i) {
        s.textContent = i < n ? '★' : '☆';
        s.style.color = i < n ? '#f39c12' : '#ccc';
    });
}

function submitReview() {
    var text = document.getElementById('reviewText').value.trim();
    if (!selectedRating) { alert('Vui lòng chọn số sao'); return; }
    if (!text) { alert('Vui lòng nhập nội dung đánh giá'); return; }

    // Lay ten nguoi dung tu session (truyen qua window.LOGGED_USER) hoac mac dinh
    var userName = window.LOGGED_USER || 'Khách hàng';
    var initials = userName.split(' ').map(function(w){ return w[0]; }).slice(-2).join('').toUpperCase();
    var stars = '';
    for (var i = 1; i <= 5; i++) stars += i <= selectedRating ? '★' : '☆';
    var today = new Date();
    var dateStr = today.getDate() + '/' + (today.getMonth()+1) + '/' + today.getFullYear();

    var card = document.createElement('div');
    card.className = 'review-card fade-in visible';
    card.innerHTML =
        '<div class="review-header">' +
            '<div class="review-avatar">' + initials + '</div>' +
            '<div class="review-info">' +
                '<div class="review-name">' + userName + '</div>' +
                '<div class="review-date">' + dateStr + '</div>' +
            '</div>' +
            '<div class="review-stars" style="color:#f39c12">' + stars + '</div>' +
        '</div>' +
        '<p class="review-text">' + text + '</p>';

    // Chen vao dau danh sach
    var list = document.getElementById('reviewsList');
    list.insertBefore(card, list.firstChild);

    document.getElementById('reviewText').value = '';
    setRating(0);
}
