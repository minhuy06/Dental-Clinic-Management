<%-- components/header.jsp - final version --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String currentPage = request.getRequestURI();
    // Demo: kiem tra dang nhap tu session
    String loggedInUser = (String) session.getAttribute("loggedInUser");
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
            <a href="${pageContext.request.contextPath}/dat-lich.jsp#datlich"
                class="<%= isDatLich ? "active" : "" %>" data-section="datlich">Đặt lịch</a>
            <a href="${pageContext.request.contextPath}/dat-lich.jsp#dichvu"
                class="<%= isDatLich ? "" : "" %>" data-section="dichvu">Dịch vụ</a>
            <a href="${pageContext.request.contextPath}/dat-lich.jsp#bacsi"
                accesskey=""class="<%= isDatLich ? "" : "" %>" data-section="bacsi">Bác sĩ</a>

            <div class="nav-mobile-actions">
                <% if (loggedInUser != null) { %>
                    <a href="${pageContext.request.contextPath}/patient/hoso.jsp" class="btn btn-primary" style="width:100%;">
                        👤 <%= loggedInUser %>
                    </a>
                <% } else { %>
                    <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="width:100%;">Đăng nhập</a>
                <% } %>
            </div>
        </nav>

        <div class="header-actions">
            <% if (loggedInUser != null) { %>
                <a href="${pageContext.request.contextPath}/patient/hoso.jsp" class="user-avatar-btn">
                    <div class="user-avatar-circle">👤</div>
                    <span class="user-avatar-name"><%= loggedInUser %></span>
                </a>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="padding:8px 20px;font-size:0.82rem;">Đăng nhập</a>
            <% } %>
            <button class="menu-toggle" onclick="toggleMenu()"><span></span><span></span><span></span></button>
        </div>
    </div>
</header>

<script>
window.addEventListener('scroll',function(){
    var h=document.getElementById('header');
    if(window.scrollY>50)h.classList.add('scrolled');else h.classList.remove('scrolled');

    // Auto highlight menu theo section dang xem (chi khi o trang dat-lich)
    if(window.location.pathname.indexOf('dat-lich')>-1){
        var sections=['datlich','dichvu','bacsi'];
        var current='';
        sections.forEach(function(id){
            var sec=document.getElementById(id);
            if(sec){
                var rect=sec.getBoundingClientRect();
                if(rect.top<=120 && rect.bottom>=120){current=id;}
            }
        });
        document.querySelectorAll('.nav-menu a[data-section]').forEach(function(a){
            a.classList.toggle('active',a.getAttribute('data-section')===current);
        });
    }
});
function toggleMenu(){document.getElementById('navMenu').classList.toggle('open');}
document.querySelectorAll('.nav-menu a').forEach(function(l){l.addEventListener('click',function(){document.getElementById('navMenu').classList.remove('open');});});
</script>
