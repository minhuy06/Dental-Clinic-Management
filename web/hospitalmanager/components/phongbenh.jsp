<%-- 
    Document   : phongbenh
    Created on : Apr 21, 2026, 2:13:07 AM
    Author     : kinhm
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="page-header" style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
    <h2 style="color: var(--primary-color);"><i class="fas fa-clinic-medical"></i> Quản Lý Phòng Khám</h2>
    
    <div class="filter-group" style="display: flex; gap: 10px; align-items: center;">
        <label style="font-size: 14px; color: #666; font-weight: 500;">Trạng thái:</label>
        <select style="padding: 8px 15px; border-radius: 8px; border: 1px solid #ddd; outline: none;">
            <option value="all">Tất cả phòng</option>
            <option value="ready">Sẵn sàng (Trống)</option>
            <option value="in-use">Đang khám</option>
            <option value="maintenance">Đang bảo trì</option>
        </select>
        <button class="btn btn-primary" style="padding: 8px 15px; background: var(--primary-color); color: white; border: none; border-radius: 8px; cursor: pointer; margin-left: 10px;">
            <i class="fas fa-plus"></i> Thêm Phòng
        </button>
    </div>
</div>

<div class="grid-container" style="display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px;">
    
    <div class="room-card card" style="background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.05); border-top: 4px solid #dc3545; transition: 0.3s;">
        <div style="padding: 15px 20px; border-bottom: 1px solid #f0f0f0; display: flex; justify-content: space-between; align-items: center;">
            <h3 style="margin: 0; display: flex; align-items: center; gap: 10px;">
                <i class="fas fa-door-closed" style="color: #dc3545;"></i> Phòng P01
            </h3>
            <span style="background: #f8d7da; color: #842029; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 600;">Đang khám</span>
        </div>
        <div style="padding: 20px;">
            <p style="margin-bottom: 10px; font-size: 14px;"><i class="fas fa-user-md" style="width: 25px; color: #666;"></i> <strong>BS. Trần Văn A</strong></p>
            <p style="margin-bottom: 20px; font-size: 14px;"><i class="fas fa-user-injured" style="width: 25px; color: #666;"></i> BN. Nguyễn Trọng B (Nhổ răng)</p>
            <div style="display: flex; gap: 10px;">
                <button style="flex: 1; padding: 8px; background: #f8f9fa; border: 1px solid #ddd; border-radius: 6px; cursor: pointer; color: #555;">Chi tiết</button>
                <button style="flex: 1; padding: 8px; background: #dc3545; color: white; border: none; border-radius: 6px; cursor: pointer;">Kết thúc ca</button>
            </div>
        </div>
    </div>

    <div class="room-card card" style="background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.05); border-top: 4px solid #198754; transition: 0.3s;">
        <div style="padding: 15px 20px; border-bottom: 1px solid #f0f0f0; display: flex; justify-content: space-between; align-items: center;">
            <h3 style="margin: 0; display: flex; align-items: center; gap: 10px;">
                <i class="fas fa-door-open" style="color: #198754;"></i> Phòng P02
            </h3>
            <span style="background: #d1e7dd; color: #0f5132; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 600;">Sẵn sàng</span>
        </div>
        <div style="padding: 20px;">
            <p style="margin-bottom: 10px; font-size: 14px; color: #aaa;"><i class="fas fa-user-md" style="width: 25px;"></i> Chưa phân bổ bác sĩ</p>
            <p style="margin-bottom: 20px; font-size: 14px; color: #aaa;"><i class="fas fa-user-injured" style="width: 25px;"></i> Trống</p>
            <div style="display: flex; gap: 10px;">
                <button style="flex: 1; padding: 8px; background: #f8f9fa; border: 1px solid #ddd; border-radius: 6px; cursor: pointer; color: #555;">Chi tiết</button>
                <button style="flex: 1; padding: 8px; background: #198754; color: white; border: none; border-radius: 6px; cursor: pointer;">Chỉ định bệnh nhân</button>
            </div>
        </div>
    </div>
    
    <div class="room-card card" style="background: white; border-radius: 10px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.05); border-top: 4px solid #fd7e14; transition: 0.3s;">
        <div style="padding: 15px 20px; border-bottom: 1px solid #f0f0f0; display: flex; justify-content: space-between; align-items: center;">
            <h3 style="margin: 0; display: flex; align-items: center; gap: 10px;">
                <i class="fas fa-tools" style="color: #fd7e14;"></i> Phòng VIP 1
            </h3>
            <span style="background: #fff3cd; color: #856404; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: 600;">Bảo trì thiết bị</span>
        </div>
        <div style="padding: 20px;">
            <p style="margin-bottom: 10px; font-size: 14px; color: #fd7e14;"><i class="fas fa-wrench" style="width: 25px;"></i> Đang sửa máy nội nha</p>
            <p style="margin-bottom: 20px; font-size: 14px; color: #aaa;"><i class="fas fa-clock" style="width: 25px;"></i> Dự kiến hoàn thành: 16:00</p>
            <div style="display: flex; gap: 10px;">
                <button style="width: 100%; padding: 8px; background: #f8f9fa; border: 1px solid #ddd; border-radius: 6px; cursor: pointer; color: #555;">Cập nhật tiến độ</button>
            </div>
        </div>
    </div>

</div>
