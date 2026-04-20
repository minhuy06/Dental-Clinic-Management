<%-- components/header.jsp - FINAL --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String currentPage = request.getRequestURI();
    String loggedInUser = (String) session.getAttribute("loggedInUser");
    boolean isIndex = currentPage.endsWith("index.jsp") || currentPage.endsWith("/");
    boolean isDatLich = currentPage.contains("dat-lich");
    boolean isHoSo = currentPage.contains("hoso");
%>

<header class="header" id="header">
    <div class="container">
        <a href="${pageContext.request.contextPath}/index.jsp" class="logo">
            <div class="logo-icon">🦷</div>
            <div class="logo-text">Nha Khoa <span>Kvone</span></div>
        </a>
        <nav class="nav-menu" id="navMenu">
            <a href="${pageContext.request.contextPath}/index.jsp" class="<%= isIndex ? "active" : "" %>">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/dat-lich.jsp#datlich" data-section="datlich">Đặt lịch</a>
            <a href="${pageContext.request.contextPath}/dat-lich.jsp#dichvu" data-section="dichvu">Dịch vụ</a>
            <a href="${pageContext.request.contextPath}/dat-lich.jsp#bacsi" data-section="bacsi">Bác sĩ</a>
            <div class="nav-mobile-actions">
                <% if (loggedInUser != null) { %>
                    <a href="${pageContext.request.contextPath}/patient/hoso.jsp" class="btn btn-primary" style="width:100%;">👤 <%= loggedInUser %></a>
                <% } else { %>
                    <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="width:100%;">Đăng nhập</a>
                <% } %>
            </div>
        </nav>
        <div class="header-actions">
            <% if (loggedInUser != null) { %>
                <div class="user-dropdown-wrapper">
                    <div class="user-avatar-btn" onclick="toggleUserDropdown()">
                        <div class="user-avatar-circle">👤</div>
                        <span class="user-avatar-name"><%= loggedInUser %></span>
                        <span class="user-arrow">▼</span>
                    </div>
                    <div class="user-dropdown" id="userDropdown">
                        <a href="${pageContext.request.contextPath}/patient/hoso.jsp?tab=info" class="user-dropdown-item">📋 Hồ sơ cá nhân</a>
                        <a href="${pageContext.request.contextPath}/patient/hoso.jsp?tab=history" class="user-dropdown-item">📅 Quản lý lịch hẹn</a>
                        <a href="${pageContext.request.contextPath}/patient/hoso.jsp?tab=password" class="user-dropdown-item">🔒 Bảo mật</a>
                        <div class="user-dropdown-divider"></div>
                        <a href="javascript:void(0)" onclick="doLogoutNow()" class="user-dropdown-item logout-item">🚪 Đăng xuất</a>
                    </div>
                </div>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="padding:8px 20px;font-size:0.82rem;">Đăng nhập</a>
            <% } %>
            <button class="menu-toggle" onclick="toggleMenu()"><span></span><span></span><span></span></button>
        </div>
    </div>
</header>

<!-- SYSTEM UPDATING MODAL (dung chung) -->
<div class="system-modal-overlay" id="systemModal" onclick="if(event.target===this)closeSystemModal()">
    <div class="system-modal">
        <span class="sys-icon">🔧</span>
        <div class="sys-title">Tính năng đang phát triển</div>
        <div class="sys-msg">Chúng tôi đang nỗ lực hoàn thiện tính năng này. Vui lòng quay lại sau!</div>
        <button class="sys-close" onclick="closeSystemModal()">Đã hiểu</button>
    </div>
</div>

<!-- CHATBOX -->
<button class="chatbox-btn" id="chatboxBtn" onclick="toggleChatbox()">💬</button>
<div class="chatbox-window" id="chatboxWindow">
    <div class="chatbox-header">
        <h4>💬 Hỗ trợ trực tuyến</h4>
        <button class="chat-close" onclick="toggleChatbox()">✕</button>
    </div>
    <div class="chatbox-body" id="chatboxBody">
        <div class="chat-msg bot">Xin chào! Tôi là trợ lý ảo của Nha Khoa Kvone. Bạn cần hỗ trợ gì?</div>
    </div>
    <div class="chatbox-quick" id="chatQuickBtns">
        <button class="quick-btn" onclick="sendQuick(this)">Giờ làm việc</button>
        <button class="quick-btn" onclick="sendQuick(this)">Địa chỉ</button>
        <button class="quick-btn" onclick="sendQuick(this)">Đặt lịch</button>
        <button class="quick-btn" onclick="sendQuick(this)">Bảng giá</button>
        <button class="quick-btn" onclick="sendQuick(this)">Liên hệ</button>
    </div>
    <div class="chatbox-input">
        <input type="text" id="chatInput" placeholder="Nhập tin nhắn..." onkeypress="if(event.key==='Enter')sendChat()">
        <button onclick="sendChat()">Gửi</button>
    </div>
</div>

<script>
    // Header scroll + section highlight
    window.addEventListener('scroll', function() {
        var h = document.getElementById('header');
        if (window.scrollY > 50) h.classList.add('scrolled');
        else h.classList.remove('scrolled');
        if (window.location.pathname.indexOf('dat-lich') > -1) {
            var sections = ['datlich','dichvu','bacsi'], current = '';
            sections.forEach(function(id) {
                var sec = document.getElementById(id);
                if (sec) { var r = sec.getBoundingClientRect(); if (r.top <= 150 && r.bottom >= 150) current = id; }
            });
            document.querySelectorAll('.nav-menu a').forEach(function(a) { a.classList.remove('active'); });
            if (current) { document.querySelectorAll('.nav-menu a').forEach(function(a) { if ((a.getAttribute('href')||'').indexOf('#'+current) > -1) a.classList.add('active'); }); }
        }
    });
    function toggleMenu() { document.getElementById('navMenu').classList.toggle('open'); }
    document.querySelectorAll('.nav-menu a').forEach(function(l) { l.addEventListener('click', function() { document.getElementById('navMenu').classList.remove('open'); }); });

    // User dropdown
    function toggleUserDropdown() { var dd = document.getElementById('userDropdown'); if (dd) dd.classList.toggle('show'); }
    document.addEventListener('click', function(e) { var w = document.querySelector('.user-dropdown-wrapper'); var dd = document.getElementById('userDropdown'); if (w && dd && !w.contains(e.target)) dd.classList.remove('show'); });

    // Logout inline (khong can logout.jsp)
    function doLogoutNow() {
        if (confirm('Bạn có chắc muốn đăng xuất?')) {
            // Xoa session bang cach goi trang voi param logout
            window.location.href = '${pageContext.request.contextPath}/index.jsp?logout=true';
        }
    }

    // System modal
    function showSystemModal() { document.getElementById('systemModal').classList.add('show'); }
    function closeSystemModal() { document.getElementById('systemModal').classList.remove('show'); }

    // Chatbox
    function toggleChatbox() {
        var w = document.getElementById('chatboxWindow'), b = document.getElementById('chatboxBtn');
        w.classList.toggle('show'); b.classList.toggle('active');
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
        setTimeout(function() { addChatMsg(chatResponses[key] || 'Cảm ơn bạn đã liên hệ. Nhân viên sẽ hỗ trợ bạn sớm nhất!', 'bot'); }, 500);
    }

    function sendChat() {
        var inp = document.getElementById('chatInput'), msg = inp.value.trim();
        if (!msg) return;
        addChatMsg(msg, 'user');
        inp.value = '';
        var lower = msg.toLowerCase(), reply = 'Cảm ơn bạn! Nhân viên sẽ phản hồi sớm nhất có thể. Hotline: 1900 1533';
        if (lower.indexOf('giờ') > -1 || lower.indexOf('mấy giờ') > -1) reply = chatResponses['giờ làm việc'];
        else if (lower.indexOf('địa chỉ') > -1 || lower.indexOf('ở đâu') > -1) reply = chatResponses['địa chỉ'];
        else if (lower.indexOf('đặt lịch') > -1 || lower.indexOf('đặt hẹn') > -1) reply = chatResponses['đặt lịch'];
        else if (lower.indexOf('giá') > -1 || lower.indexOf('bảng giá') > -1 || lower.indexOf('bao nhiêu') > -1) reply = chatResponses['bảng giá'];
        else if (lower.indexOf('liên hệ') > -1 || lower.indexOf('hotline') > -1 || lower.indexOf('điện thoại') > -1) reply = chatResponses['liên hệ'];
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
</script>
