<%-- 
    Document   : hoso
    Created on : Apr 22, 2026, 12:56:17 AM
    Author     : AHieu
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nha Khoa 5AE - Trang Cá Nhân</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:opsz,wght@14..32,300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style_1.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
    <style>
        /* ==================== CSS RIÊNG CHO TRANG CÁ NHÂN ==================== */
        .profile-container {
            display: flex;
            gap: 30px;
            max-width: 1200px;
            margin: 30px auto;
            background: var(--bg-white);
            border-radius: 20px;
            overflow: hidden;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
            border: 1px solid var(--border-color);
        }

        /* Sidebar */
        .profile-sidebar {
            width: 320px;
            background: linear-gradient(135deg, #1e40af 0%, #1e3a8a 100%);
            padding: 30px 20px;
            text-align: center;
            color: white;
        }

        .profile-avatar {
            width: 120px;
            height: 120px;
            background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            border: 4px solid white;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
        }

        .profile-avatar i {
            font-size: 3rem;
            color: white;
        }

        .profile-name {
            font-size: 1.3rem;
            font-weight: 700;
            margin-bottom: 5px;
        }

        .profile-role {
            display: inline-block;
            padding: 4px 12px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 20px;
            font-size: 0.7rem;
            font-weight: 600;
            margin-bottom: 20px;
        }

        .profile-stats {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin: 20px 0;
            padding: 15px 0;
            border-top: 1px solid rgba(255, 255, 255, 0.2);
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }

        .profile-stat {
            text-align: center;
        }

        .profile-stat .number {
            font-size: 1.3rem;
            font-weight: 800;
        }

        .profile-stat .label {
            font-size: 0.7rem;
            opacity: 0.8;
        }

        .edit-profile-btn {
            width: 100%;
            padding: 10px;
            background: white;
            color: var(--primary-color);
            border: none;
            border-radius: 10px;
            font-weight: 600;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            transition: all 0.3s;
            margin-top: 10px;
        }

        .edit-profile-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }

        .logout-btn {
            width: 100%;
            padding: 10px;
            background: rgba(255, 255, 255, 0.1);
            color: white;
            border: 1px solid rgba(255, 255, 255, 0.3);
            border-radius: 10px;
            font-weight: 600;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            transition: all 0.3s;
            margin-top: 10px;
        }

        .logout-btn:hover {
            background: #ef4444;
            border-color: #ef4444;
        }

        /* Main Content */
        .profile-info {
            flex: 1;
            padding: 30px;
        }

        .info-section {
            margin-bottom: 30px;
        }

        .info-section h4 {
            font-size: 1rem;
            color: var(--text-main);
            margin-bottom: 20px;
            padding-bottom: 8px;
            border-bottom: 2px solid var(--primary-color);
            display: inline-block;
        }

        .info-row {
            display: flex;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 1px solid var(--border-color);
        }

        .info-label {
            width: 130px;
            font-size: 0.85rem;
            color: var(--text-sub);
            font-weight: 500;
        }

        .info-value {
            flex: 1;
            font-size: 0.9rem;
            color: var(--text-main);
            font-weight: 500;
        }

        /* Activity List */
        .activity-list {
            list-style: none;
        }

        .activity-item {
            display: flex;
            align-items: center;
            gap: 15px;
            padding: 12px 0;
            border-bottom: 1px solid var(--border-color);
        }

        .activity-icon {
            width: 40px;
            height: 40px;
            background: #eff6ff;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: var(--primary-color);
        }

        .activity-content {
            flex: 1;
        }

        .activity-title {
            font-size: 0.9rem;
            font-weight: 500;
            color: var(--text-main);
        }

        .activity-time {
            font-size: 0.7rem;
            color: var(--text-sub);
            margin-top: 3px;
        }

        /* Modal chỉnh sửa */
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            backdrop-filter: blur(5px);
            z-index: 2000;
            justify-content: center;
            align-items: center;
        }

        .modal-content {
            background: white;
            border-radius: 20px;
            width: 90%;
            max-width: 500px;
            animation: modalSlide 0.3s;
        }

        @keyframes modalSlide {
            from {
                transform: translateY(-30px);
                opacity: 0;
            }
            to {
                transform: translateY(0);
                opacity: 1;
            }
        }

        .modal-header {
            padding: 20px 25px;
            border-bottom: 1px solid var(--border-color);
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .modal-header h3 {
            color: var(--text-main);
            font-size: 1.2rem;
        }

        .close {
            font-size: 28px;
            cursor: pointer;
            color: var(--text-sub);
            transition: transform 0.3s;
        }

        .close:hover {
            transform: rotate(90deg);
            color: var(--danger);
        }

        .modal-body {
            padding: 25px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: var(--text-main);
            font-size: 0.8rem;
            font-weight: 600;
        }

        .form-group input {
            width: 100%;
            padding: 10px 15px;
            border: 1px solid var(--border-color);
            border-radius: 10px;
            font-size: 0.9rem;
        }

        .form-group input:focus {
            outline: none;
            border-color: var(--primary-color);
        }

        .modal-footer {
            padding: 20px 25px;
            border-top: 1px solid var(--border-color);
            display: flex;
            justify-content: flex-end;
            gap: 12px;
        }

        .btn-cancel, .btn-save {
            padding: 10px 25px;
            border-radius: 10px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }

        .btn-cancel {
            background: var(--bg-gray);
            border: 1px solid var(--border-color);
            color: var(--text-sub);
        }

        .btn-save {
            background: var(--primary-color);
            border: none;
            color: white;
        }

        .btn-cancel:hover, .btn-save:hover {
            transform: translateY(-2px);
        }

        .toast-message {
            position: fixed;
            bottom: 30px;
            right: 30px;
            background: var(--success);
            color: white;
            padding: 12px 20px;
            border-radius: 12px;
            font-weight: 600;
            z-index: 3000;
            animation: slideInRight 0.3s;
        }

        @keyframes slideInRight {
            from {
                transform: translateX(100%);
                opacity: 0;
            }
            to {
                transform: translateX(0);
                opacity: 1;
            }
        }

        @media (max-width: 768px) {
            .profile-container {
                flex-direction: column;
                margin: 15px;
            }
            .profile-sidebar {
                width: 100%;
            }
            .info-row {
                flex-direction: column;
            }
            .info-label {
                width: 100%;
                margin-bottom: 5px;
            }
        }
    </style>
</head>
<body>


    <div class="profile-container">
        <!-- Sidebar -->
        <div class="profile-sidebar">
            <div class="profile-avatar">
                <i class="fas fa-user-tie"></i>
            </div>
            <div class="profile-name" id="profileName">Lễ Tân 01</div>
            <div class="profile-role" id="profileRole">Lễ tân</div>
            
            <div class="profile-stats">
                <div class="profile-stat">
                    <div class="number" id="statAppointments">0</div>
                    <div class="label">Lịch hẹn</div>
                </div>
                <div class="profile-stat">
                    <div class="number" id="statCustomers">0</div>
                    <div class="label">Khách hàng</div>
                </div>
                <div class="profile-stat">
                    <div class="number" id="statRevenue">0</div>
                    <div class="label">Doanh thu</div>
                </div>
            </div>
            
            <button class="edit-profile-btn" id="editProfileBtn">
                <i class="fas fa-edit"></i> Chỉnh sửa hồ sơ
            </button>
            <button class="logout-btn" id="logoutBtn">
                <i class="fas fa-sign-out-alt"></i> Đăng xuất
            </button>
        </div>

        <!-- Main Content -->
        <div class="profile-info">
            <div class="info-section">
                <h4>Thông tin tài khoản</h4>
                <div class="info-row">
                    <div class="info-label">Họ tên:</div>
                    <div class="info-value" id="infoFullName">Lễ Tân 01</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Email:</div>
                    <div class="info-value" id="infoEmail">letan01@5ae-dental.com</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Số điện thoại:</div>
                    <div class="info-value" id="infoPhone">0901 123 456</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Chức vụ:</div>
                    <div class="info-value" id="infoPosition">Lễ tân</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Ngày tham gia:</div>
                    <div class="info-value" id="infoJoinDate">01/01/2024</div>
                </div>
                <div class="info-row">
                    <div class="info-label">Địa chỉ:</div>
                    <div class="info-value" id="infoAddress">123 Đường Lê Lợi, Quận 1, TP.HCM</div>
                </div>
            </div>

            <div class="info-section">
                <h4>Hoạt động gần đây</h4>
                <ul class="activity-list" id="activityList">
                    <li class="activity-item">
                        <div class="activity-icon"><i class="fas fa-calendar-plus"></i></div>
                        <div class="activity-content">
                            <div class="activity-title">Đã tạo lịch hẹn mới</div>
                            <div class="activity-time">Hôm nay, 09:30</div>
                        </div>
                    </li>
                    <li class="activity-item">
                        <div class="activity-icon"><i class="fas fa-credit-card"></i></div>
                        <div class="activity-content">
                            <div class="activity-title">Đã thanh toán hóa đơn #HD-20240001</div>
                            <div class="activity-time">Hôm qua, 15:20</div>
                        </div>
                    </li>
                    <li class="activity-item">
                        <div class="activity-icon"><i class="fas fa-user-plus"></i></div>
                        <div class="activity-content">
                            <div class="activity-title">Thêm bệnh nhân mới: Nguyễn Văn An</div>
                            <div class="activity-time">Hôm qua, 10:15</div>
                        </div>
                    </li>
                    <li class="activity-item">
                        <div class="activity-icon"><i class="fas fa-edit"></i></div>
                        <div class="activity-content">
                            <div class="activity-title">Cập nhật thông tin bệnh nhân</div>
                            <div class="activity-time">2 ngày trước</div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <!-- Modal chỉnh sửa hồ sơ -->
    <div id="editProfileModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3><i class="fas fa-user-edit"></i> Chỉnh sửa hồ sơ</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <form id="editProfileForm">
                    <div class="form-group">
                        <label>Họ tên</label>
                        <input type="text" id="editFullName" value="Lễ Tân 01">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="editEmail" value="letan01@5ae-dental.com">
                    </div>
                    <div class="form-group">
                        <label>Số điện thoại</label>
                        <input type="tel" id="editPhone" value="0901 123 456">
                    </div>
                    <div class="form-group">
                        <label>Địa chỉ</label>
                        <input type="text" id="editAddress" value="123 Đường Lê Lợi, Quận 1, TP.HCM">
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu mới</label>
                        <input type="password" id="editPassword" placeholder="Để trống nếu không đổi">
                    </div>
                    <div class="form-group">
                        <label>Xác nhận mật khẩu</label>
                        <input type="password" id="editConfirmPassword" placeholder="Nhập lại mật khẩu mới">
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel">Hủy</button>
                <button class="btn-save" id="saveProfileBtn">Lưu thay đổi</button>
            </div>
        </div>
    </div>



    <script>
        // Dữ liệu mẫu
        let userData = {
            fullName: "Lễ Tân 01",
            email: "letan01@5ae-dental.com",
            phone: "0901 123 456",
            position: "Lễ tân",
            joinDate: "01/01/2024",
            address: "123 Đường Lê Lợi, Quận 1, TP.HCM"
        };

        // Thống kê mẫu
        let stats = {
            appointments: 128,
            customers: 256,
            revenue: 125000000
        };

        // Hiển thị thông tin
        function loadProfileData() {
            document.getElementById('profileName').innerText = userData.fullName;
            document.getElementById('infoFullName').innerText = userData.fullName;
            document.getElementById('infoEmail').innerText = userData.email;
            document.getElementById('infoPhone').innerText = userData.phone;
            document.getElementById('infoPosition').innerText = userData.position;
            document.getElementById('infoJoinDate').innerText = userData.joinDate;
            document.getElementById('infoAddress').innerText = userData.address;
            
            document.getElementById('statAppointments').innerText = stats.appointments;
            document.getElementById('statCustomers').innerText = stats.customers;
            document.getElementById('statRevenue').innerText = stats.revenue.toLocaleString() + 'đ';
        }

        function formatPrice(price) {
            return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
        }

        function showToast(message) {
            let toast = document.createElement('div');
            toast.className = 'toast-message';
            toast.innerHTML = `<i class="fas fa-check-circle"></i> ${message}`;
            document.body.appendChild(toast);
            setTimeout(() => toast.remove(), 3000);
        }

        // Mở modal chỉnh sửa
        function openEditModal() {
            document.getElementById('editFullName').value = userData.fullName;
            document.getElementById('editEmail').value = userData.email;
            document.getElementById('editPhone').value = userData.phone;
            document.getElementById('editAddress').value = userData.address;
            document.getElementById('editPassword').value = '';
            document.getElementById('editConfirmPassword').value = '';
            document.getElementById('editProfileModal').style.display = 'flex';
        }

        // Lưu thay đổi
        function saveProfile() {
            let fullName = document.getElementById('editFullName').value.trim();
            let email = document.getElementById('editEmail').value.trim();
            let phone = document.getElementById('editPhone').value.trim();
            let address = document.getElementById('editAddress').value.trim();
            let password = document.getElementById('editPassword').value;
            let confirmPassword = document.getElementById('editConfirmPassword').value;
            
            if (password && password !== confirmPassword) {
                alert('Mật khẩu xác nhận không khớp!');
                return;
            }
            
            userData.fullName = fullName;
            userData.email = email;
            userData.phone = phone;
            userData.address = address;
            
            loadProfileData();
            closeModal();
            showToast('Cập nhật hồ sơ thành công!');
        }

        function closeModal() {
            document.getElementById('editProfileModal').style.display = 'none';
        }

        function logout() {
            if (confirm('Bạn có chắc chắn muốn đăng xuất?')) {
                showToast('Đã đăng xuất!');
                setTimeout(() => {
                    window.location.href = 'index.jsp';
                }, 1000);
            }
        }

        // Sự kiện
        document.addEventListener('DOMContentLoaded', () => {
            loadProfileData();
            
            document.getElementById('editProfileBtn').onclick = openEditModal;
            document.getElementById('logoutBtn').onclick = logout;
            document.querySelector('.close').onclick = closeModal;
            document.querySelector('.btn-cancel').onclick = closeModal;
            document.getElementById('saveProfileBtn').onclick = saveProfile;
            
            window.onclick = (e) => {
                if (e.target === document.getElementById('editProfileModal')) closeModal();
            };
        });
    </script>
</body>
</html>