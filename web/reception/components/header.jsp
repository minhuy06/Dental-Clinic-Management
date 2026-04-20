<%-- 
    Document   : header
    Created on : Apr 21, 2026, 1:33:19 AM
    Author     : kinhm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="header">
    <div class="logo">
        <div class="logo-icon">
                    <i class="fas fa-tooth"></i>
                </div>
                <div class="logo-text">
                    <h1>NHA KHOA 5AE</h1>
                    <p>HỆ THỐNG BÁO CÁO DOANH THU</p>
                </div>
            </div>
            <ul class="nav-menu">
                <li><a href="index.jsp">Lịch hẹn</a></li>
                <li><a href="benhnhan.jsp">Bệnh nhân</a></li>
                <li><a href="baocao.jsp" class="active">Báo cáo</a></li>
                <li><a href="cskh.jsp">CSKH</a></li>
            </ul>
            <div class="user-info">
                <i class="fas fa-bell" style="color: #bfdbfe; cursor: pointer;"></i>

                <div class="user-profile" style="display: flex; align-items: center; gap: 10px; cursor: pointer;">
                    <div class="avatar">
                        <i class="fas fa-user" style="color: white;"></i>
                    </div>

                    <div class="user-details" style="color: white;">
                        <div style="font-weight: 600; font-size: 15px;">${accountLogan.hoTen}</div>
                        <div style="font-size: 12px; opacity: 0.8;">${accountLogan.vaiTro}</div>
                    </div>
                </div>
            </div>
        </div>
