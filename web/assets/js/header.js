window.addEventListener('scroll', function() {
    var h = document.getElementById('header');
    if (window.scrollY > 50) h.classList.add('scrolled');
    else h.classList.remove('scrolled');

    if (window.location.pathname.indexOf('dat-lich') > -1) {
        var sections = ['datlich', 'dichvu', 'bacsi'];
        var current = '';
        sections.forEach(function(id) {
            var sec = document.getElementById(id);
            if (sec) {
                var r = sec.getBoundingClientRect();
                if (r.top <= 150 && r.bottom >= 150) current = id;
            }
        });
        document.querySelectorAll('.nav-menu a').forEach(function(a) {
            a.classList.remove('active');
        });
        if (current) {
            document.querySelectorAll('.nav-menu a').forEach(function(a) {
                if ((a.getAttribute('href') || '').indexOf('#' + current) > -1) {
                    a.classList.add('active');
                }
            });
        }
    }
});

// Mobile menu toggle
function toggleMenu() {
    document.getElementById('navMenu').classList.toggle('open');
}

// Close mobile menu when clicking link
document.querySelectorAll('.nav-menu a').forEach(function(l) {
    l.addEventListener('click', function() {
        document.getElementById('navMenu').classList.remove('open');
    });
});

// User dropdown toggle
function toggleUserDropdown() {
    var dd = document.getElementById('userDropdown');
    if (dd) dd.classList.toggle('show');
}

// Close dropdown when clicking outside
document.addEventListener('click', function(e) {
    var w = document.querySelector('.user-dropdown-wrapper');
    var dd = document.getElementById('userDropdown');
    if (w && dd && !w.contains(e.target)) {
        dd.classList.remove('show');
    }
});

// Logout
function doLogoutNow() {
    if (confirm('Bạn có chắc muốn đăng xuất?')) {
        window.location.href = window.CONTEXT_PATH + '/index.jsp?logout=true';
    }
}

// System "coming soon" modal
function showSystemModal() {
    document.getElementById('systemModal').classList.add('show');
}
function closeSystemModal() {
    document.getElementById('systemModal').classList.remove('show');
}

/* ============================================
   CHATBOX
   ============================================ */
function toggleChatbox() {
    var w = document.getElementById('chatboxWindow');
    var b = document.getElementById('chatboxBtn');
    w.classList.toggle('show');
    b.classList.toggle('active');
    b.textContent = w.classList.contains('show') ? '✕' : '💬';
}

var chatResponses = {
    'giờ làm việc': 'Thứ 2 - Thứ 7: 8:00 - 17:00\nChủ nhật: 8:00 - 12:00',
    'địa chỉ': '📍 48 Cao Thắng, Hải Châu, TP. Đà Nẵng',
    'đặt lịch': 'Bạn có thể đặt lịch trực tiếp tại trang Đặt lịch hoặc gọi hotline 1900 1533.',
    'bảng giá': 'Bạn có thể xem bảng giá đầy đủ tại mục Dịch vụ trên website.',
    'liên hệ': '📞 Hotline: 1900 1533\n✉️ Email: info@nhakhoakvone.vn'
};

function sendQuick(btn) {
    var q = btn.textContent;
    addChatMsg(q, 'user');
    var key = q.toLowerCase();
    setTimeout(function() {
        addChatMsg(chatResponses[key] || 'Cảm ơn bạn đã liên hệ. Nhân viên sẽ hỗ trợ bạn sớm nhất!', 'bot');
    }, 500);
}

function sendChat() {
    var inp = document.getElementById('chatInput');
    var msg = inp.value.trim();
    if (!msg) return;
    addChatMsg(msg, 'user');
    inp.value = '';
    var lower = msg.toLowerCase();
    var reply = 'Cảm ơn bạn! Nhân viên sẽ phản hồi sớm nhất có thể. Hotline: 1900 1533';
    if (lower.indexOf('giờ') > -1 || lower.indexOf('mấy giờ') > -1) reply = chatResponses['giờ làm việc'];
    else if (lower.indexOf('địa chỉ') > -1 || lower.indexOf('ở đâu') > -1) reply = chatResponses['địa chỉ'];
    else if (lower.indexOf('đặt lịch') > -1 || lower.indexOf('đặt hẹn') > -1) reply = chatResponses['đặt lịch'];
    else if (lower.indexOf('giá') > -1 || lower.indexOf('bảng giá') > -1 || lower.indexOf('bao nhiêu') > -1) reply = chatResponses['bảng giá'];
    else if (lower.indexOf('liên hệ') > -1 || lower.indexOf('hotline') > -1) reply = chatResponses['liên hệ'];
    else if (lower.indexOf('cảm ơn') > -1 || lower.indexOf('thanks') > -1) reply = 'Không có gì ạ! Chúc bạn một ngày tốt lành! 😊';
    else if (lower.indexOf('xin chào') > -1 || lower.indexOf('hello') > -1 || lower.indexOf('hi') > -1) reply = 'Chào bạn! Rất vui được hỗ trợ. Bạn cần tư vấn gì ạ?';
    setTimeout(function() { addChatMsg(reply, 'bot'); }, 600);
}

function addChatMsg(text, type) {
    var body = document.getElementById('chatboxBody');
    var div = document.createElement('div');
    div.className = 'chat-msg ' + type;
    div.textContent = text;
    body.appendChild(div);
    body.scrollTop = body.scrollHeight;
}
