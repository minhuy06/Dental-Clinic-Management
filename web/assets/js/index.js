var selectedRating = 0;

function setRating(n) {
    selectedRating = n;
    document.querySelectorAll('#reviewStarsInput .star-input').forEach(function(s, i) {
        s.textContent = i < n ? '★' : '☆';
        s.style.color = i < n ? '#f39c12' : '#ccc';
    });
}

function submitReview() {
    var text = document.getElementById('reviewText').value.trim();
    if (!selectedRating) {
        alert('Vui lòng chọn số sao');
        return;
    }
    if (!text) {
        alert('Vui lòng nhập nội dung đánh giá');
        return;
    }
    alert('✅ Cảm ơn bạn đã gửi đánh giá!');
    document.getElementById('reviewText').value = '';
    setRating(0);
}
