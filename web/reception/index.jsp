<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nha Khoa 5AE - Quản Lý Lịch Hẹn Cao Cấp</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:opsz,wght@14..32,300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style_1.css">
</head>
<body>
    <!-- ==================== HEADER MÀU XANH ==================== -->
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
            <li><a href="index.jsp" class="active">Lịch hẹn</a></li>
            <li><a href="benhnhan.jsp">Bệnh nhân</a></li>
            <li><a href="baocao.jsp">Báo cáo</a></li>
            <li><a href="cskh.jsp">CSKH</a></li>
        </ul>
        <div class="user-info">
            <div class="avatar" id="avatarBtn">
                <i class="fas fa-user"  style="color: white;"></i>
            </div>
            <span class="staff-name">Lễ Tân 1</span>
        </div>
    </div>

    <div class="container">
        <!-- ==================== STATS CARDS ==================== -->
        <div class="stats-grid">
            <div class="stat-card" onclick="filterByStatus('all')" id="statAll">
                <div class="stat-header">
                    <h3>Tổng lịch hẹn</h3>
                    <div class="stat-icon"><i class="fas fa-calendar-check"></i></div>
                </div>
                <div class="stat-number" id="totalAppointments">0</div>
                <div class="stat-change"><i class="fas fa-chart-line"></i> Tất cả lịch hẹn</div>
            </div>
            <div class="stat-card" onclick="filterByStatus('confirmed')" id="statConfirmed">
                <div class="stat-header">
                    <h3>Đã duyệt</h3>
                    <div class="stat-icon"><i class="fas fa-check-circle"></i></div>
                </div>
                <div class="stat-number" id="confirmedCount">0</div>
                <div class="stat-change"><i class="fas fa-check"></i> Sẵn sàng phục vụ</div>
            </div>
            <div class="stat-card" onclick="filterByStatus('pending')" id="statPending">
                <div class="stat-header">
                    <h3>Chờ duyệt</h3>
                    <div class="stat-icon"><i class="fas fa-hourglass-half"></i></div>
                </div>
                <div class="stat-number" id="pendingCount">0</div>
                <div class="stat-change"><i class="fas fa-bell"></i> Cần xác nhận</div>
            </div>
            <div class="stat-card" onclick="filterByStatus('completed')" id="statCompleted">
                <div class="stat-header">
                    <h3>Hoàn thành</h3>
                    <div class="stat-icon"><i class="fas fa-check-double"></i></div>
                </div>
                <div class="stat-number" id="completedCount">0</div>
                <div class="stat-change"><i class="fas fa-trophy"></i> Đã kết thúc</div>
            </div>
        </div>

        <!-- ==================== TOOLBAR ==================== -->
        <div class="toolbar">
            <div class="date-picker">
                <button class="btn-icon" id="prevDayBtn"><i class="fas fa-chevron-left"></i></button>
                <div class="date-display" id="currentDate">Hôm nay</div>
                <button class="btn-icon" id="nextDayBtn"><i class="fas fa-chevron-right"></i></button>
                <button class="btn-icon" id="todayBtn"><i class="fas fa-calendar-day"></i></button>
            </div>
            <div class="view-options">
                <button class="active" data-view="day">Ngày</button>
                <button data-view="week">Tuần</button>
                <button data-view="month">Tháng</button>
            </div>
            <button class="btn-add" id="addAppointmentBtn">
                <i class="fas fa-plus"></i> Đặt lịch mới
            </button>
        </div>

        <!-- ==================== FILTER SECTION ==================== -->
        <div class="filter-section">
            <div class="filter-group">
                <label><i class="fas fa-user-md"></i> Bác sĩ</label>
                <select id="filterDoctor">
                    <option value="">Tất cả bác sĩ</option>
                    <option value="BS. Nguyễn Hải">BS. Nguyễn Hải</option>
                    <option value="BS. Trần Tâm">BS. Trần Tâm</option>
                    <option value="BS. Lê Quang">BS. Lê Quang</option>
                    <option value="BS. Phạm Hương">BS. Phạm Hương</option>
                    <option value="BS. Hoàng Quân">BS. Hoàng Quân</option>
                </select>
            </div>
            <div class="filter-group">
                <label><i class="fas fa-search"></i> Tìm kiếm</label>
                <input type="text" id="searchInput" placeholder="Tìm theo tên hoặc SĐT...">
            </div>
        </div>

        <!-- ==================== SCHEDULE TABLE ==================== -->
        <div class="schedule-container">
            <table id="appointmentTable">
                <thead>
                    <tr>
                        <th>Giờ hẹn</th>
                        <th>Bệnh nhân</th>
                        <th>Bác sĩ</th>
                        <th>Dịch vụ & Số lượng</th>
                        <th>Phòng</th>
                        <th>Trạng thái</th>
                        <th>Thanh toán</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="appointmentTableBody"></tbody>
            </table>
        </div>
    </div>

    <!-- ==================== MODAL THÊM/SỬA LỊCH HẸN ==================== -->
    <div id="appointmentModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="modalTitle">Đặt lịch hẹn mới</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <form id="appointmentForm">
                    <input type="hidden" id="appointmentId">
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Họ tên bệnh nhân *</label>
                            <input type="text" id="patientName" required placeholder="Nhập họ tên">
                        </div>
                        <div class="form-group">
                            <label>Số điện thoại *</label>
                            <input type="tel" id="patientPhone" required placeholder="Nhập số điện thoại">
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Ngày khám *</label>
                            <input type="date" id="appointmentDate" required>
                        </div>
                        <div class="form-group">
                            <label>Giờ khám *</label>
                            <select id="appointmentTime" required>
                                <option value="">Chọn giờ</option>
                                <option value="08:00">08:00</option><option value="08:30">08:30</option>
                                <option value="09:00">09:00</option><option value="09:30">09:30</option>
                                <option value="10:00">10:00</option><option value="10:30">10:30</option>
                                <option value="11:00">11:00</option><option value="11:30">11:30</option>
                                <option value="13:00">13:00</option><option value="13:30">13:30</option>
                                <option value="14:00">14:00</option><option value="14:30">14:30</option>
                                <option value="15:00">15:00</option><option value="15:30">15:30</option>
                                <option value="16:00">16:00</option><option value="16:30">16:30</option>
                                <option value="17:00">17:00</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Bác sĩ *</label>
                            <select id="doctorName" required>
                                <option value="">Chọn bác sĩ</option>
                                <option value="BS. Nguyễn Hải">BS. Nguyễn Hải (CKI - Tổng quát)</option>
                                <option value="BS. Trần Tâm">BS. Trần Tâm (Thạc sĩ - Chỉnh nha)</option>
                                <option value="BS. Lê Quang">BS. Lê Quang (Tiến sĩ - Phục hình)</option>
                                <option value="BS. Phạm Hương">BS. Phạm Hương (CKI - Thẩm mỹ)</option>
                                <option value="BS. Hoàng Quân">BS. Hoàng Quân (CKII - PT miệng)</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Phòng khám *</label>
                            <select id="room" required>
                                <option value="">Chọn phòng</option>
                                <option value="Phòng 1">Phòng 1 (Tổng quát)</option>
                                <option value="Phòng 2">Phòng 2 (Chỉnh nha)</option>
                                <option value="Phòng 3">Phòng 3 (Phục hình)</option>
                                <option value="Phòng 4">Phòng 4 (Phẫu thuật)</option>
                                <option value="Phòng 5">Phòng 5 (VIP)</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label><i class="fas fa-tooth"></i> Chọn dịch vụ (có thể chọn nhiều) *</label>
                        <div class="services-grid" id="servicesGrid"></div>
                    </div>
                    
                    <div class="selected-services-summary" id="selectedServicesSummary">
                        <h4><i class="fas fa-shopping-cart"></i> Dịch vụ đã chọn:</h4>
                        <div class="selected-tags" id="selectedTags"></div>
                        <div class="total-amount">
                            <i class="fas fa-money-bill-wave"></i> Tổng tiền: <span id="totalAmount">0</span> đ
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label>Ghi chú</label>
                        <textarea id="notes" rows="2" placeholder="Ghi chú thêm..."></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel"><i class="fas fa-times"></i> Hủy</button>
                <button class="btn-save"><i class="fas fa-save"></i> Lưu lịch hẹn</button>
            </div>
        </div>
    </div>

    <!-- ==================== MODAL THANH TOÁN ==================== -->
    <div id="paymentModal" class="modal">
        <div class="modal-content" style="max-width: 650px;">
            <div class="modal-header">
                <h3><i class="fas fa-file-invoice"></i> Hóa đơn thanh toán</h3>
                <span class="close-payment">&times;</span>
            </div>
            <div class="modal-body" style="padding: 0;">
                <div class="invoice">
                    <div class="invoice-header">
                        <h2>NHA KHOA 5AE</h2>
                        <p>HÓA ĐƠN THANH TOÁN DỊCH VỤ</p>
                    </div>
                    <div class="invoice-body">
                        <div class="invoice-info">
                            <div class="info-item">
                                <div class="label">MÃ HÓA ĐƠN</div>
                                <div class="value" id="invoiceId">#HD-20240001</div>
                            </div>
                            <div class="info-item">
                                <div class="label">NGÀY LẬP</div>
                                <div class="value" id="invoiceDate"></div>
                            </div>
                            <div class="info-item">
                                <div class="label">NGÀY THANH TOÁN</div>
                                <div class="value" id="paymentDate"></div>
                            </div>
                        </div>
                        
                        <div class="customer-info">
                            <h4><i class="fas fa-user-circle"></i> THÔNG TIN KHÁCH HÀNG</h4>
                            <div class="customer-detail">
                                <span><i class="fas fa-user"></i> <strong id="paymentPatientName"></strong></span>
                                <span><i class="fas fa-phone"></i> <span id="paymentPatientPhone"></span></span>
                                <span><i class="fas fa-calendar"></i> Ngày khám: <span id="paymentAppointmentDate"></span></span>
                                <span><i class="fas fa-user-md"></i> Bác sĩ: <span id="paymentDoctor"></span></span>
                            </div>
                        </div>
                        
                        <table class="invoice-table" id="invoiceServicesTable">
                            <thead><tr><th>Dịch vụ</th><th>Số lượng</th><th>Đơn giá</th><th>Thành tiền</th></tr></thead>
                            <tbody id="invoiceServicesBody"></tbody>
                        </table>
                        
                        <div class="invoice-summary">
                            <div class="summary-row"><span class="label">Tạm tính:</span><span class="value" id="subtotal">0đ</span></div>
                            <div class="summary-row"><span class="label">VAT (10%):</span><span class="value" id="vatAmount">0đ</span></div>
                            <div class="summary-row total"><span class="label">TỔNG CỘNG:</span><span class="value" id="totalAmountInvoice">0đ</span></div>
                        </div>
                        
                        <div class="payment-method-section">
                            <label><i class="fas fa-credit-card"></i> Phương thức thanh toán</label>
                            <select id="paymentMethod" class="payment-method-select">
                                <option value="Tiền mặt">💵 Tiền mặt</option>
                                <option value="Chuyển khoản">🏦 Chuyển khoản</option>
                                <option value="Thẻ Visa/Master">💳 Thẻ Visa/Master</option>
                                <option value="Momo">📱 Ví Momo</option>
                                <option value="ZaloPay">📱 ZaloPay</option>
                            </select>
                        </div>
                    </div>
                    <div class="invoice-footer">
                        <p><i class="fas fa-check-circle"></i> Cảm ơn quý khách! Hẹn gặp lại!</p>
                        <p>Mọi thắc mắc vui lòng liên hệ CSKH: 1900 1234</p>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel"><i class="fas fa-times"></i> Hủy</button>
                <button class="btn-save" id="confirmPaymentBtn"><i class="fas fa-check-circle"></i> Xác nhận thanh toán</button>
            </div>
        </div>
    </div>

    <!-- ==================== FOOTER ==================== -->
    <div class="footer">
        <p><i class="fas fa-tooth"></i> NHA KHOA 5AE - Chất lượng tạo niềm tin</p>
        <p>© 2024 Nha Khoa 5AE - Hệ thống quản lý lịch hẹn chuyên nghiệp</p>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>