<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    String activeTab = request.getParameter("tab");
    if (activeTab == null) activeTab = "info";
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân - Nha Khoa Kvone</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/datlich.css">
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
                        <div class="sidebar-menu-item <%= "info".equals(activeTab)?"active":"" %>" onclick="switchTab('info',this)"><span class="menu-icon">📋</span> Thông tin cá nhân</div>
                        <div class="sidebar-menu-item <%= "history".equals(activeTab)?"active":"" %>" onclick="switchTab('history',this)"><span class="menu-icon">📅</span> Quản lý lịch hẹn</div>
                        <div class="sidebar-menu-item <%= "password".equals(activeTab)?"active":"" %>" onclick="switchTab('password',this)"><span class="menu-icon">🔒</span> Đổi mật khẩu</div>
                        <div class="sidebar-menu-item logout" onclick="if(confirm('Bạn có chắc muốn đăng xuất?'))window.location.href=window.CONTEXT_PATH+'/index.jsp?logout=true'"><span class="menu-icon">🚪</span> Đăng xuất</div>
                    </div>
                </div>

                <!-- CONTENT -->
                <div class="profile-content">
                    <!-- TAB INFO -->
                    <div class="tab-content <%= "info".equals(activeTab)?"active":"" %>" id="tab-info">
                        <div class="profile-content-header"><h2>Thông tin cá nhân</h2><p>Thông tin tài khoản của bạn</p></div>
                        <div class="info-display">
                            <div class="info-row-display">
                                <div class="info-field"><span class="field-label">Họ và tên</span><span class="field-value" id="dispName">Nguyễn Văn An</span></div>
                                <div class="info-field"><span class="field-label">Số điện thoại</span><span class="field-value" id="dispPhone">0901000001</span></div>
                            </div>
                            <div class="info-row-display">
                                <div class="info-field"><span class="field-label">Email</span><span class="field-value" id="dispEmail">nguyenvanan@gmail.com</span></div>
                                <div class="info-field"><span class="field-label">Ngày sinh</span><span class="field-value" id="dispDob">15/05/1990</span></div>
                            </div>
                            <div class="info-row-display">
                                <div class="info-field"><span class="field-label">Giới tính</span><span class="field-value" id="dispGender">Nam</span></div>
                            </div>
                        </div>
                        <div style="text-align:right;margin-top:20px;">
                            <button class="btn btn-primary" onclick="openEditModal()">✏️ Chỉnh sửa</button>
                        </div>
                    </div>

                    <!-- TAB HISTORY -->
                    <div class="tab-content <%= "history".equals(activeTab)?"active":"" %>" id="tab-history">
                        <div class="profile-content-header"><h2>Quản lý lịch hẹn</h2><p>Xem và quản lý các lịch hẹn</p></div>
                        <div class="history-toolbar">
                            <div class="history-filter">
                                <button class="filter-chip active" onclick="filterHistory('all',this)">Tất cả</button>
                                <button class="filter-chip" onclick="filterHistory('pending',this)">Chờ xác nhận</button>
                                <button class="filter-chip" onclick="filterHistory('confirmed',this)">Đã xác nhận</button>
                                <button class="filter-chip" onclick="filterHistory('paid',this)">Đã khám</button>
                            </div>
                        </div>
                        <div class="history-table-wrapper">
                            <table class="history-table" id="historyTable">
                                <thead><tr><th>STT</th><th>Ngày</th><th>Giờ</th><th>Dịch vụ</th><th>Trạng thái</th><th style="text-align:center">Thao tác</th></tr></thead>
                                <tbody>
                                    <tr data-status="pending" data-id="3"><td class="stt-cell">1</td><td>20/05/2024</td><td>14:00</td><td>Bọc sứ Zirconia x2, Tẩy trắng</td><td><span class="status-badge pending">Chờ xác nhận</span></td><td><div class="action-buttons"><button class="btn-action btn-view" onclick="showDetail(3)">Xem</button><button class="btn-action btn-edit" onclick="openEditAppointment(3)">Sửa</button><button class="btn-action btn-cancel" onclick="confirmCancel(3)">Hủy</button></div></td></tr>
                                    <tr data-status="confirmed" data-id="2"><td class="stt-cell">2</td><td>15/04/2024</td><td>09:30</td><td>Tư vấn chỉnh nha, Chụp X-quang</td><td><span class="status-badge confirmed">Đã xác nhận</span></td><td><div class="action-buttons"><button class="btn-action btn-view" onclick="showDetail(2)">Xem</button><button class="btn-action btn-cancel" onclick="confirmCancelConfirmed(2)">Hủy</button></div></td></tr>
                                    <tr data-status="paid" data-id="1"><td class="stt-cell">3</td><td>01/03/2024</td><td>08:00</td><td>Khám tổng quát, Cạo vôi</td><td><span class="status-badge paid">Đã khám</span></td><td><div class="action-buttons"><button class="btn-action btn-view" onclick="showDetail(1)">Xem</button></div></td></tr>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- TAB PASSWORD -->
                    <div class="tab-content <%= "password".equals(activeTab)?"active":"" %>" id="tab-password">
                        <div class="profile-content-header"><h2>Đổi mật khẩu</h2><p>Cập nhật mật khẩu bảo mật</p></div>
                        <form class="password-form" id="passwordForm" onsubmit="return changePassword(event)">
                            <div class="form-group" id="oldPassGroup"><label>Mật khẩu cũ <span style="color:#e74c3c">*</span></label><input type="password" class="form-control" id="oldPassword" placeholder="Nhập mật khẩu hiện tại"><div class="form-error">Vui lòng nhập</div></div>
                            <div class="form-group" id="newPassGroup"><label>Mật khẩu mới <span style="color:#e74c3c">*</span></label><input type="password" class="form-control" id="newPassword" placeholder="VD: Abc@1234" oninput="checkStrength(this.value)"><div class="password-strength"><div class="password-strength-bar" id="strengthBar"></div></div><div class="password-hint">Tối thiểu 8 ký tự, gồm hoa/thường/số/đặc biệt</div><div class="form-error">Không đạt yêu cầu</div></div>
                            <div class="form-group" id="confirmPassGroup"><label>Nhập lại mật khẩu mới <span style="color:#e74c3c">*</span></label><input type="password" class="form-control" id="confirmNewPassword" placeholder="Nhập lại"><div class="form-error">Không khớp</div></div>
                            <div class="form-actions"><button type="submit" class="btn btn-primary">🔐 Đổi mật khẩu</button></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- MODAL: CHINH SUA THONG TIN CA NHAN -->
    <div class="modal-overlay" id="editInfoModal" onclick="if(event.target===this)closeEditModal()">
        <div class="modal-box" style="max-width:520px;">
            <div class="modal-header"><h3>✏️ Chỉnh sửa thông tin</h3><button class="modal-close" onclick="closeEditModal()">✕</button></div>
            <div class="modal-body" style="padding:28px;">
                <div class="form-group"><label>Họ và tên</label><input type="text" class="form-control" id="editName" value="Nguyễn Văn An" disabled style="background:#f0f4f8;color:#8c8c9a;"></div>
                <div class="form-row" style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                    <div class="form-group"><label>Số điện thoại</label><input type="tel" class="form-control" id="editPhone" value="0901000001"></div>
                    <div class="form-group"><label>Email</label><input type="email" class="form-control" id="editEmail" value="nguyenvanan@gmail.com"></div>
                </div>
                <div class="form-row" style="display:grid;grid-template-columns:1fr 1fr;gap:16px;">
                    <div class="form-group"><label>Ngày sinh</label><input type="date" class="form-control" id="editDob" value="1990-05-15"></div>
                    <div class="form-group"><label>Giới tính</label><select class="form-control" id="editGender"><option value="Nam" selected>Nam</option><option value="Nữ">Nữ</option></select></div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeEditModal()">Hủy</button>
                <button class="btn btn-primary" onclick="saveEditInfo()">Xác nhận</button>
            </div>
        </div>
    </div>

    <!-- MODAL: CHINH SUA LICH HEN (Giong giao dien dat lich) -->
    <div class="modal-overlay" id="editAppointmentModal" onclick="if(event.target===this)closeEditAppointment()">
        <div class="modal-box" style="max-width:780px;">
            <div class="modal-header"><h3>📝 Chỉnh sửa lịch hẹn</h3><button class="modal-close" onclick="closeEditAppointment()">✕</button></div>
            <div class="modal-body" style="padding:28px;max-height:75vh;overflow-y:auto;overflow-x:hidden;">
                <!-- Ngay + Gio -->
                <div class="nb-row-2">
                    <div class="form-group" id="editDateGroup">
                        <label>Ngày khám <span class="req">*</span></label>
                        <input type="date" class="form-control" id="editDate">
                        <div class="form-error">Vui lòng chọn ngày</div>
                    </div>
                    <div class="form-group" id="editTimeGroup">
                        <label>Giờ khám <span class="req">*</span></label>
                        <select class="form-control" id="editTimeSelect">
                            <option value="">Chọn giờ</option>
                        </select>
                        <div class="form-error">Vui lòng chọn giờ</div>
                    </div>
                </div>

                <!-- Checkbox grid dich vu -->
                <div class="form-group" id="editServiceGroup">
                    <label>🦷 Chọn dịch vụ (có thể chọn nhiều) <span class="req">*</span></label>
                    <div class="svc-checkbox-grid" id="editSvcGrid"></div>
                    <div class="form-error">Vui lòng chọn ít nhất 1 dịch vụ</div>
                </div>

                <!-- Dich vu da chon -->
                <div id="editSelectedBox" class="nb-selected-box" style="display:none;">
                    <div class="nb-selected-header">🛒 Dịch vụ đã chọn</div>
                    <div class="nb-selected-list" id="editSelectedList"></div>
                    <div class="nb-total">
                        <span class="nb-total-label">💰 Tổng tiền:</span>
                        <span id="editTotalValue">0</span> đ
                    </div>
                </div>

                <!-- Ghi chu -->
                <div class="form-group" style="margin-top:16px;">
                    <label>Ghi chú</label>
                    <textarea class="form-control" id="editNote" placeholder="Ghi chú thêm..." rows="3"></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeEditAppointment()">Hủy</button>
                <button class="btn btn-primary" onclick="saveEditAppointment()">Lưu thay đổi</button>
            </div>
        </div>
    </div>

    <!-- MODAL: CHI TIET PHIEU KHAM -->
    <div class="modal-overlay" id="detailModal" onclick="if(event.target===this)closeModal()">
        <div class="modal-box receipt-modal">
            <div class="modal-body" id="modalBody"></div>
            <div class="modal-footer"><button class="btn btn-outline" onclick="closeModal()">Đóng</button></div>
        </div>
    </div>

    <!-- MODAL: XAC NHAN HUY PENDING -->
    <div class="modal-overlay" id="cancelModal" onclick="if(event.target===this)closeCancelModal()">
        <div class="modal-box confirm-box">
            <div class="modal-header" style="background:#e74c3c"><h3>⚠️ Xác nhận hủy</h3><button class="modal-close" onclick="closeCancelModal()">✕</button></div>
            <div class="modal-body">
                <span class="confirm-icon">🗓️</span>
                <div class="confirm-title">Hủy lịch hẹn này?</div>
                <div class="confirm-message">Lịch hẹn sẽ bị xóa khỏi danh sách.</div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeCancelModal()">Không</button>
                <button class="btn" onclick="doCancelAppointment()" style="background:#e74c3c;color:white">Có, hủy</button>
            </div>
        </div>
    </div>

    <!-- MODAL: XAC NHAN HUY CONFIRMED -->
    <div class="modal-overlay" id="cancelConfirmedModal" onclick="if(event.target===this)closeCancelConfirmedModal()">
        <div class="modal-box confirm-box">
            <div class="modal-header" style="background:#e74c3c"><h3>⚠️ Xác nhận hủy lịch</h3><button class="modal-close" onclick="closeCancelConfirmedModal()">✕</button></div>
            <div class="modal-body">
                <span class="confirm-icon">🗓️</span>
                <div class="confirm-title">Bạn chắc chắn muốn hủy?</div>
                <div class="confirm-message">Lịch hẹn đã xác nhận cần thông qua phòng khám để hủy.</div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-outline" onclick="closeCancelConfirmedModal()">Không</button>
                <button class="btn" onclick="doCancelConfirmed()" style="background:#e74c3c;color:white">Xác nhận hủy</button>
            </div>
        </div>
    </div>

    <!-- MODAL: THONG BAO CHO XU LY -->
    <div class="modal-overlay" id="cancelInfoModal" onclick="if(event.target===this)closeCancelInfoModal()">
        <div class="modal-box confirm-box">
            <div class="modal-header" style="background:var(--accent)"><h3>📞 Thông báo</h3><button class="modal-close" onclick="closeCancelInfoModal()">✕</button></div>
            <div class="modal-body">
                <span class="confirm-icon">⏳</span>
                <div class="confirm-title">Yêu cầu hủy đang chờ xử lý</div>
                <div class="confirm-message">Vui lòng liên hệ hotline <strong style="color:var(--primary)">1900 1533</strong> để được hỗ trợ hủy lịch nhanh nhất.</div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-accent" onclick="closeCancelInfoModal()">Đã hiểu</button>
            </div>
        </div>
    </div>

    <jsp:include page="../components/footer.jsp" />
    <script src="${pageContext.request.contextPath}/assets/js/hoso.js"></script>
</body>
</html>
