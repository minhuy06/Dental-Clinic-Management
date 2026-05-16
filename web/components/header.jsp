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
    String workspaceLabel = "Trang làm việc";
    boolean isPatient = true;
    boolean isStaff = false;
    if (loggedInUser != null) {
        String role = loggedInUser.getVaiTro();
        workspaceUrl = RoleNavHelper.getWorkspaceUrl(ctx, role);
        workspaceLabel = RoleNavHelper.getWorkspaceLabel(role);
        isPatient = RoleNavHelper.isPatient(role);
        isStaff = !isPatient;
    }
    boolean isIndex = currentPage.endsWith("index.jsp")
            || currentPage.endsWith(ctx + "/")
            || currentPage.endsWith("/Dental_Clinic_Management")
            || currentPage.endsWith("/Dental_Clinic_Management/");
    boolean isInforPage = currentPage.contains("/Infor/");
%>

<header class="header" id="header" data-context-path="${pageContext.request.contextPath}" data-home-url="<%= homeUrl %>">
    <div class="container">
        <a href="<%= homeUrl %>" class="logo">
            <div class="logo-icon">🦷</div>
            <div class="logo-text">Nha Khoa <span>5AE</span></div>
        </a>
        <nav class="nav-menu" id="navMenu">
            <a href="<%= homeUrl %>" class="<%= isIndex ? "active" : "" %>">Trang chủ</a>
            <a href="<%= scheduleUrl %>" data-section="datlich">Đặt lịch</a>
            <a href="<%= serviceUrl %>" data-section="dichvu">Dịch vụ</a>
            <a href="<%= doctorUrl %>" data-section="bacsi">Bác sĩ</a>
            <div class="nav-mobile-actions">
                <% if (loggedInUser != null) { %>
                    <% if (isStaff && isIndex) { %>
                        <button type="button" class="btn btn-primary" style="width:100%;" onclick="toggleUserDropdown()">👤 <%= loggedInUser.getHoTen() != null && !loggedInUser.getHoTen().trim().isEmpty() ? loggedInUser.getHoTen() : "Người dùng" %></button>
                    <% } else { %>
                        <a href="<%= isStaff ? workspaceUrl : (ctx + "/hoso") %>" class="btn btn-primary" style="width:100%;">👤 <%= loggedInUser.getHoTen() != null && !loggedInUser.getHoTen().trim().isEmpty() ? loggedInUser.getHoTen() : "Người dùng" %></a>
                    <% } %>

                <% } else { %>
                    <a href="${pageContext.request.contextPath}/account/login.jsp" class="btn btn-outline" style="width:100%;">Đăng nhập</a>
                <% } %>
            </div>
        </nav>
        <div class="header-actions">
            <% if (loggedInUser != null) { %>
                <div class="user-dropdown-wrapper">
                    <% if (isStaff && isIndex) { %>
                        <div class="user-avatar-btn" onclick="toggleUserDropdown()">
                            <div class="user-avatar-circle">👤</div>
                            <span class="user-avatar-name"><%= loggedInUser.getHoTen() != null && !loggedInUser.getHoTen().trim().isEmpty() ? loggedInUser.getHoTen() : "Người dùng" %></span>
                            <span class="user-arrow">▼</span>
                        </div>
                        <div class="user-dropdown" id="userDropdown">
                            <a href="<%= workspaceUrl %>" class="user-dropdown-item">🏥 <%= workspaceLabel %></a>
                            <div class="user-dropdown-divider"></div>
                            <a href="javascript:void(0)" onclick="AppNotify.doLogoutWithConfirm(); return false;" class="user-dropdown-item logout-item">🚪 Đăng xuất</a>
                        </div>
                    <% } else if (isStaff) { %>
                        <a href="<%= workspaceUrl %>" class="user-avatar-btn user-avatar-btn--link" title="<%= workspaceLabel %>">
                            <div class="user-avatar-circle">👤</div>
                            <span class="user-avatar-name"><%= loggedInUser.getHoTen() != null && !loggedInUser.getHoTen().trim().isEmpty() ? loggedInUser.getHoTen() : "Người dùng" %></span>
                        </a>
                    <% } else { %>
                        <div class="user-avatar-btn" onclick="toggleUserDropdown()">
                            <div class="user-avatar-circle">👤</div>
                            <span class="user-avatar-name"><%= loggedInUser.getHoTen() != null && !loggedInUser.getHoTen().trim().isEmpty() ? loggedInUser.getHoTen() : "Người dùng" %></span>
                            <span class="user-arrow">▼</span>
                        </div>
                        <div class="user-dropdown" id="userDropdown">
                            <a href="${pageContext.request.contextPath}/hoso?tab=info" class="user-dropdown-item">📋 Hồ sơ cá nhân</a>
                            <a href="${pageContext.request.contextPath}/hoso?tab=history" class="user-dropdown-item">📅 Quản lý lịch hẹn</a>
                            <a href="${pageContext.request.contextPath}/hoso?tab=password" class="user-dropdown-item">🔒 Bảo mật</a>
                            <div class="user-dropdown-divider"></div>
                            <a href="javascript:void(0)" onclick="AppNotify.doLogoutWithConfirm(); return false;" class="user-dropdown-item logout-item">🚪 Đăng xuất</a>
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

<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app-notify.css">
<script src="${pageContext.request.contextPath}/assets/js/bootstrap-helper.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/app-notify.js?v=20260518b"></script>
<script src="${pageContext.request.contextPath}/assets/js/header.js"></script>
