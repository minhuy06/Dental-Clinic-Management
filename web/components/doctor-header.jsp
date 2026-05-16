<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.dentalclinic.model.TaiKhoan" %>
<%
    String staffDisplayName = "Bác sĩ";
    TaiKhoan u = (TaiKhoan) session.getAttribute("loggedInUser");
    if (u != null && u.getHoTen() != null && !u.getHoTen().trim().isEmpty()) {
        staffDisplayName = u.getHoTen().trim();
        if (!staffDisplayName.toLowerCase().startsWith("bs")) {
            staffDisplayName = "BS. " + staffDisplayName;
        }
    }
%>
<div class="header doctor-staff-header">
    <jsp:include page="clinic-logo.jsp" />
    <div class="user-info doctor-header-meta">
        <span class="doctor-header-time" id="currentDateTime"></span>
        <span class="staff-name"><%= staffDisplayName %></span>
    </div>
</div>
<script src="${pageContext.request.contextPath}/assets/js/doctor-header.js"></script>
