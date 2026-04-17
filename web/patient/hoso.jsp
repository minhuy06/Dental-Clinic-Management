<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân - Nha Khoa Kvone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/hoso.css">
</head>
<body>

    <jsp:include page="../components/header.jsp" />

    <main class="profile-page">
        <div class="container">
            <div class="profile-layout">

                <!-- SIDEBAR -->
                <div class="profile-sidebar">
                    <div class="sidebar-header">
                        <div class="sidebar-avatar">👤</div>
                        <h3>Nguyễn Văn An</h3>
                        <p>0901000001</p>
                    </div>
                    <div class="sidebar-menu">
                        <div class="sidebar-menu-item active" onclick="switchTab('info', this)">
                            <span class="menu-icon">📋</span> Thông tin cá nhân
                        </div>
                        <div class="sidebar-menu-item" onclick="switchTab('history', this)">
                            <span class="menu-icon">📅</span> Quản lý lịch hẹn
                        </div>
                        <div class="sidebar-menu-item" onclick="switchTab('password', this)">
                            <span class="menu-icon">🔒</span> Đổi mật khẩu
                        </div>
                        <div class="sidebar-menu-item logout" onclick="confirmLogout()">
                            <span class="menu-icon">🚪</span> Đăng xuất
                        </div>
                    </div>
                </div>

                <!-- MAIN CONTENT -->
                <div class="profile-content">

                    <!-- TAB 1: THONG TIN CA NHAN -->
                    <div class="tab-content active" id="tab-info">
                        <div class="profile-content-header">
                            <h2>Thông tin cá nhân</h2>
                            <p>Xem và cập nhật thông tin của bạn</p>
                        </div>

                        <form class="info-form" id="infoForm" onsubmit="return saveInfo(event)">
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Họ và tên <span style="color:#e74c3c">*</span></label>
                                    <input type="text" class="form-control" id="infoName" value="Nguyễn Văn An">
                                </div>
                                <div class="form-group">
                                    <label>Số điện thoại</label>
                                    <input type="text" class="form-control" value="0901000001" disabled>
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label>Email</label>
                                    <input type="email" class="form-control" id="infoEmail" value="nguyenvanan@gmail.com" placeholder="Nhập email liên hệ">
                                </div>
                                <div class="form-group">
                                    <label>Ngày sinh</label>
                                    <input type="date" class="form-control" id="infoDob" value="1990-05-15">
                                </div>
                            </div>

                            <div class="form-row">
                                <div class="form-group">
                                    <label>Giới tính</label>
                                    <select class="form-control" id="infoGender">
                                        <option value="1" selected>Nam</option>
                                        <option value="0">Nữ</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Nhóm máu</label>
                                    <select class="form-control" id="infoBlood">
                                        <option value="A" selected>A</option>
                                        <option value="B">B</option>
                                        <option value="AB">AB</option>
                                        <option value="O">O</option>
                                        <option value="A+">A+</option>
                                        <option value="A-">A-</option>
                                        <option value="B+">B+</option>
                                        <option value="B-">B-</option>
                                        <option value="AB+">AB+</option>
                                        <option value="AB-">AB-</option>
                                        <option value="O+">O+</option>
                                        <option value="O-">O-</option>
                                    </select>
                                </div>
                            </div>

                            <div class="form-actions">
                                <button type="submit" class="btn btn-primary">💾 Lưu thay đổi</button>
                            </div>
                        </form>
                    </div>

                    <!-- TAB 2: QUAN LY LICH HEN -->
                    <div class="tab-content" id="tab-history">
                        <div class="profile-content-header">
                            <h2>Quản lý lịch hẹn</h2>
                            <p>Xem, thanh toán và quản lý các lịch hẹn của bạn</p>
                        </div>

                        <div class="history-toolbar">
                            <div class="history-filter">
                                <button class="filter-chip active" onclick="filterHistory('all', this)">Tất cả</button>
                                <button class="filter-chip" onclick="filterHistory('pending', this)">Chờ xác nhận</button>
                                <button class="filter-chip" onclick="filterHistory('confirmed', this)">Đã xác nhận</button>
                                <button class="filter-chip" onclick="filterHistory('paid', this)">Đã thanh toán</button>
                            </div>
                        </div>

                        <div class="history-table-wrapper">
                            <table class="history-table" id="historyTable">
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Ngày khám</th>
                                        <th>Giờ</th>
                                        <th>Bác sĩ</th>
                                        <th>Trạng thái</th>
                                        <th style="text-align:center;">Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr data-status="paid" data-id="1">
                                        <td class="stt-cell">1</td>
                                        <td>01/03/2024</td>
                                        <td>08:00</td>
                                        <td>BS. Nguyễn Hải</td>
                                        <td><span class="status-badge paid">Đã thanh toán</span></td>
                                        <td>
                                            <div class="action-buttons">
                                                <button class="btn-action btn-view" onclick="showDetail(1)">Xem chi tiết</button>
                                                <button class="btn-action btn-cancel" disabled>Hủy lịch</button>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr data-status="confirmed" data-id="2">
                                        <td class="stt-cell">2</td>
                                        <td>15/04/2024</td>
                                        <td>09:30</td>
                                        <td>BS. Trần Tâm</td>
                                        <td><span class="status-badge confirmed">Đã xác nhận</span></td>
                                        <td>
                                            <div class="action-buttons">
                                                <button class="btn-action btn-view" onclick="showDetail(2)">Xem chi tiết</button>
                                                <button class="btn-action btn-cancel" onclick="confirmCancel(2)">Hủy lịch</button>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr data-status="pending" data-id="3">
                                        <td class="stt-cell">3</td>
                                        <td>20/05/2024</td>
                                        <td>14:00</td>
                                        <td>BS. Lê Quang</td>
                                        <td><span class="status-badge pending">Chờ xác nhận</span></td>
                                        <td>
                                            <div class="action-buttons">
                                                <button class="btn-action btn-view" onclick="showDetail(3)">Xem chi tiết</button>
                                                <button class="btn-action btn-cancel" onclick="confirmCancel(3)">Hủy lịch</button>
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- TAB 3: DOI MAT KHAU -->
                    <div class="tab-content" id="tab-password">
                        <div class="profile-content-header">
                            <h2>Đổi mật khẩu</h2>
                            <p>Cập nhật mật khẩu để bảo mật tài khoản</p>
                        </div>

                        <form class="password-form" id="passwordForm" onsubmit="return changePassword(event)">
                            <div class="form-group" id="oldPassGroup">
                                <label>Mật khẩu cũ <span style="color:#e74c3c">*</span></label>
                                <input type="password" class="form-control" id="oldPassword" placeholder="Nhập mật khẩu hiện tại">
                                <div class="form-error">Vui lòng nhập mật khẩu cũ</div>
                            </div>

                            <div class="form-group" id="newPassGroup">
                                <label>Mật khẩu mới <span style="color:#e74c3c">*</span></label>
                                <input type="password" class="form-control" id="newPassword" placeholder="VD: Abc@1234" oninput="checkStrength(this.value)">
                                <div class="password-strength">
                                    <div class="password-strength-bar" id="strengthBar"></div>
                                </div>
                                <div class="password-hint">Tối thiểu 8 ký tự, gồm chữ hoa, chữ thường, số và ký tự đặc biệt (!@#$%^&*)</div>
                                <div class="form-error">Mật khẩu không đạt yêu cầu</div>
                            </div>

                            <div class="form-group" id="confirmPassGroup">
                                <label>Nhập lại mật khẩu mới <span style="color:#e74c3c">*</span></label>
                                <input type="password" class="form-control" id="confirmNewPassword" placeholder="Nhập lại mật khẩu mới">
                                <div class="form-error">Mật khẩu nhập lại không khớp</div>
                            </div>

                            <div class="form-actions">
                                <button type="submit" class="btn btn-primary">🔐 Đổi mật khẩu</button>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
        </div>
    </main>

    <!-- MODAL CHI TIET PHIEU KHAM -->
    <div class="modal-overlay" id="detailModal" onclick="closeModalOnOverlay(event)">
        <div class="modal-box receipt-modal">
            <div class="modal-body" id="modalBody">
                <!-- Noi dung dong -->
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeModal()">Đóng</button>
            </div>
        </div>
    </div>

    <!-- MODAL XAC NHAN HUY -->
    <div class="modal-overlay" id="cancelModal" onclick="closeCancelOnOverlay(event)">
        <div class="modal-box confirm-box">
            <div class="modal-header" style="background:#e74c3c;">
                <h3>⚠️ Xác nhận hủy lịch</h3>
                <button class="modal-close" onclick="closeCancelModal()">✕</button>
            </div>
            <div class="modal-body">
                <span class="confirm-icon">🗓️</span>
                <div class="confirm-title">Bạn chắc chắn muốn hủy lịch hẹn này?</div>
                <div class="confirm-message">Lịch hẹn đã hủy không thể khôi phục. Vui lòng đặt lại nếu cần.</div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeCancelModal()">Không</button>
                <button class="btn" onclick="doCancelAppointment()" style="background:#e74c3c;color:white;">Có, hủy lịch</button>
            </div>
        </div>
    </div>
    <!-- MODAL XAC NHAN DANG XUAT -->
    <div class="modal-overlay" id="logoutModal" onclick="closeLogoutOnOverlay(event)">
        <div class="modal-box confirm-box">
            <div class="modal-header" style="background:linear-gradient(135deg,#e67e22 0%,#d35400 100%);">
                <h3>⚠️ Xác nhận đăng xuất</h3>
                <button class="modal-close" onclick="closeLogoutModal()">✕</button>
            </div>
            <div class="modal-body">
                <span class="confirm-icon">🚪</span>
                <div class="confirm-title">Bạn có chắc muốn đăng xuất?</div>
                <div class="confirm-message">Bạn sẽ cần đăng nhập lại để tiếp tục sử dụng các chức năng.</div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeLogoutModal()">Hủy</button>
                <button class="btn btn-accent" onclick="doLogout()">Đăng xuất</button>
            </div>
        </div>
    </div>

    <jsp:include page="../components/footer.jsp" />

    <script>
        // === DU LIEU MAU PHIEU KHAM ===
        var appointmentDetails = {
            1: {
                code: 'PK00001', status: 'paid',
                date: '01/03/2024', time: '08:00', room: 'Phòng 101',
                doctorName: 'BS. Nguyễn Hải', doctorSpec: 'Tổng quát', doctorDegree: 'CKI',
                doctorAvatar: 'https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=100&h=100&fit=crop',
                reason: 'Khám định kỳ 6 tháng',
                symptoms: 'Cảm thấy ê buốt khi uống nước lạnh, có mảng bám vàng ở răng cửa.',
                diagnosis: 'Vôi răng nhiều tích tụ ở mặt trong răng hàm dưới. Cần cạo vôi tổng quát.',
                note: 'Khuyến cáo súc miệng nước muối 2 lần/ngày và tái khám sau 6 tháng.',
                services: [
                    {name: 'Khám tổng quát', position: '-', price: 100000},
                    {name: 'Cạo vôi răng', position: 'Toàn hàm', price: 200000}
                ],
                total: 300000
            },
            2: {
                code: 'PK00002', status: 'unpaid',
                date: '15/04/2024', time: '09:30', room: 'Phòng 203',
                doctorName: 'BS. Trần Tâm', doctorSpec: 'Chỉnh nha', doctorDegree: 'Thạc sĩ',
                doctorAvatar: 'https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=100&h=100&fit=crop',
                reason: 'Tư vấn niềng răng',
                symptoms: 'Răng cửa mọc lệch, khớp cắn không đều khi nhai.',
                diagnosis: 'Răng mọc lệch lạc nhẹ. Có thể chỉnh nha bằng mắc cài kim loại hoặc Invisalign.',
                note: 'Đã tư vấn 2 phương án, khách hàng đang xem xét.',
                services: [
                    {name: 'Khám tư vấn chỉnh nha', position: '-', price: 500000},
                    {name: 'Chụp X-quang toàn cảnh', position: 'Toàn hàm', price: 300000}
                ],
                total: 800000
            },
            3: {
                code: 'PK00003', status: 'pending',
                date: '20/05/2024', time: '14:00', room: 'Chưa phân phòng',
                doctorName: 'BS. Lê Quang', doctorSpec: 'Phục hình răng', doctorDegree: 'Tiến sĩ',
                doctorAvatar: 'https://images.unsplash.com/photo-1622253692010-333f2da6031d?w=100&h=100&fit=crop',
                reason: 'Bọc răng sứ',
                symptoms: 'Chưa khám',
                diagnosis: 'Chưa có chẩn đoán - đang chờ xác nhận lịch hẹn.',
                note: 'Lịch hẹn chờ phòng khám xác nhận.',
                services: [],
                total: 0
            }
        };

        var currentDetailId = null;
        var currentCancelId = null;
        // === MODAL DANG XUAT ===
        function confirmLogout() {
            document.getElementById('logoutModal').classList.add('show');
        }
        function closeLogoutModal() {
            document.getElementById('logoutModal').classList.remove('show');
        }
        function closeLogoutOnOverlay(e) {
            if (e.target.id === 'logoutModal') closeLogoutModal();
        }
        function doLogout() {
            window.location.href = '${pageContext.request.contextPath}/account/logout.jsp';
        }

        // === CHUYEN TAB ===
        function switchTab(tabName, element) {
            document.querySelectorAll('.tab-content').forEach(function(tab) {
                tab.classList.remove('active');
            });
            document.querySelectorAll('.sidebar-menu-item').forEach(function(item) {
                item.classList.remove('active');
            });
            document.getElementById('tab-' + tabName).classList.add('active');
            element.classList.add('active');
        }

        // === LUU THONG TIN ===
        function saveInfo(event) {
            event.preventDefault();
            var name = document.getElementById('infoName').value.trim();
            var email = document.getElementById('infoEmail').value.trim();
            if (!name) { alert('Vui lòng nhập họ và tên'); return false; }
            if (email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                alert('Email không hợp lệ'); return false;
            }
            alert('✅ Cập nhật thông tin thành công!');
            return false;
        }

        // === FILTER LICH HEN ===
        function filterHistory(status, btn) {
            document.querySelectorAll('.filter-chip').forEach(function(c) { c.classList.remove('active'); });
            btn.classList.add('active');
            var rows = document.querySelectorAll('#historyTable tbody tr');
            var stt = 0;
            rows.forEach(function(row) {
                var match = status === 'all' || row.getAttribute('data-status') === status;
                if (match) {
                    stt++;
                    row.style.display = '';
                    row.querySelector('.stt-cell').textContent = stt;
                } else {
                    row.style.display = 'none';
                }
            });
        }

        // === MODAL CHI TIET ===
        function showDetail(id) {
            currentDetailId = id;
            var d = appointmentDetails[id];
            if (!d) return;

            var statusClass = d.status;
            var statusText = {paid:'Đã thanh toán', unpaid:'Chưa thanh toán', pending:'Chờ xác nhận'}[d.status] || '';

            var html = '';

            // Hero
            html += '<div class="receipt-hero">';
            html += '<span class="receipt-status-pill ' + statusClass + '">' + statusText + '</span>';
            html += '<span class="receipt-code">Mã phiếu: ' + d.code + '</span>';
            html += '<h3>Chi tiết phiếu khám</h3>';
            html += '<div class="receipt-subtitle">Nha Khoa Kvone - 48 Cao Thắng, Đà Nẵng</div>';
            html += '</div>';

            html += '<div class="receipt-content">';

            // Ngay gio phong
            html += '<div class="receipt-card">';
            html += '<div class="receipt-card-title"><span class="title-icon">📅</span> Thông tin lịch hẹn</div>';
            html += '<div class="info-row">';
            html += '<div class="info-cell"><div class="cell-icon">🗓️</div><div class="cell-text"><small>Ngày khám</small><strong>' + d.date + '</strong></div></div>';
            html += '<div class="info-cell"><div class="cell-icon">🕐</div><div class="cell-text"><small>Giờ khám</small><strong>' + d.time + '</strong></div></div>';
            html += '<div class="info-cell"><div class="cell-icon">🏥</div><div class="cell-text"><small>Phòng khám</small><strong>' + d.room + '</strong></div></div>';
            html += '<div class="info-cell"><div class="cell-icon">📋</div><div class="cell-text"><small>Lý do khám</small><strong>' + d.reason + '</strong></div></div>';
            html += '</div>';
            html += '</div>';

            // Bac si
            html += '<div class="receipt-card">';
            html += '<div class="receipt-card-title"><span class="title-icon">👨‍⚕️</span> Bác sĩ phụ trách</div>';
            html += '<div class="doctor-card-mini">';
            html += '<div class="doctor-avatar-mini"><img src="' + d.doctorAvatar + '" alt="Doctor"></div>';
            html += '<div class="doctor-details">';
            html += '<h4>' + d.doctorName + '</h4>';
            html += '<p>Trình độ: ' + d.doctorDegree + '</p>';
            html += '<span class="specialty-tag">' + d.doctorSpec + '</span>';
            html += '</div>';
            html += '</div>';
            html += '</div>';

            // Trieu chung - Chan doan - Ghi chu
            html += '<div class="receipt-card">';
            html += '<div class="receipt-card-title"><span class="title-icon">🩺</span> Thông tin khám</div>';
            html += '<div class="medical-item"><div class="medical-label">Triệu chứng</div><div class="medical-text">' + d.symptoms + '</div></div>';
            html += '<div class="medical-item"><div class="medical-label">Chẩn đoán</div><div class="medical-text diagnosis">' + d.diagnosis + '</div></div>';
            html += '<div class="medical-item"><div class="medical-label">Ghi chú của bác sĩ</div><div class="medical-text">' + d.note + '</div></div>';
            html += '</div>';

            // Dich vu - Gia
            html += '<div class="receipt-card">';
            html += '<div class="receipt-card-title"><span class="title-icon">💎</span> Dịch vụ & Chi phí</div>';
            if (d.services.length > 0) {
                html += '<div class="services-list">';
                d.services.forEach(function(s) {
                    html += '<div class="service-row">';
                    html += '<div class="service-info"><div class="service-name">' + s.name + '</div>';
                    if (s.position && s.position !== '-') {
                        html += '<div class="service-position">Vị trí: ' + s.position + '</div>';
                    }
                    html += '</div>';
                    html += '<div class="service-price">' + s.price.toLocaleString('vi-VN') + ' đ</div>';
                    html += '</div>';
                });
                html += '</div>';
            } else {
                html += '<div class="medical-empty">Chưa có dịch vụ nào được thực hiện</div>';
            }
            html += '</div>';

            // Tong tien
            if (d.total > 0) {
                html += '<div class="receipt-total-box">';
                html += '<div class="total-label">Tổng chi phí</div>';
                html += '<div class="total-value">' + d.total.toLocaleString('vi-VN') + ' đ</div>';
                html += '</div>';
            }

            html += '</div>'; // end receipt-content

            document.getElementById('modalBody').innerHTML = html;
            document.getElementById('detailModal').classList.add('show');
        }

        function closeModal() {
            document.getElementById('detailModal').classList.remove('show');
        }

        function closeModalOnOverlay(e) {
            if (e.target.id === 'detailModal') closeModal();
        }

        
        // === MODAL HUY LICH ===
        function confirmCancel(id) {
            currentCancelId = id;
            document.getElementById('cancelModal').classList.add('show');
        }

        function closeCancelModal() {
            document.getElementById('cancelModal').classList.remove('show');
        }

        function closeCancelOnOverlay(e) {
            if (e.target.id === 'cancelModal') closeCancelModal();
        }

        function doCancelAppointment() {
            alert('✅ Đã hủy lịch hẹn thành công!');
            var row = document.querySelector('tr[data-id="' + currentCancelId + '"]');
            if (row) row.remove(); 
            var stt = 1;
            document.querySelectorAll('#historyTable tbody tr:not([style*="none"])').forEach(function(r){
                r.querySelector('.stt-cell').textContent = stt++;
            });
            closeCancelModal();
        }

        // === DOI MAT KHAU ===
        function checkStrength(password) {
            var bar = document.getElementById('strengthBar');
            var hasLower = /[a-z]/.test(password);
            var hasUpper = /[A-Z]/.test(password);
            var hasNumber = /[0-9]/.test(password);
            var hasSpecial = /[!@#$%^&*]/.test(password);
            var isLong = password.length >= 8;
            var score = 0;
            if (hasLower) score++;
            if (hasUpper) score++;
            if (hasNumber) score++;
            if (hasSpecial) score++;
            if (isLong) score++;
            bar.className = 'password-strength-bar';
            if (score <= 2) bar.classList.add('weak');
            else if (score <= 4) bar.classList.add('medium');
            else bar.classList.add('strong');
        }

        function changePassword(event) {
            event.preventDefault();
            var isValid = true;
            var oldPass = document.getElementById('oldPassword').value;
            var newPass = document.getElementById('newPassword').value;
            var confirmPass = document.getElementById('confirmNewPassword').value;

            if (!oldPass) {
                document.getElementById('oldPassGroup').classList.add('error');
                isValid = false;
            } else {
                document.getElementById('oldPassGroup').classList.remove('error');
            }

            var strongRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[A-Za-z0-9!@#$%^&*]{8,}$/;
            if (!strongRegex.test(newPass)) {
                document.getElementById('newPassGroup').classList.add('error');
                isValid = false;
            } else {
                document.getElementById('newPassGroup').classList.remove('error');
            }

            if (confirmPass !== newPass || !confirmPass) {
                document.getElementById('confirmPassGroup').classList.add('error');
                isValid = false;
            } else {
                document.getElementById('confirmPassGroup').classList.remove('error');
            }

            if (isValid) {
                alert('✅ Đổi mật khẩu thành công!');
                document.getElementById('passwordForm').reset();
                document.getElementById('strengthBar').className = 'password-strength-bar';
            }
            return false;
        }

        document.getElementById('oldPassword').addEventListener('input', function() {
            document.getElementById('oldPassGroup').classList.remove('error');
        });
        document.getElementById('newPassword').addEventListener('input', function() {
            document.getElementById('newPassGroup').classList.remove('error');
        });
        document.getElementById('confirmNewPassword').addEventListener('input', function() {
            document.getElementById('confirmPassGroup').classList.remove('error');
        });
    </script>

</body>
</html>
