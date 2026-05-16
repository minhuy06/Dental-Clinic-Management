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
<script>
(function() {
    function updateDoctorHeaderDateTime() {
        var el = document.getElementById('currentDateTime');
        if (!el) return;
        var now = new Date();
        var dateStr = now.toLocaleDateString('vi-VN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
        var timeStr = now.toLocaleTimeString('vi-VN');
        el.innerHTML = '<i class="fa-regular fa-calendar"></i> ' + dateStr + ' | ' + timeStr;
    }
    updateDoctorHeaderDateTime();
    setInterval(updateDoctorHeaderDateTime, 1000);
})();
</script>
