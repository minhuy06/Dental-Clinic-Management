<%-- 
    Document   : letan
    Created on : Apr 21, 2026, 2:37:44 AM
    Author     : kinhm
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="page-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
    <h2 style="color: var(--primary-color);"><i class="fas fa-user-nurse"></i> Danh Sách Lễ Tân</h2>
    <div class="search-box" style="position: relative;">
        <input type="text" id="searchLeTan" placeholder="Tìm tên, số điện thoại..." onkeyup="filterTable('tableLeTan', 'searchLeTan')" style="padding: 8px 15px 8px 35px; border-radius: 20px; border: 1px solid #ccc; width: 250px;">
        <i class="fas fa-search" style="position: absolute; left: 12px; top: 10px; color: #888;"></i>
    </div>
</div>

<div class="table-container" style="background: white; border-radius: 10px; padding: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.05);">
    <table id="tableLeTan" style="width: 100%; border-collapse: collapse; text-align: left;">
        <thead>
            <tr style="border-bottom: 2px solid #eee;">
                <th style="padding: 12px;">Mã LT</th>
                <th style="padding: 12px;">Họ và Tên</th>
                <th style="padding: 12px;">Số điện thoại</th>
                <th style="padding: 12px;">Ca làm việc</th>
                <th style="padding: 12px;">Thao tác</th>
            </tr>
        </thead>
        <tbody>
            <c:if test="${empty listLT}">
                <tr><td colspan="5" style="text-align:center; padding: 20px;">Chưa có dữ liệu lễ tân nào.</td></tr>
            </c:if>
            
            <c:forEach items="${listLT}" var="letan" varStatus="loop">
            <tr class="table-row" style="border-bottom: 1px solid #eee; transition: 0.2s;" onmouseover="this.style.background='#f8f9fa'" onmouseout="this.style.background='white'">
                <td style="padding: 12px;">${letan.leTanID}</td>
                <td style="padding: 12px; font-weight: 500;">${letan.taiKhoan.hoTen}</td>
                <td style="padding: 12px;">${letan.taiKhoan.soDienThoai}</td>
                <td style="padding: 12px;">Sáng (07:00 - 15:00)</td>
                <td style="padding: 12px;">
                    <button onclick="openDetailModal('${fn:escapeXml(letan.taiKhoan.hoTen)}','Lễ tân - Ca Sáng','${fn:escapeXml(letan.taiKhoan.soDienThoai)}','Đang làm việc')" style="padding: 5px 10px; background: #e9ecef; border: none; border-radius: 4px; cursor: pointer;"><i class="fas fa-eye text-primary"></i> Xem</button>
                </td>
            </tr>
            </c:forEach>
        </tbody>
    </table>
    
</div>
