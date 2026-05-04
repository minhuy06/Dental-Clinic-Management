<%-- components/header.jsp - final version --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="com.dentalclinic.model.TaiKhoan" %>
<%
    String currentPage = request.getRequestURI();
    // Demo: kiem tra dang nhap tu session
    TaiKhoan Acc = (TaiKhoan) session.getAttribute("loggedInUser");
%>

<header class="header" id="header">
    <div class="container">
        <a href="${pageContext.request.contextPath}/index.jsp" class="logo">
            <div class="logo-icon">🦷</div>
            <div class="logo-text">Nha Khoa <span>Kvone</span></div>
        </a>

        <nav class="nav-menu" id="navMenu">
            <%
                boolean isIndex = currentPage.endsWith("/index.jsp") || currentPage.endsWith("/") || currentPage.endsWith("/Dental_Clinic_Management/");
                boolean isDatLich = currentPage.contains("dat-lich");
                boolean isHoSo = currentPage.contains("hoso");
            %>
            <a href="${pageContext.request.contextPath}/index.jsp"
                accesskey=""class="<%= isIndex ? "active" : "" %>">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/datlich#datlich"
                class="<%= isDatLich ? "active" : "" %>" data-section="datlich">Đặt lịch</a>
            <a href="${pageContext.request.contextPath}/datlich#dichvu"
                class="<%= isDatLich ? "" : "" %>" data-section="dichvu">Dịch vụ</a>
            <a href="${pageContext.request.contextPath}/datlich#bacsi"
                accesskey=""class="<%= isDatLich ? "" : "" %>" data-section="bacsi">Bác sĩ</a>

            <div class="nav-mobile-actions">
                <% if (Acc != null) { %>
                    <a href="${pageContext.request.contextPath}/patient/hoso.jsp" class="btn btn-primary" style="width:100%;">
                        👤 <%= Acc.getHoTen() %>
                    </a>
                <% } else { %>
                    <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="width:100%;">Đăng nhập</a>
                <% } %>
            </div>
        </nav>

        <div class="header-actions">
            <% if (Acc != null) { %>
                <div class="user-dropdown-wrapper">
                    <div class="user-avatar-btn" onclick="toggleUserDropdown()">
                        <div class="user-avatar-circle">👤</div>
                        <span class="user-avatar-name"><%= Acc.getHoTen() %></span>
                        <span class="user-arrow">▼</span>
                    </div>
                    <div class="user-dropdown" id="userDropdown">
                        <a href="${pageContext.request.contextPath}/patient/hoso.jsp?tab=info" class="user-dropdown-item">📋 Hồ sơ cá nhân</a>
                        <a href="${pageContext.request.contextPath}/patient/hoso.jsp?tab=history" class="user-dropdown-item">📅 Quản lý lịch hẹn</a>
                        <a href="${pageContext.request.contextPath}/patient/hoso.jsp?tab=password" class="user-dropdown-item">🔒 Bảo mật</a>
                        <div class="user-dropdown-divider"></div>
                        <a href="${pageContext.request.contextPath}/logout" class="user-dropdown-item logout-item">🚪 Đăng xuất</a>
                    </div>
                </div>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="padding:8px 20px;font-size:0.82rem;">Đăng nhập</a>
            <% } %>
            <button class="menu-toggle" onclick="toggleMenu()"><span></span><span></span><span></span></button>
        </div>
    </div>
</header>

            
<script>
    window.addEventListener('scroll', function() {
        var h = document.getElementById('header');
        if (window.scrollY > 50) h.classList.add('scrolled');
        else h.classList.remove('scrolled');
    });

    function toggleMenu() {
        document.getElementById('navMenu').classList.toggle('open');
    }

    document.querySelectorAll('.nav-menu a').forEach(function(l) {
        l.addEventListener('click', function() {
            document.getElementById('navMenu').classList.remove('open');
        });
    });

    function toggleUserDropdown() {
        var dd = document.getElementById('userDropdown');
        if (dd) dd.classList.toggle('show');
    }

    document.addEventListener('click', function(e) {
        var w = document.querySelector('.user-dropdown-wrapper');
        var dd = document.getElementById('userDropdown');
        if (w && dd && !w.contains(e.target)) {
            dd.classList.remove('show');
        }
    });
</script>
