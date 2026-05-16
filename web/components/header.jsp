<%-- components/header.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.dentalclinic.model.TaiKhoan" %>
<%@ page import="com.dentalclinic.utils.RoleNavHelper" %>
<%
    String currentPage = request.getRequestURI();
    TaiKhoan loggedInUser = (TaiKhoan) session.getAttribute("loggedInUser");
    String ctx = request.getContextPath();
    String homeUrl = RoleNavHelper.getHomeUrl(ctx);
    String scheduleUrl = RoleNavHelper.getScheduleUrl(ctx);
    String serviceUrl = RoleNavHelper.getServiceUrl(ctx);
    String doctorUrl = RoleNavHelper.getDoctorUrl(ctx);
    String workspaceUrl = "";
    boolean isPatient = true;
    boolean isStaff = false;
    if (loggedInUser != null) {
        workspaceUrl = RoleNavHelper.getWorkspaceUrl(ctx, loggedInUser.getVaiTro());
        isPatient = RoleNavHelper.isPatient(loggedInUser.getVaiTro());
        isStaff = !isPatient;
    }
    boolean isIndex = currentPage.endsWith("index.jsp")
            || currentPage.endsWith(ctx + "/")
            || currentPage.endsWith("/Dental_Clinic_Management")
            || currentPage.endsWith("/Dental_Clinic_Management/");
    boolean isSchedule = currentPage.contains("/Infor/Schedule");
    boolean isService = currentPage.contains("/Infor/service");
    boolean isDoctor = currentPage.contains("/Infor/Doctor");
%>

<header class="header" id="header">
    <div class="container">
        <a href="<%= homeUrl %>" class="logo">
            <div class="logo-icon">🦷</div>
            <div class="logo-text">Nha Khoa <span>5AE</span></div>
        </a>
        <nav class="nav-menu" id="navMenu">
            <a href="<%= homeUrl %>" class="<%= isIndex ? "active" : "" %>">Trang chủ</a>
            <a href="<%= scheduleUrl %>" class="<%= isSchedule ? "active" : "" %>" data-section="datlich">Đặt lịch</a>
            <a href="<%= serviceUrl %>" class="<%= isService ? "active" : "" %>" data-section="dichvu">Dịch vụ</a>
            <a href="<%= doctorUrl %>" class="<%= isDoctor ? "active" : "" %>" data-section="bacsi">Bác sĩ</a>
            <div class="nav-mobile-actions">
                <% if (loggedInUser != null) { %>
                    <a href="<%= workspaceUrl %>" class="btn btn-primary" style="width:100%;">👤 <%= loggedInUser.getHoTen() %></a>
                <% } else { %>
                    <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="width:100%;">Đăng nhập</a>
                <% } %>
            </div>
        </nav>
        <div class="header-actions">
            <% if (loggedInUser != null) { %>
                <div class="user-dropdown-wrapper">
                    <% if (isStaff) { %>
                        <a href="<%= workspaceUrl %>" class="user-avatar-btn user-avatar-btn--link" title="Vào trang làm việc">
                            <div class="user-avatar-circle">👤</div>
                            <span class="user-avatar-name"><%= loggedInUser.getHoTen() %></span>
                        </a>
                    <% } else { %>
                        <div class="user-avatar-btn" onclick="toggleUserDropdown()">
                            <div class="user-avatar-circle">👤</div>
                            <span class="user-avatar-name"><%= loggedInUser.getHoTen() %></span>
                            <span class="user-arrow">▼</span>
                        </div>
                        <div class="user-dropdown" id="userDropdown">
                            <a href="${pageContext.request.contextPath}/hoso?tab=info" class="user-dropdown-item">📋 Hồ sơ cá nhân</a>
                            <a href="${pageContext.request.contextPath}/hoso?tab=history" class="user-dropdown-item">📅 Quản lý lịch hẹn</a>
                            <a href="${pageContext.request.contextPath}/hoso?tab=password" class="user-dropdown-item">🔒 Bảo mật</a>
                            <div class="user-dropdown-divider"></div>
                            <a href="javascript:void(0)" onclick="doLogoutNow()" class="user-dropdown-item logout-item">🚪 Đăng xuất</a>
                        </div>
                    <% } %>
                </div>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="padding:8px 20px;font-size:0.82rem;">Đăng nhập</a>
            <% } %>
            <button class="menu-toggle" onclick="toggleMenu()"><span></span><span></span><span></span></button>
        </div>
    </div>
</header>

<!-- SYSTEM UPDATING MODAL -->
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
        <div class="chat-msg bot">Xin chào! Tôi là trợ lý ảo của Nha Khoa 5AE. Bạn cần hỗ trợ gì?</div>
    </div>
    <div class="chatbox-quick">
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
    window.CONTEXT_PATH = '${pageContext.request.contextPath}';
    window.HOME_URL = '<%= homeUrl %>';
</script>
<script src="${pageContext.request.contextPath}/assets/js/header.js"></script>
