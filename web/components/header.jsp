<%-- components/header.jsp v3 --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String currentPage = request.getRequestURI();
%>

<header class="header" id="header">
    <div class="container">
        <!-- Logo -->
        <a href="${pageContext.request.contextPath}/index.jsp" class="logo">
            <div class="logo-icon">🦷</div>
            <div class="logo-text">Nha Khoa <span>Kvone</span></div>
        </a>

        <!-- Navigation -->
        <nav class="nav-menu" id="navMenu">
            <a href="${pageContext.request.contextPath}/index.jsp"
               class="<%= currentPage.contains("index") ? "active" : "" %>">
                Trang chủ
            </a>
            <a href="${pageContext.request.contextPath}/dat-lich.jsp"
               class="<%= currentPage.contains("dat-lich") ? "active" : "" %>">
                Đặt lịch
            </a>
            <a href="${pageContext.request.contextPath}/dat-lich.jsp"
               class="<%= currentPage.contains("dichvu") ? "active" : "" %>">
                Dịch vụ
            </a>
            <a href="${pageContext.request.contextPath}/dat-lich.jsp#bacsi">
                Bác sĩ
            </a>

            <!-- Nut chi hien tren mobile -->
            <div class="nav-mobile-actions">
                <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="width:100%;">
                    Đăng nhập
                </a>
            </div>
        </nav>

        <!-- Header Actions -->
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline"
               style="padding: 8px 20px; font-size: 0.82rem;">
                Đăng nhập
            </a>

            <!-- Mobile Menu Button -->
            <button class="menu-toggle" id="menuToggle" onclick="toggleMenu()">
                <span></span>
                <span></span>
                <span></span>
            </button>
        </div>
    </div>
</header>

<script>
    window.addEventListener('scroll', function() {
        var header = document.getElementById('header');
        if (window.scrollY > 50) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
    });

    function toggleMenu() {
        document.getElementById('navMenu').classList.toggle('open');
    }

    document.querySelectorAll('.nav-menu a').forEach(function(link) {
        link.addEventListener('click', function() {
            document.getElementById('navMenu').classList.remove('open');
        });
    });
</script>
