<%-- 
    Document   : hoso
    Created on : Apr 21, 2026, 2:38:14 AM
    Author     : kinhm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="page-header" style="margin-bottom: 20px;">
    <h2 style="color: var(--primary-color);"><i class="fas fa-id-card"></i> Hồ Sơ Tài Khoản</h2>
</div>

<div class="profile-container" style="display: flex; gap: 30px;">
    <div class="profile-avatar" style="background: white; padding: 30px; border-radius: 10px; text-align: center; width: 300px; box-shadow: 0 2px 5px rgba(0,0,0,0.05);">
        <div style="width: 120px; height: 120px; background: var(--primary-color); color: white; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 50px; margin: 0 auto 20px;">
            <i class="fas fa-user-tie"></i>
        </div>
        <h3 style="margin-bottom: 5px;">${accountLogan.hoTen}</h3>
        <p style="color: #666; font-size: 14px; margin-bottom: 15px;">${accountLogan.vaiTro}</p>
        <button style="padding: 8px 20px; background: #f8f9fa; border: 1px solid #ddd; border-radius: 20px; cursor: pointer;">Đổi ảnh đại diện</button>
    </div>

    <div class="profile-info" style="background: white; padding: 30px; border-radius: 10px; flex: 1; box-shadow: 0 2px 5px rgba(0,0,0,0.05);">
        <h4 style="margin-bottom: 20px; border-bottom: 1px solid #eee; padding-bottom: 10px;">Thông tin chi tiết</h4>
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
            <div>
                <label style="display: block; font-size: 13px; color: #666; margin-bottom: 5px;">Họ và Tên</label>
                <input type="text" value="${accountLogan.hoTen}" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;" disabled>
            </div>
            <div>
                <label style="display: block; font-size: 13px; color: #666; margin-bottom: 5px;">Số điện thoại (Tài khoản)</label>
                <input type="text" value="${accountLogan.soDienThoai}" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;" disabled>
            </div>
            <div>
                <label style="display: block; font-size: 13px; color: #666; margin-bottom: 5px;">Email liên hệ</label>
                <input type="email" value="manager@nhakhoa5ae.com" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;">
            </div>
            <div>
                <label style="display: block; font-size: 13px; color: #666; margin-bottom: 5px;">Cơ sở làm việc</label>
                <input type="text" value="Chi nhánh Trung tâm" style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 5px;" disabled>
            </div>
        </div>
        <button style="margin-top: 20px; padding: 10px 25px; background: var(--primary-color); color: white; border: none; border-radius: 5px; cursor: pointer;">Cập nhật thông tin</button>
    </div>
</div>
