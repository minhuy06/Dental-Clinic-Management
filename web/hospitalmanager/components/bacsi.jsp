<%-- 
    Document   : bacsi
    Created on : Apr 21, 2026, 2:37:28 AM
    Author     : kinhm
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="page-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
    <h2 style="color: var(--primary-color);"><i class="fas fa-user-md"></i> Quản Lý Bác Sĩ Nha Khoa</h2>
    <div class="search-box" style="position: relative;">
        <input type="text" id="searchBacSi" placeholder="Tìm tên bác sĩ, chuyên khoa..." onkeyup="filterData('card-item', 'searchBacSi')" style="padding: 8px 15px 8px 35px; border-radius: 20px; border: 1px solid #ccc; width: 250px;">
        <i class="fas fa-search" style="position: absolute; left: 12px; top: 10px; color: #888;"></i>
    </div>
    <button class="btn btn-primary" style="padding: 8px 15px; background: var(--primary-color); color: white; border: none; border-radius: 5px; cursor: pointer;"><i class="fas fa-plus"></i> Thêm Bác sĩ</button>
</div>

<div class="grid-container" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(250px, 1fr)); gap: 20px;" id="danhSachBacSi">
    <c:if test="${empty listBS}">
        <tr><td colspan="5" style="text-align:center; padding: 20px;">Chưa có dữ liệu bác sĩ nào.</td></tr>
    </c:if>
    <c:forEach items="${listBS}" var="bacsi" varStatus="loop">
    <div class="card-item card" onclick="openDetailModal('${bacsi.taiKhoan.hoTen}', 'Chỉnh nha (Niềng răng)', '${bacsi.taiKhoan.soDienThoai}', '${bacsi.taiKhoan.trangThai}')" style="background: white; padding: 20px; border-radius: 10px; text-align: center; box-shadow: 0 2px 5px rgba(0,0,0,0.05); cursor: pointer; transition: 0.3s;">
        <div class="avatar-lg" style="width: 70px; height: 70px; background: #e9ecef; border-radius: 50%; margin: 0 auto 15px; display: flex; align-items: center; justify-content: center; font-size: 30px; color: var(--primary-color);">
            <i class="fas fa-user-md"></i>
        </div>
        <h4 class="name">${bacsi.taiKhoan.hoTen}</h4>
        <p class="spec" style="color: #666; font-size: 14px; margin: 5px 0;">Chỉnh nha (Niềng răng)</p>
        <span style="display: inline-block; padding: 3px 10px; background: #d1e7dd; color: #0f5132; border-radius: 12px; font-size: 12px;">${bacsi.taiKhoan.trangThai}</span>
    </div>
    </c:forEach>
</div>
