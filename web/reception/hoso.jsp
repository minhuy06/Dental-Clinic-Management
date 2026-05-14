<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.dentalclinic.model.TaiKhoan" %>
<%
    // Lấy thông tin người dùng từ Session
    TaiKhoan loggedInUser = (TaiKhoan) session.getAttribute("loggedInUser");
    String activeTab = request.getParameter("tab");
    if (activeTab == null) activeTab = "info";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ Lễ tân - Nha Khoa 5AE</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:opsz,wght@14..32,300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- CSS dùng chung của hệ thống -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style_1.css">
</head>
<body id="receptionProfilePage">

    <!-- ==================== HEADER MÀU XANH CỦA LỄ TÂN ==================== -->
    <div class="header">
        <div class="logo">
            <div class="logo-icon">
                <i class="fas fa-tooth"></i>
            </div>
            <div class="logo-text">
                <h1>NHA KHOA 5AE</h1>
                <p>HỆ THỐNG QUẢN LÝ</p>
            </div>
        </div>
        <ul class="nav-menu">
            <li><a href="${pageContext.request.contextPath}/reception-dashboard">Lịch hẹn</a></li>
            <li><a href="benhnhan.jsp">Bệnh nhân</a></li>
            <li><a href="baocao.jsp">Báo cáo</a></li>
            <li><a href="cskh.jsp">CSKH</a></li>
        </ul>
        <div class="user-info dropdown-container">
            <div class="avatar" id="avatarBtn" onclick="toggleUserDropdown()" style="cursor: pointer;">
                <i class="fas fa-user" style="color: white;"></i>
            </div>
            <span class="staff-name" onclick="toggleUserDropdown()" style="cursor: pointer; display: flex; align-items: center; gap: 5px;">
                <%= loggedInUser != null ? loggedInUser.getHoTen() : "Lễ Tân" %> 
                <i class="fas fa-caret-down" style="font-size: 0.8rem; opacity: 0.8;"></i>
            </span>

            <!-- Menu Dropdown -->
            <div class="user-dropdown-menu" id="userDropdown">
                <a href="${pageContext.request.contextPath}/hoso?tab=info" class="dropdown-item">
                    <i class="fas fa-id-card"></i> Hồ sơ cá nhân
                </a>
                <a href="${pageContext.request.contextPath}/hoso?tab=password" class="dropdown-item">
                    <i class="fas fa-key"></i> Đổi mật khẩu
                </a>
                <div class="dropdown-divider"></div>
                <a href="javascript:void(0)" onclick="doLogoutNow()" class="dropdown-item text-danger">
                    <i class="fas fa-sign-out-alt"></i> Đăng xuất
                </a>
            </div>
        </div>
    </div>

    <!-- ==================== MAIN CONTENT: HỒ SƠ ==================== -->
    <main class="profile-page">
        <div class="container">
            <div class="profile-layout">
                <!-- SIDEBAR -->
                <div class="profile-sidebar">
                    <div class="sidebar-header">
                        <div class="sidebar-avatar" id="sidebarAvatar"><i class="fas fa-user-tie"></i></div>
                        <h3 id="sidebarName">Đang tải...</h3>
                        <p id="sidebarPhone"></p>
                        <span class="role-badge">Nhân viên Lễ tân</span>
                    </div>
                    <div class="sidebar-menu">
                        <div class="sidebar-menu-item <%= "info".equals(activeTab)?"active":"" %>" onclick="switchTab('info',this)">
                            <span class="menu-icon"><i class="fas fa-id-card"></i></span> Thông tin cá nhân
                        </div>
                        <div class="sidebar-menu-item <%= "password".equals(activeTab)?"active":"" %>" onclick="switchTab('password',this)">
                            <span class="menu-icon"><i class="fas fa-key"></i></span> Đổi mật khẩu
                        </div>
                        <div class="sidebar-menu-item logout" onclick="doLogoutNow()">
                            <span class="menu-icon"><i class="fas fa-sign-out-alt"></i></span> Đăng xuất
                        </div>
                    </div>
                </div>

                <!-- CONTENT -->
                <div class="profile-content">
                    <!-- TAB: THÔNG TIN CÁ NHÂN -->
                    <div class="tab-content <%= "info".equals(activeTab)?"active":"" %>" id="tab-info">
                        <div class="profile-content-header">
                            <h2>Thông tin cá nhân</h2>
                            <p>Quản lý thông tin hồ sơ nhân viên</p>
                        </div>
                        <div class="info-display">
                            <div class="info-row-display">
                                <div class="info-field">
                                    <span class="field-label">Họ và tên</span>
                                    <span class="field-value" id="dispName">—</span>
                                </div>
                                <div class="info-field">
                                    <span class="field-label">Số điện thoại</span>
                                    <span class="field-value" id="dispPhone">—</span>
                                </div>
                            </div>
                            <div class="info-row-display">
                                <div class="info-field">
                                    <span class="field-label">Ngày sinh</span>
                                    <span class="field-value" id="dispDob">—</span>
                                </div>
                                <div class="info-field">
                                    <span class="field-label">Giới tính</span>
                                    <span class="field-value" id="dispGender">—</span>
                                </div>
                            </div>
                        </div>
                        <div style="text-align:right; margin-top:25px;">
                            <button class="btn btn-primary" onclick="openEditModal()"><i class="fas fa-edit"></i> Chỉnh sửa thông tin</button>
                        </div>
                    </div>

                    <!-- TAB: ĐỔI MẬT KHẨU -->
                    <div class="tab-content <%= "password".equals(activeTab)?"active":"" %>" id="tab-password">
                        <div class="profile-content-header">
                            <h2>Đổi mật khẩu</h2>
                            <p>Cập nhật mật khẩu bảo mật tài khoản</p>
                        </div>
                        <form class="password-form" id="passwordForm" onsubmit="return changePassword(event)">
                            <div class="form-group" id="oldPassGroup">
                                <label>Mật khẩu cũ <span style="color:#e74c3c">*</span></label>
                                <input type="password" class="form-control" id="oldPassword" placeholder="Nhập mật khẩu hiện tại">
                                <div class="form-error">Vui lòng nhập mật khẩu cũ</div>
                            </div>
                            <div class="form-group" id="newPassGroup">
                                <label>Mật khẩu mới <span style="color:#e74c3c">*</span></label>
                                <input type="password" class="form-control" id="newPassword" placeholder="VD: Abc@1234">
                                <div class="password-hint">Tối thiểu 8 ký tự, bao gồm chữ hoa, thường, số và ký tự đặc biệt</div>
                                <div class="form-error">Mật khẩu không đạt yêu cầu</div>
                            </div>
                            <div class="form-group" id="confirmPassGroup">
                                <label>Nhập lại mật khẩu mới <span style="color:#e74c3c">*</span></label>
                                <input type="password" class="form-control" id="confirmNewPassword" placeholder="Nhập lại mật khẩu mới">
                                <div class="form-error">Mật khẩu nhập lại không khớp</div>
                            </div>
                            <div class="form-actions" style="margin-top: 30px;">
                                <button type="submit" class="btn btn-primary"><i class="fas fa-lock"></i> Xác nhận đổi mật khẩu</button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- MODAL: CHỈNH SỬA THÔNG TIN -->
    <div class="modal-overlay" id="editInfoModal" onclick="if(event.target===this) closeEditModal()">
        <div class="modal-box" style="max-width:550px;">
            <div class="modal-header">
                <h3><i class="fas fa-user-edit"></i> Chỉnh sửa thông tin</h3>
                <button class="modal-close" onclick="closeEditModal()">✕</button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>Họ và tên</label>
                    <input type="text" class="form-control" id="editName" disabled style="background:#f1f5f9; color:#94a3b8; cursor: not-allowed;">
                    <div class="password-hint"><i class="fas fa-info-circle"></i> Tên nhân viên do Quản trị viên hệ thống thiết lập.</div>
                </div>
                <div class="form-row-grid">
                    <div class="form-group">
                        <label>Số điện thoại</label>
                        <input type="tel" class="form-control" id="editPhone">
                    </div>
                    <div class="form-group">
                        <label>Ngày sinh</label>
                        <input type="date" class="form-control" id="editDob">
                    </div>
                </div>
                <div class="form-group" style="margin-bottom: 0;">
                    <label>Giới tính</label>
                    <select class="form-control" id="editGender">
                        <option value="Nam">Nam</option>
                        <option value="Nữ">Nữ</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeEditModal()">Hủy bỏ</button>
                <button class="btn btn-primary" id="btnSaveInfo" onclick="saveEditInfo()"><i class="fas fa-save"></i> Lưu thay đổi</button>
            </div>
        </div>
    </div>

    <!-- ==================== FOOTER ==================== -->
    <div class="footer">
        <p><i class="fas fa-tooth"></i> NHA KHOA 5AE - Chất lượng tạo niềm tin</p>
        <p>© 2024 Nha Khoa 5AE - Hệ thống quản lý lịch hẹn chuyên nghiệp</p>
    </div>

    <!-- ==================== STYLES (ĐÃ ĐƯỢC LÀM ĐẸP) ==================== -->
    <style>
        /* BASE & LAYOUT */
        body#receptionProfilePage { background-color: #f8fafc; font-family: 'Inter', sans-serif; }
        .profile-page { padding: 40px 0; min-height: calc(100vh - 160px); }
        .container { max-width: 1100px; margin: 0 auto; padding: 0 20px; }
        .profile-layout { display: flex; gap: 30px; align-items: flex-start; }

        /* NÚT BẤM CHUNG (BUTTONS) */
        .btn { display: inline-flex; align-items: center; justify-content: center; gap: 8px; padding: 12px 24px; border-radius: 10px; font-size: 0.95rem; font-weight: 600; cursor: pointer; transition: all 0.2s ease; border: none; }
        .btn-primary { background: #2563eb; color: white; box-shadow: 0 4px 12px rgba(37,99,235,0.2); }
        .btn-primary:hover:not(:disabled) { background: #1d4ed8; transform: translateY(-1px); box-shadow: 0 6px 16px rgba(37,99,235,0.3); }
        .btn-primary:disabled { opacity: 0.7; cursor: not-allowed; }
        .btn-outline { background: white; color: #475569; border: 1px solid #cbd5e1; }
        .btn-outline:hover { background: #f1f5f9; color: #0f172a; }

        /* SIDEBAR (CỘT TRÁI) */
        .profile-sidebar { flex: 0 0 280px; background: white; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); overflow: hidden; border: 1px solid #f1f5f9; }
        .sidebar-header { text-align: center; padding: 30px 20px 20px; border-bottom: 1px solid #f1f5f9; }
        .sidebar-avatar { width: 80px; height: 80px; margin: 0 auto 15px; border-radius: 50%; background: linear-gradient(135deg, #3b82f6, #6366f1); display: flex; align-items: center; justify-content: center; font-size: 2rem; color: white; box-shadow: 0 8px 16px rgba(59,130,246,0.25); }
        .sidebar-header h3 { margin: 0 0 5px 0; font-size: 1.2rem; color: #0f172a; font-weight: 700; }
        .sidebar-header p { margin: 0; color: #64748b; font-size: 0.9rem; font-weight: 500; }
        .role-badge { display: inline-block; margin-top: 12px; padding: 6px 14px; background: #e0e7ff; color: #4338ca; border-radius: 999px; font-size: 0.75rem; font-weight: 800; text-transform: uppercase; letter-spacing: 0.5px; }
        
        .sidebar-menu { padding: 15px; }
        .sidebar-menu-item { display: flex; align-items: center; padding: 12px 16px; margin-bottom: 5px; border-radius: 10px; color: #475569; font-weight: 600; cursor: pointer; transition: all 0.2s ease; }
        .sidebar-menu-item .menu-icon { width: 30px; font-size: 1.1rem; color: #94a3b8; transition: color 0.2s ease; }
        .sidebar-menu-item:hover { background: #f8fafc; color: #0f172a; }
        .sidebar-menu-item.active { background: #eff6ff; color: #2563eb; }
        .sidebar-menu-item.active .menu-icon { color: #2563eb; }
        .sidebar-menu-item.logout { margin-top: 15px; color: #ef4444; border-top: 1px dashed #e2e8f0; border-radius: 0; padding-top: 20px; }
        .sidebar-menu-item.logout .menu-icon { color: #f87171; }
        .sidebar-menu-item.logout:hover { background: #fef2f2; border-radius: 10px; }

        /* CONTENT (CỘT PHẢI) */
        .profile-content { flex: 1; background: white; border-radius: 16px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); border: 1px solid #f1f5f9; min-height: 400px; padding: 30px 40px; }
        .tab-content { display: none; animation: fadeIn 0.3s ease; }
        .tab-content.active { display: block; }
        @keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
        
        .profile-content-header { margin-bottom: 30px; border-bottom: 1px solid #f1f5f9; padding-bottom: 20px; }
        .profile-content-header h2 { margin: 0 0 8px 0; font-size: 1.5rem; color: #0f172a; font-weight: 800; }
        .profile-content-header p { margin: 0; color: #64748b; font-size: 0.95rem; }

        /* HIỂN THỊ THÔNG TIN */
        .info-display { background: #f8fafc; border-radius: 12px; border: 1px solid #e2e8f0; overflow: hidden; }
        .info-row-display { display: grid; grid-template-columns: 1fr 1fr; border-bottom: 1px solid #e2e8f0; }
        .info-row-display:last-child { border-bottom: none; }
        .info-field { padding: 20px 24px; display: flex; flex-direction: column; gap: 6px; border-right: 1px solid #e2e8f0; }
        .info-field:last-child { border-right: none; }
        .field-label { font-size: 0.8rem; text-transform: uppercase; font-weight: 700; color: #94a3b8; letter-spacing: 0.5px; }
        .field-value { font-size: 1.05rem; font-weight: 600; color: #1e293b; }

        /* FORMS & INPUTS */
        .password-form { max-width: 500px; }
        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; margin-bottom: 8px; font-size: 0.9rem; font-weight: 600; color: #334155; }
        .form-control { width: 100%; padding: 12px 16px; font-size: 0.95rem; color: #0f172a; background: #fff; border: 1px solid #cbd5e1; border-radius: 10px; transition: all 0.2s ease; outline: none; font-family: inherit; }
        .form-control:focus { border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59,130,246,0.15); }
        .password-hint { font-size: 0.8rem; color: #64748b; margin-top: 6px; }
        
        /* Error states */
        .form-error { display: none; font-size: 0.85rem; color: #ef4444; margin-top: 6px; font-weight: 500; }
        .form-group.error .form-control { border-color: #ef4444; background: #fef2f2; }
        .form-group.error .form-control:focus { box-shadow: 0 0 0 3px rgba(239,68,68,0.15); }
        .form-group.error .form-error { display: block; }

        /* MODAL (POPUP) */
        .modal-overlay { position: fixed; inset: 0; background: rgba(15,23,42,0.6); backdrop-filter: blur(4px); display: flex; align-items: center; justify-content: center; opacity: 0; pointer-events: none; transition: all 0.3s ease; z-index: 99999; }
        .modal-overlay.show { opacity: 1; pointer-events: auto; }
        .modal-box { background: white; border-radius: 16px; width: 90%; max-width: 550px; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1), 0 10px 10px -5px rgba(0,0,0,0.04); transform: scale(0.95) translateY(20px); transition: all 0.3s cubic-bezier(0.16, 1, 0.3, 1); }
        .modal-overlay.show .modal-box { transform: scale(1) translateY(0); }
        .modal-header { padding: 20px 24px; border-bottom: 1px solid #f1f5f9; display: flex; align-items: center; justify-content: space-between; }
        .modal-header h3 { margin: 0; font-size: 1.2rem; color: #0f172a; display: flex; align-items: center; gap: 10px; }
        .modal-header h3 i { color: #2563eb; }
        .modal-close { background: none; border: none; font-size: 1.2rem; color: #94a3b8; cursor: pointer; padding: 4px; border-radius: 6px; transition: all 0.2s; }
        .modal-close:hover { background: #f1f5f9; color: #ef4444; }
        .modal-body { padding: 24px; }
        .form-row-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        .modal-footer { padding: 16px 24px; background: #f8fafc; border-top: 1px solid #f1f5f9; border-radius: 0 0 16px 16px; display: flex; justify-content: flex-end; gap: 12px; }

        /* CSS HEADER DROPDOWN MENU */
        .dropdown-container { position: relative; }
        .user-dropdown-menu { display: none; position: absolute; top: 130%; right: 0; background: #ffffff; border-radius: 12px; box-shadow: 0 10px 25px rgba(0,0,0,0.15); min-width: 220px; z-index: 9999; overflow: hidden; border: 1px solid var(--border-color, #e5e7eb); animation: fadeInDown 0.2s ease forwards; }
        .user-dropdown-menu.show { display: block; }
        .dropdown-item { display: flex; align-items: center; padding: 14px 20px; color: #4b5563; text-decoration: none; font-size: 0.9rem; font-weight: 600; transition: all 0.2s; }
        .dropdown-item i { margin-right: 12px; width: 18px; text-align: center; font-size: 1.1rem; color: #9ca3af; transition: color 0.2s; }
        .dropdown-item:hover { background: #f3f4f6; color: var(--primary-color, #2563eb); }
        .dropdown-item:hover i { color: var(--primary-color, #2563eb); }
        .dropdown-divider { height: 1px; background: #e5e7eb; margin: 4px 0; }
        .dropdown-item.text-danger { color: #dc2626; }
        .dropdown-item.text-danger:hover { background: #fef2f2; }
        .dropdown-item.text-danger i { color: #ef4444; }
        @keyframes fadeInDown { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }

        /* RESPONSIVE */
        @media (max-width: 768px) {
            .profile-layout { flex-direction: column; }
            .profile-sidebar { flex: auto; width: 100%; }
            .profile-content { padding: 20px; }
            .info-row-display { grid-template-columns: 1fr; border-bottom: none; }
            .info-field { border-right: none; border-bottom: 1px solid #e2e8f0; padding: 16px; }
            .info-field:last-child { border-bottom: none; }
            .form-row-grid { grid-template-columns: 1fr; gap: 0; }
        }
    </style>

    <!-- ==================== SCRIPTS ==================== -->
    <script>
        // ======= LOGIC DROPDOWN HEADER =======
        function toggleUserDropdown() {
            document.getElementById("userDropdown").classList.toggle("show");
        }
        window.addEventListener('click', function(e) {
            if (!e.target.closest('.dropdown-container')) {
                var dropdown = document.getElementById("userDropdown");
                if (dropdown && dropdown.classList.contains('show')) dropdown.classList.remove('show');
            }
        });
        function doLogoutNow() {
            if (confirm('Bạn có chắc chắn muốn đăng xuất khỏi hệ thống?')) {
                window.location.href = '${pageContext.request.contextPath}/index.jsp?logout=true';
            }
        }

        // ======= LOGIC XỬ LÝ HỒ SƠ =======
        const API_BASE = '${pageContext.request.contextPath}/api/hoso';
        let currentUser = {};

        (function initData() {
            let userJsonStr = '${not empty hosoUserJson ? hosoUserJson : "{}"}';
            try {
                currentUser = JSON.parse(userJsonStr);
                renderUserInfo();
            } catch(e) {
                console.error("Lỗi parse JSON User", e);
            }
        })();

        function renderUserInfo() {
            let u = currentUser;
            if(document.getElementById('sidebarName')) document.getElementById('sidebarName').textContent = u.name || '—';
            if(document.getElementById('sidebarPhone')) document.getElementById('sidebarPhone').textContent = u.phone || '';
            if(document.getElementById('dispName')) document.getElementById('dispName').textContent = u.name || '—';
            if(document.getElementById('dispPhone')) document.getElementById('dispPhone').textContent = u.phone || '—';
            if(document.getElementById('dispGender')) document.getElementById('dispGender').textContent = u.gender || '—';
            if(document.getElementById('dispDob') && u.dob) {
                let parts = u.dob.split('-');
                document.getElementById('dispDob').textContent = parts.length === 3 ? `${parts[2]}/${parts[1]}/${parts[0]}` : u.dob;
            }
            if(document.getElementById('editName')) document.getElementById('editName').value = u.name || '';
            if(document.getElementById('editPhone')) document.getElementById('editPhone').value = u.phone || '';
            if(document.getElementById('editDob')) document.getElementById('editDob').value = u.dob || '';
            if(document.getElementById('editGender') && u.gender) document.getElementById('editGender').value = u.gender;
        }

        function switchTab(tabId, el) {
            document.querySelectorAll('.tab-content').forEach(c => c.classList.remove('active'));
            document.querySelectorAll('.sidebar-menu-item').forEach(i => i.classList.remove('active'));
            document.getElementById('tab-' + tabId).classList.add('active');
            el.classList.add('active');
        }

        function openEditModal() { document.getElementById('editInfoModal').classList.add('show'); }
        function closeEditModal() { document.getElementById('editInfoModal').classList.remove('show'); }

        async function saveEditInfo() {
            let phone = document.getElementById('editPhone').value.trim();
            let dob = document.getElementById('editDob').value;
            let gender = document.getElementById('editGender').value;

            if (!phone) return showToast('❌ Vui lòng nhập số điện thoại', 'error');

            let btn = document.getElementById('btnSaveInfo');
            btn.disabled = true; btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang lưu...';

            try {
                let res = await fetch(API_BASE + '/update-info', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ phone: phone, dob: dob, gender: gender })
                });
                let data = await res.json();
                
                if (data.success) {
                    currentUser.phone = phone; currentUser.dob = dob; currentUser.gender = gender;
                    renderUserInfo(); closeEditModal();
                    showToast('✅ Cập nhật thông tin thành công!', 'success');
                } else {
                    showToast('❌ ' + (data.message || 'Cập nhật thất bại'), 'error');
                }
            } catch (error) {
                showToast('❌ Lỗi kết nối đến máy chủ', 'error');
            } finally {
                btn.disabled = false; btn.innerHTML = '<i class="fas fa-save"></i> Lưu thay đổi';
            }
        }

        async function changePassword(e) {
            e.preventDefault();
            let ok = true, oldPw = document.getElementById('oldPassword').value, newPw = document.getElementById('newPassword').value, confirmPw = document.getElementById('confirmNewPassword').value;

            if(!oldPw) { document.getElementById('oldPassGroup').classList.add('error'); ok = false; } else document.getElementById('oldPassGroup').classList.remove('error');
            if(!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(newPw)) { document.getElementById('newPassGroup').classList.add('error'); ok = false; } else document.getElementById('newPassGroup').classList.remove('error');
            if(confirmPw !== newPw) { document.getElementById('confirmPassGroup').classList.add('error'); ok = false; } else document.getElementById('confirmPassGroup').classList.remove('error');

            if(!ok) return false;

            let btn = document.querySelector('#passwordForm .btn-primary');
            btn.disabled = true; btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';

            try {
                let res = await fetch(API_BASE + '/change-password', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ oldPassword: oldPw, newPassword: newPw })
                });
                let data = await res.json();

                if (data.success) {
                    document.getElementById('passwordForm').reset();
                    showToast('✅ Đổi mật khẩu thành công!', 'success');
                } else {
                    if (data.code === 'WRONG_PASSWORD') document.getElementById('oldPassGroup').classList.add('error');
                    showToast('❌ ' + (data.message || 'Đổi mật khẩu thất bại'), 'error');
                }
            } catch (error) {
                showToast('❌ Lỗi kết nối đến máy chủ', 'error');
            } finally {
                btn.disabled = false; btn.innerHTML = '<i class="fas fa-lock"></i> Xác nhận đổi mật khẩu';
            }
        }

        function showToast(msg, type) {
            let t = document.getElementById('hosoToast');
            if (!t) {
                t = document.createElement('div'); t.id = 'hosoToast';
                t.style.cssText = 'position:fixed;bottom:30px;right:30px;z-index:999999;padding:14px 24px;border-radius:12px;font-size:1rem;font-weight:600;color:#fff;max-width:350px;box-shadow:0 10px 25px rgba(0,0,0,.2);transition:all .3s cubic-bezier(0.16, 1, 0.3, 1);opacity:0;transform:translateY(20px);pointer-events:none; display:flex; align-items:center; gap:10px;';
                document.body.appendChild(t);
            }
            
            // Thêm Icon vào Toast cho đẹp
            let icon = type === 'error' ? '<i class="fas fa-exclamation-circle" style="font-size:1.2rem;"></i>' : '<i class="fas fa-check-circle" style="font-size:1.2rem;"></i>';
            t.innerHTML = icon + ' <span>' + msg.replace('✅ ', '').replace('❌ ', '') + '</span>';
            
            t.style.background = type === 'error' ? 'linear-gradient(135deg, #ef4444, #dc2626)' : 'linear-gradient(135deg, #10b981, #059669)';
            t.style.opacity = '1';
            t.style.transform = 'translateY(0)';
            
            clearTimeout(t._timer); 
            t._timer = setTimeout(() => {
                t.style.opacity = '0';
                t.style.transform = 'translateY(20px)';
            }, 3000);
        }

        document.getElementById('oldPassword').addEventListener('input', function() { this.parentElement.classList.remove('error'); });
        document.getElementById('newPassword').addEventListener('input', function() { this.parentElement.classList.remove('error'); });
        document.getElementById('confirmNewPassword').addEventListener('input', function() { this.parentElement.classList.remove('error'); });
    </script>
</body>
</html>