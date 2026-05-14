<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.dentalclinic.model.TaiKhoan" %>
<%
    // Lấy thông tin người dùng từ Session
    TaiKhoan loggedInUser = (TaiKhoan) session.getAttribute("loggedInUser");
%>
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
<body id="receptionDashboardPage">
<c:set var="rcvLoc" value="${empty receptionLoc ? 'all' : receptionLoc}"/>
<c:set var="rcvView" value="${empty receptionViewMode ? 'day' : receptionViewMode}"/>
<c:url var="urlReceptionPeriodDay" value="/reception-dashboard"><c:param name="view" value="day"/><c:param name="ngay" value="${selectedDateNgay}"/><c:param name="loc" value="${rcvLoc}"/></c:url>
<c:url var="urlReceptionPeriodWeek" value="/reception-dashboard"><c:param name="view" value="week"/><c:param name="ngay" value="${selectedDateNgay}"/><c:param name="loc" value="${rcvLoc}"/></c:url>
<c:url var="urlReceptionPeriodMonth" value="/reception-dashboard"><c:param name="view" value="month"/><c:param name="ngay" value="${selectedDateNgay}"/><c:param name="loc" value="${rcvLoc}"/></c:url>
<c:url var="urlReceptionPeriodYear" value="/reception-dashboard"><c:param name="view" value="year"/><c:param name="ngay" value="${selectedDateNgay}"/><c:param name="loc" value="${rcvLoc}"/></c:url>
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
            <li><a href="${pageContext.request.contextPath}/reception-dashboard" class="active">Lịch hẹn</a></li>
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

    <div class="container">
        <!-- ==================== STATS CARDS ==================== -->
        <div class="stats-grid">
            <div class="stat-card<c:if test="${rcvLoc=='all'}"> active</c:if>" onclick="filterByStatus('all')" id="statAll">
                <div class="stat-header">
                    <h3>Tổng lịch hẹn</h3>
                    <div class="stat-icon"><i class="fas fa-calendar-check"></i></div>
                </div>
                <div class="stat-number" id="totalAppointments">${statTotal}</div>
                <div class="stat-change"><i class="fas fa-chart-line"></i> Tất cả lịch hẹn</div>
            </div>
            <div class="stat-card<c:if test="${rcvLoc=='confirmed'}"> active</c:if>" onclick="filterByStatus('confirmed')" id="statConfirmed">
                <div class="stat-header">
                    <h3>Đã duyệt</h3>
                    <div class="stat-icon"><i class="fas fa-check-circle"></i></div>
                </div>
                <div class="stat-number" id="confirmedCount">${statConfirmed}</div>
                <div class="stat-change"><i class="fas fa-check"></i> Sẵn sàng phục vụ</div>
            </div>
            <div class="stat-card<c:if test="${rcvLoc=='pending'}"> active</c:if>" onclick="filterByStatus('pending')" id="statPending">
                <div class="stat-header">
                    <h3>Chờ duyệt</h3>
                    <div class="stat-icon"><i class="fas fa-hourglass-half"></i></div>
                </div>
                <div class="stat-number" id="pendingCount">${statPending}</div>
                <div class="stat-change"><i class="fas fa-bell"></i> Cần xác nhận</div>
            </div>
            <div class="stat-card<c:if test="${rcvLoc=='completed'}"> active</c:if>" onclick="filterByStatus('completed')" id="statCompleted">
                <div class="stat-header">
                    <h3>Hoàn thành</h3>
                    <div class="stat-icon"><i class="fas fa-check-double"></i></div>
                </div>
                <div class="stat-number" id="completedCount">${statCompleted}</div>
                <div class="stat-change"><i class="fas fa-trophy"></i> Đã kết thúc</div>
            </div>
        </div>

        <!-- ==================== TOOLBAR ==================== -->
        <div class="toolbar reception-toolbar">
            <div class="date-picker reception-date-picker" style="position: relative;">
                <button type="button" class="btn-icon" id="prevDayBtn"><i class="fas fa-chevron-left"></i></button>
                <div class="date-display" id="currentDate" data-server-fixed="1"><c:out value="${receptionToolbarLabel}"/></div>
                <button type="button" class="btn-icon" id="nextDayBtn"><i class="fas fa-chevron-right"></i></button>
                <button type="button" class="btn-icon" id="todayBtn" title="Chọn ngày xem lịch"><i class="fas fa-calendar-day"></i></button>
                <div id="receptionCalendarPanel" class="reception-calendar-panel" style="display: none;" role="dialog" aria-label="Chọn ngày">
                    <label for="receptionDatePick" style="display:block;font-size:0.85rem;font-weight:600;margin-bottom:8px;">Chọn ngày</label>
                    <input type="date" id="receptionDatePick" value="${selectedDateNgay}" style="width:100%;padding:10px;border-radius:10px;border:1px solid var(--border-color);font:inherit;">
                    <button type="button" id="receptionCalendarApply" class="reception-calendar-apply-btn">Xem lịch hẹn</button>
                </div>
            </div>
            <div class="reception-period-toggle" role="group" aria-label="Xem theo khoảng thời gian">
                <a href="${urlReceptionPeriodDay}" class="period-btn<c:if test="${rcvView=='day'}"> active</c:if>">Ngày</a>
                <a href="${urlReceptionPeriodWeek}" class="period-btn<c:if test="${rcvView=='week'}"> active</c:if>">Tuần</a>
                <a href="${urlReceptionPeriodMonth}" class="period-btn<c:if test="${rcvView=='month'}"> active</c:if>">Tháng</a>
                <a href="${urlReceptionPeriodYear}" class="period-btn<c:if test="${rcvView=='year'}"> active</c:if>">Năm</a>
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
                    <c:forEach var="bs" items="${danhSachBacSi}">
                        <option value="${bs.bacSiID}">
                            ${empty bs.taiKhoan ? '—' : fn:escapeXml(bs.taiKhoan.hoTen)}
                            —
                            ${empty bs.chuyenKhoa ? '—' : fn:escapeXml(bs.chuyenKhoa.tenChuyenKhoa)}
                        </option>
                    </c:forEach>
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
                    <tr class="rcv-thead-row">
                        <th><span class="rcv-th">Ngày</span></th>
                        <th><span class="rcv-th">Giờ</span></th>
                        <th><span class="rcv-th">Bệnh nhân</span></th>
                        <th><span class="rcv-th">SĐT</span></th>
                        <th><span class="rcv-th">Bác sĩ</span></th>
                        <th><span class="rcv-th">Dịch vụ</span></th>
                        <th><span class="rcv-th">Phòng</span></th>
                        <th><span class="rcv-th">Trạng thái</span></th>
                        <th><span class="rcv-th">Thu tiền</span></th>
                        <th><span class="rcv-th">Thao tác</span></th>
                    </tr>
                </thead>
              <tbody id="appointmentTableBody">
    <c:forEach var="lh" items="${allLichHen}">
        <c:set var="stKey" value="pending"/>
        <c:choose>
            <c:when test="${lh.trangThai eq 'Đã hủy'}"><c:set var="stKey" value="cancelled"/></c:when>
            <c:when test="${lh.trangThai eq 'Hoàn thành' or lh.trangThai eq 'Đã thanh toán'}"><c:set var="stKey" value="completed"/></c:when>
            <c:when test="${lh.trangThai eq 'Đã duyệt' or lh.trangThai eq 'Đã xác nhận' or lh.trangThai eq 'Đã đến'}"><c:set var="stKey" value="confirmed"/></c:when>
            <c:when test="${lh.trangThai eq 'Chờ duyệt' or lh.trangThai eq 'Chờ xác nhận'}"><c:set var="stKey" value="pending"/></c:when>
        </c:choose>
        <c:set var="gioRaw"><c:out value="${lh.gioKham}"/></c:set>
        <tr data-lh-id="${lh.lichHenID}" data-status="${stKey}"
            data-bacsi-id="${lh.bacSiID}"
            data-patient-name="${lh.benhNhan ne null && lh.benhNhan.taiKhoan ne null ? fn:escapeXml(lh.benhNhan.taiKhoan.hoTen) : ''}"
            data-patient-phone="${lh.benhNhan ne null && lh.benhNhan.taiKhoan ne null && lh.benhNhan.taiKhoan.soDienThoai != null ? fn:escapeXml(lh.benhNhan.taiKhoan.soDienThoai) : ''}">
            <td class="rcv-td-muted"><fmt:formatDate value="${lh.ngayKham}" pattern="dd/MM/yyyy"/></td>
            <td><span class="rcv-time-pill">${fn:length(gioRaw) ge 5 ? fn:substring(gioRaw, 0, 5) : gioRaw}</span></td>
            <c:set var="ptName">${lh.benhNhan ne null && lh.benhNhan.taiKhoan ne null ? fn:escapeXml(lh.benhNhan.taiKhoan.hoTen) : ''}</c:set>
            <td>
                <div class="rcv-patient-pill">
                    <span class="rcv-patient-pill__av"><c:choose><c:when test="${fn:length(ptName) ge 1}">${fn:toUpperCase(fn:substring(ptName, 0, 1))}</c:when><c:otherwise>?</c:otherwise></c:choose></span>
                    <span class="rcv-patient-pill__nm">${ptName}</span>
                </div>
            </td>
            <td class="rcv-td-phone">
                <c:choose>
                    <c:when test="${lh.benhNhan ne null && lh.benhNhan.taiKhoan ne null && not empty lh.benhNhan.taiKhoan.soDienThoai}">
                        <span class="rcv-phone-pill"><i class="fas fa-phone"></i> ${fn:escapeXml(lh.benhNhan.taiKhoan.soDienThoai)}</span>
                    </c:when>
                    <c:otherwise><span class="rcv-dash">—</span></c:otherwise>
                </c:choose>
            </td>
            <c:set var="bsName">${lh.bacSi ne null && lh.bacSi.taiKhoan ne null ? fn:escapeXml(lh.bacSi.taiKhoan.hoTen) : ''}</c:set>
            <c:set var="bsSpec">${lh.bacSi ne null && not empty lh.bacSi.chuyenKhoa ? lh.bacSi.chuyenKhoa.tenChuyenKhoa : ''}</c:set>
            <td>
                <div class="rcv-dr-chip">
                    <span class="rcv-dr-chip__badge">${fn:length(bsName) ge 1 ? fn:toUpperCase(fn:substring(bsName, 0, 1)) : '?'}</span>
                    <div class="rcv-dr-chip__txt">
                        <span class="rcv-dr-chip__name">${bsName}</span>
                        <c:if test="${not empty bsSpec}"><span class="rcv-dr-chip__spec">${fn:escapeXml(bsSpec)}</span></c:if>
                    </div>
                </div>
            </td>
            
            <td>${lh.ghiChu != null ? fn:escapeXml(lh.ghiChu) : '—'}</td>
            
            <td>${fn:escapeXml(lh.tenPhong)}</td>
            
            <td>
                <c:set var="badgeClass" value="status-pending"/>
                <c:set var="statusLabel" value="${lh.trangThai}"/>
                <c:choose>
                    <c:when test="${lh.trangThai eq 'Đã hủy'}"><c:set var="badgeClass" value="status-cancelled"/></c:when>
                    <c:when test="${lh.trangThai eq 'Đã thanh toán'}"><c:set var="badgeClass" value="rcv-status-settled"/></c:when>
                    <c:when test="${lh.trangThai eq 'Hoàn thành'}"><c:set var="badgeClass" value="status-completed"/></c:when>
                    <c:when test="${lh.trangThai eq 'Đã duyệt' or lh.trangThai eq 'Đã xác nhận' or lh.trangThai eq 'Đã đến'}"><c:set var="badgeClass" value="status-confirmed"/></c:when>
                    <c:when test="${lh.trangThai eq 'Chờ duyệt' or lh.trangThai eq 'Chờ xác nhận'}">
                        <c:set var="badgeClass" value="status-pending"/>
                        <c:set var="statusLabel" value="Chờ duyệt"/>
                    </c:when>
                </c:choose>
                <div class="status-dropdown">
                    <span class="status-badge ${badgeClass}" onclick="toggleReceptionStatusMenu(${lh.lichHenID})">${statusLabel}</span>
                    <div class="status-menu" id="statusMenu_${lh.lichHenID}">
                        <div class="status-option" onclick="setReceptionStatus(${lh.lichHenID}, 'pending')">
                            <i class="fas fa-hourglass-half" style="color:#f59e0b;"></i> Chờ duyệt
                        </div>
                        <div class="status-option" onclick="setReceptionStatus(${lh.lichHenID}, 'approved')">
                            <i class="fas fa-check-circle" style="color:#10b981;"></i> Đã duyệt
                        </div>
                    </div>
                </div>
            </td>
            
            <td>
                <c:choose>
                    <c:when test="${lh.trangThai eq 'Đã thanh toán'}">
                        <span class="rcv-pay-pill rcv-pay-pill--done"><i class="fas fa-sack-dollar"></i> Đã thu</span>
                    </c:when>
                    <c:when test="${lh.trangThai eq 'Đã duyệt' or lh.trangThai eq 'Đã xác nhận' or lh.trangThai eq 'Đã đến'}">
                        <button class="btn-payment" onclick="openReceptionPaymentModal(${lh.lichHenID})">
                            <i class="fas fa-credit-card"></i> Thanh toán
                        </button>
                    </c:when>
                    <c:otherwise>
                        <span class="rcv-pay-pill rcv-pay-pill--open"><i class="fas fa-hourglass-half"></i> Chưa thu</span>
                    </c:otherwise>
                </c:choose>
            </td>
            
            <td>
                <div class="action-buttons">
                    <button class="btn-action" onclick="viewDetail(${lh.lichHenID})" title="Xem chi tiết">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn-action" onclick="deleteReceptionAppointment(${lh.lichHenID})" title="Xóa lịch hẹn">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </td>
        </tr>
    </c:forEach>
    
    <c:if test="${empty allLichHen}">
        <tr>
            <td colspan="10" style="text-align: center; padding: 20px;">
                Không có lịch hẹn nào cần xử lý.
            </td>
        </tr>
    </c:if>
</tbody>
                
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
                                <c:forEach var="bs" items="${danhSachBacSi}">
                                    <option value="${bs.bacSiID}">
                                        ${empty bs.taiKhoan ? '—' : fn:escapeXml(bs.taiKhoan.hoTen)}
                                        (${empty bs.chuyenKhoa ? '—' : fn:escapeXml(bs.chuyenKhoa.tenChuyenKhoa)})
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Phòng khám *</label>
                            <select id="room" required>
                                <option value="">Chọn phòng</option>
                                <c:forEach var="phong" items="${danhSachPhongKham}">
                                    <option value="${phong.phongID}">${fn:escapeXml(phong.tenPhong)}</option>
                                </c:forEach>
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

    <script>
        window.RECEPTION_CONTEXT = {
            baseUrl: '${pageContext.request.contextPath}/reception-dashboard',
            selectedDate: '<c:out value="${selectedDateNgay}"/>',
            viewMode: '<c:out value="${rcvView}"/>',
            loc: '<c:out value="${rcvLoc}"/>',
            histTotal: <c:choose><c:when test="${receptionHistBanner eq true}">true</c:when><c:otherwise>false</c:otherwise></c:choose>
        };
        window.SERVICE_LIST_FROM_DB = <c:out value="${empty serviceListJson ? '[]' : serviceListJson}" escapeXml="false"/>;
        window.ROOM_LIST_FROM_DB = <c:out value="${empty roomListJson ? '[]' : roomListJson}" escapeXml="false"/>;
    </script>
    <style>
        body#receptionDashboardPage .reception-toolbar,
        body#receptionDashboardPage .reception-date-picker {
            overflow: visible;
        }
        .reception-calendar-panel {
            position: absolute;
            top: calc(100% + 8px);
            left: 0;
            z-index: 12000;
            min-width: 260px;
            background: var(--bg-white, #fff);
            border: 1px solid var(--border-color, #e5e7eb);
            border-radius: 12px;
            padding: 14px;
            box-shadow: 0 12px 28px rgba(0,0,0,.12);
        }
        .reception-calendar-apply-btn {
            margin-top: 12px;
            width: 100%;
            padding: 10px;
            border-radius: 10px;
            background: var(--primary-color);
            color: #fff;
            border: none;
            font-weight: 600;
            cursor: pointer;
        }
        .reception-calendar-apply-btn:hover {
            background: var(--primary-dark);
        }
        .reception-toolbar {
            gap: 12px !important;
        }
        .reception-period-toggle {
            display: flex;
            align-items: center;
            gap: 8px;
            flex-wrap: wrap;
            margin: 0 auto;
            flex: 1;
            justify-content: center;
        }
        .reception-period-toggle .period-btn {
            padding: 8px 16px;
            border-radius: 10px;
            border: 1px solid var(--border-color);
            background: var(--bg-gray);
            color: var(--text-main);
            font-weight: 600;
            font-size: 0.875rem;
            text-decoration: none;
            cursor: pointer;
            transition: all 0.2s;
        }
        .reception-period-toggle .period-btn:hover {
            border-color: var(--primary-color);
            color: var(--primary-color);
        }
        .reception-period-toggle .period-btn.active {
            background: var(--primary-color);
            border-color: var(--primary-color);
            color: #fff;
        }

        body#receptionDashboardPage #appointmentTable {
            border-collapse: separate;
            border-spacing: 0;
        }
        body#receptionDashboardPage #appointmentTable .rcv-thead-row th {
            background: linear-gradient(180deg, #f8fafc 0%, #eef2ff 100%);
            border-bottom: 2px solid #c7d2fe;
            padding: 14px 10px;
            font-size: 0.72rem;
            font-weight: 800;
            letter-spacing: 0.06em;
            color: var(--text-main, #334155);
            text-transform: uppercase;
            white-space: nowrap;
            position: sticky;
            top: 0;
            z-index: 1;
        }
        body#receptionDashboardPage #appointmentTable .rcv-thead-row th:first-child { border-radius: 12px 0 0 0; }
        body#receptionDashboardPage #appointmentTable .rcv-thead-row th:last-child { border-radius: 0 12px 0 0; }
        body#receptionDashboardPage #appointmentTable .rcv-th { display: inline-block; vertical-align: middle; }

        body#receptionDashboardPage #appointmentTable td {
            padding: 12px 10px;
            vertical-align: middle;
            border-bottom: 1px solid var(--border-color, #e5e7eb);
        }
        body#receptionDashboardPage #appointmentTable .rcv-td-muted {
            font-size: 0.85rem;
            font-weight: 600;
            color: var(--text-sub, #64748b);
        }
        body#receptionDashboardPage #appointmentTable .rcv-time-pill {
            display: inline-flex;
            align-items: center;
            padding: 5px 12px;
            border-radius: 999px;
            font-size: 0.78rem;
            font-weight: 700;
            background: #eff6ff;
            color: #1d4ed8;
            border: 1px solid #bfdbfe;
        }

        body#receptionDashboardPage #appointmentTable .rcv-patient-pill {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        body#receptionDashboardPage #appointmentTable .rcv-patient-pill__av {
            width: 38px;
            height: 38px;
            border-radius: 12px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-weight: 800;
            font-size: 0.85rem;
            color: #fff;
            background: linear-gradient(135deg, #3b82f6, #6366f1);
            flex-shrink: 0;
        }
        body#receptionDashboardPage #appointmentTable .rcv-patient-pill__nm {
            font-weight: 600;
            color: var(--text-main);
            font-size: 0.92rem;
        }

        body#receptionDashboardPage #appointmentTable .rcv-td-phone { min-width: 118px; }
        body#receptionDashboardPage #appointmentTable .rcv-phone-pill {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            font-size: 0.82rem;
            font-weight: 600;
            color: #0f766e;
            background: #ccfbf1;
            padding: 5px 10px;
            border-radius: 999px;
            border: 1px solid #5eead4;
        }
        body#receptionDashboardPage #appointmentTable .rcv-phone-pill .fa-phone { opacity: .85; font-size: .75rem; }

        body#receptionDashboardPage #appointmentTable .rcv-dash {
            color: var(--text-sub);
            opacity: .6;
            font-weight: 600;
        }

        body#receptionDashboardPage #appointmentTable .rcv-dr-chip {
            display: flex;
            align-items: flex-start;
            gap: 10px;
            max-width: 220px;
        }
        body#receptionDashboardPage #appointmentTable .rcv-dr-chip__badge {
            flex-shrink: 0;
            width: 36px;
            height: 36px;
            border-radius: 999px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            font-size: 0.8rem;
            font-weight: 800;
            color: #7c3aed;
            background: linear-gradient(145deg, #fae8ff 0%, #ede9fe 100%);
            border: 2px solid #ddd6fe;
            box-shadow: 0 1px 3px rgba(124,58,237,0.12);
        }
        body#receptionDashboardPage #appointmentTable .rcv-dr-chip__txt {
            display: flex;
            flex-direction: column;
            gap: 2px;
            min-width: 0;
        }
        body#receptionDashboardPage #appointmentTable .rcv-dr-chip__name {
            font-weight: 600;
            font-size: 0.9rem;
            color: var(--text-main);
            line-height: 1.3;
            word-break: break-word;
        }
        body#receptionDashboardPage #appointmentTable .rcv-dr-chip__spec {
            font-size: 0.72rem;
            font-weight: 600;
            color: var(--text-sub);
        }

        body#receptionDashboardPage #appointmentTable .status-badge.rcv-status-settled {
            background: #f3e8ff;
            color: #7c3aed;
            border: 1px solid #ddd6fe;
        }
        body#receptionDashboardPage #appointmentTable .rcv-pay-pill {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            padding: 6px 12px;
            border-radius: 999px;
            font-size: 0.72rem;
            font-weight: 800;
            letter-spacing: 0.02em;
            white-space: nowrap;
            border: 1px solid transparent;
        }
        body#receptionDashboardPage #appointmentTable .rcv-pay-pill .fa-hourglass-half,
        body#receptionDashboardPage #appointmentTable .rcv-pay-pill .fa-sack-dollar { opacity: .9; }
        body#receptionDashboardPage #appointmentTable .rcv-pay-pill--done {
            background: #e8fdfa;
            color: #059669;
            border-color: #6ee7b7;
            box-shadow: 0 0 0 1px rgba(16,185,129,0.12) inset;
        }
        body#receptionDashboardPage #appointmentTable .rcv-pay-pill--open {
            background: linear-gradient(180deg, #fffbeb 0%, #fef3c7 100%);
            color: #b45309;
            border-color: #fcd34d;
        }
        /* ==================== USER DROPDOWN CSS ==================== */
        .dropdown-container {
            position: relative;
        }
        .user-dropdown-menu {
            display: none;
            position: absolute;
            top: 130%;
            right: 0;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.15);
            min-width: 220px;
            z-index: 9999;
            overflow: hidden;
            border: 1px solid var(--border-color, #e5e7eb);
            animation: fadeInDown 0.2s ease forwards;
        }
        .user-dropdown-menu.show {
            display: block;
        }
        .dropdown-item {
            display: flex;
            align-items: center;
            padding: 14px 20px;
            color: #4b5563;
            text-decoration: none;
            font-size: 0.9rem;
            font-weight: 600;
            transition: all 0.2s;
        }
        .dropdown-item i {
            margin-right: 12px;
            width: 18px;
            text-align: center;
            font-size: 1.1rem;
            color: #9ca3af;
            transition: color 0.2s;
        }
        .dropdown-item:hover {
            background: #f3f4f6;
            color: var(--primary-color, #2563eb);
        }
        .dropdown-item:hover i {
            color: var(--primary-color, #2563eb);
        }
        .dropdown-divider {
            height: 1px;
            background: #e5e7eb;
            margin: 4px 0;
        }
        .dropdown-item.text-danger {
            color: #dc2626;
        }
        .dropdown-item.text-danger:hover {
            background: #fef2f2;
        }
        .dropdown-item.text-danger i {
            color: #ef4444;
        }
        @keyframes fadeInDown {
            from { opacity: 0; transform: translateY(-10px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
    <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
</body>
</html>