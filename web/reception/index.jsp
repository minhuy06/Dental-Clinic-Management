<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
        <a href="${pageContext.request.contextPath}/" class="logo" style="text-decoration:none;color:inherit;">
            <div class="logo-icon">
                <i class="fas fa-tooth"></i>
            </div>
            <div class="logo-text">
                <h1>NHA KHOA 5AE</h1>
                <p>HỆ THỐNG QUẢN LÝ</p>
            </div>
        </a>
        <ul class="nav-menu">
            <li><a href="${pageContext.request.contextPath}/reception-dashboard" class="active">Lịch hẹn</a></li>
            <li><a href="${pageContext.request.contextPath}/reception-patient">Bệnh nhân</a></li>
            <li><a href="${pageContext.request.contextPath}/reception-report">Báo cáo</a></li>
            <li><a href="${pageContext.request.contextPath}/reception/cskh.jsp">CSKH</a></li>
        </ul>
        <a href="${pageContext.request.contextPath}/reception-dashboard" class="user-info" style="text-decoration:none;color:inherit;" title="Trang lễ tân">
            <div class="avatar" id="avatarBtn">
                <i class="fas fa-user" style="color: white;"></i>
            </div>
            <span class="staff-name"><c:out value="${sessionScope.loggedInUser.hoTen}" default="Lễ tân"/></span>
        </a>
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

        <!-- ==================== BẢNG LỊCH HẸN ==================== -->
        <div class="table-container reception-appointment-table">
            <table id="appointmentTable">
                <thead>
                    <tr>
                        <th>Ngày</th>
                        <th>Giờ</th>
                        <th>Bệnh nhân</th>
                        <th>Số điện thoại</th>
                        <th>Bác sĩ</th>
                        <th>Dịch vụ</th>
                        <th>Phòng</th>
                        <th>Trạng thái</th>
                        <th>Thu tiền</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
              <tbody id="appointmentTableBody">
    <c:forEach var="lh" items="${allLichHen}">
        <c:set var="stKey" value="pending"/>
        <c:choose>
            <c:when test="${lh.trangThai eq 'Đã hủy'}"><c:set var="stKey" value="cancelled"/></c:when>
            <c:when test="${lh.trangThai eq 'Hoàn thành' or lh.trangThai eq 'Đã hoàn thành' or lh.trangThai eq 'Đã thanh toán'}"><c:set var="stKey" value="completed"/></c:when>
            <c:when test="${lh.trangThai eq 'Đã khám'}"><c:set var="stKey" value="examined"/></c:when>
            <c:when test="${lh.trangThai eq 'Đang khám'}"><c:set var="stKey" value="examining"/></c:when>
            <c:when test="${lh.trangThai eq 'Đã đến'}"><c:set var="stKey" value="arrived"/></c:when>
            <c:when test="${lh.trangThai eq 'Đã duyệt' or lh.trangThai eq 'Đã xác nhận'}"><c:set var="stKey" value="confirmed"/></c:when>
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
            <td class="rcv-td-name">${ptName}</td>
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
            <td class="rcv-td-doctor">
                <span class="rcv-dr-name">${bsName}</span>
                <c:if test="${not empty bsSpec}"><span class="rcv-dr-spec">${fn:escapeXml(bsSpec)}</span></c:if>
            </td>
            
            <td class="service-tags-cell">
                <c:choose>
                    <c:when test="${not empty lh.tenDichVuList}">
                        <c:forEach var="dvName" items="${lh.tenDichVuList}">
                            <span class="history-tag">${fn:escapeXml(dvName)}</span>
                        </c:forEach>
                    </c:when>
                    <c:otherwise><span class="no-data">Chưa chọn dịch vụ</span></c:otherwise>
                </c:choose>
            </td>
            <td><c:out value="${empty lh.tenPhong ? '—' : lh.tenPhong}"/></td>
            
            <td>
                <c:set var="badgeClass" value="status-pending"/>
                <c:set var="statusLabel" value="${lh.trangThai}"/>
                <c:choose>
                    <c:when test="${lh.trangThai eq 'Đã hủy'}"><c:set var="badgeClass" value="status-cancelled"/></c:when>
                    <c:when test="${lh.trangThai eq 'Đã thanh toán'}">
                        <c:set var="badgeClass" value="status-completed"/>
                        <c:set var="statusLabel" value="Đã thanh toán"/>
                    </c:when>
                    <c:when test="${lh.trangThai eq 'Đã khám'}">
                        <c:set var="badgeClass" value="status-examined"/>
                        <c:set var="statusLabel" value="Đã khám"/>
                    </c:when>
                    <c:when test="${lh.trangThai eq 'Đã hoàn thành' or lh.trangThai eq 'Hoàn thành'}">
                        <c:set var="badgeClass" value="status-completed"/>
                        <c:set var="statusLabel" value="Đã hoàn thành"/>
                    </c:when>
                    <c:when test="${lh.trangThai eq 'Đang khám'}"><c:set var="badgeClass" value="status-examining"/></c:when>
                    <c:when test="${lh.trangThai eq 'Đã đến'}"><c:set var="badgeClass" value="status-arrived"/></c:when>
                    <c:when test="${lh.trangThai eq 'Đã duyệt' or lh.trangThai eq 'Đã xác nhận'}">
                        <c:set var="badgeClass" value="status-confirmed"/>
                        <c:set var="statusLabel" value="Đã xác nhận"/>
                    </c:when>
                    <c:when test="${lh.trangThai eq 'Chờ duyệt' or lh.trangThai eq 'Chờ xác nhận'}">
                        <c:set var="badgeClass" value="status-pending"/>
                        <c:set var="statusLabel" value="Chờ duyệt"/>
                    </c:when>
                </c:choose>
                <c:set var="isExamined" value="${lh.trangThai eq 'Đã khám' or lh.trangThai eq 'Đã hoàn thành' or lh.trangThai eq 'Hoàn thành'}"/>
                <c:set var="canChangeStatus" value="${lh.trangThai eq 'Chờ xác nhận' or lh.trangThai eq 'Chờ duyệt' or lh.trangThai eq 'Đã xác nhận' or lh.trangThai eq 'Đã duyệt' or lh.trangThai eq 'Đã đến' or isExamined}"/>
                <div class="status-dropdown">
                    <span class="status-badge ${badgeClass}" <c:if test="${canChangeStatus}">onclick="toggleReceptionStatusMenu(${lh.lichHenID})"</c:if>>${statusLabel}</span>
                    <c:if test="${canChangeStatus}">
                    <div class="status-menu" id="statusMenu_${lh.lichHenID}">
                        <c:if test="${isExamined}">
                        <div class="status-option" onclick="setReceptionStatus(${lh.lichHenID}, 'arrived')">
                            <i class="fas fa-user-check" style="color:#2563eb;"></i> Chuyển lại: Đã đến
                        </div>
                        </c:if>
                        <c:if test="${not isExamined and lh.trangThai ne 'Đã đến'}">
                        <div class="status-option" onclick="setReceptionStatus(${lh.lichHenID}, 'pending')">
                            <i class="fas fa-hourglass-half" style="color:#f59e0b;"></i> Chờ duyệt
                        </div>
                        </c:if>
                        <c:if test="${not isExamined}">
                        <div class="status-option" onclick="setReceptionStatus(${lh.lichHenID}, 'approved')">
                            <i class="fas fa-check-circle" style="color:#10b981;"></i> Đã xác nhận
                        </div>
                        <c:if test="${lh.trangThai ne 'Đã đến'}">
                        <div class="status-option" onclick="setReceptionStatus(${lh.lichHenID}, 'arrived')">
                            <i class="fas fa-user-check" style="color:#2563eb;"></i> Đã đến
                        </div>
                        <div class="status-option" onclick="setReceptionStatus(${lh.lichHenID}, 'cancelled')">
                            <i class="fas fa-ban" style="color:#ef4444;"></i> Đã hủy
                        </div>
                        </c:if>
                        </c:if>
                    </div>
                    </c:if>
                </div>
            </td>
            
            <td>
                <c:set var="choThu" value="${lh.trangThai eq 'Đã khám'}"/>
                <c:set var="daHoanThanh" value="${lh.trangThai eq 'Đã hoàn thành' or lh.trangThai eq 'Hoàn thành' or lh.trangThai eq 'Đã thanh toán'}"/>
                <c:choose>
                    <c:when test="${daHoanThanh}">
                        <span class="rcv-pay-pill rcv-pay-pill--done"><i class="fas fa-check"></i> Đã thu</span>
                    </c:when>
                    <c:when test="${choThu}">
                        <button type="button" class="btn-payment" onclick="openReceptionPaymentModal(${lh.lichHenID})">
                            <i class="fas fa-credit-card"></i> Thanh toán
                        </button>
                    </c:when>
                    <c:otherwise>
                        <span class="rcv-dash">—</span>
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
        window.APP_CONTEXT_PATH = '${pageContext.request.contextPath}';
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

        body#receptionDashboardPage .reception-appointment-table.table-container {
            background: #fff;
            border-radius: 16px;
            overflow-x: auto;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            border: 1px solid #e5e7eb;
            margin-top: 8px;
        }
        body#receptionDashboardPage #appointmentTable {
            width: 100%;
            border-collapse: collapse;
            min-width: 1000px;
        }
        body#receptionDashboardPage #appointmentTable th {
            background: #f8fafc;
            padding: 15px;
            text-align: left;
            font-size: 0.75rem;
            font-weight: 700;
            text-transform: uppercase;
            color: #6b7280;
            border-bottom: 1px solid #e5e7eb;
        }
        body#receptionDashboardPage #appointmentTable td {
            padding: 15px;
            border-bottom: 1px solid #e5e7eb;
            font-size: 0.85rem;
            vertical-align: middle;
        }
        body#receptionDashboardPage #appointmentTable tbody tr:hover {
            background: #f8fafc;
        }
        body#receptionDashboardPage .history-tag {
            background: #fef3c7;
            color: #d97706;
            padding: 4px 10px;
            border-radius: 12px;
            font-size: 0.7rem;
            display: inline-block;
            margin: 2px 4px 2px 0;
        }
        body#receptionDashboardPage .service-tags-cell {
            max-width: 220px;
        }
        body#receptionDashboardPage .no-data {
            color: #6b7280;
            font-style: italic;
            font-size: 0.75rem;
        }
        body#receptionDashboardPage .status-badge.status-arrived {
            background: #dbeafe;
            color: #1d4ed8;
        }
        body#receptionDashboardPage .status-badge.status-examining {
            background: #ede9fe;
            color: #6d28d9;
        }
        body#receptionDashboardPage .status-badge.status-examined {
            background: #d1fae5;
            color: #047857;
        }
        body#receptionDashboardPage #appointmentTable td.action-btns,
        body#receptionDashboardPage #appointmentTable .action-buttons {
            display: flex;
            gap: 8px;
            align-items: center;
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
            font-family: 'Inter', system-ui, -apple-system, sans-serif;
            font-size: 0.875rem;
            font-weight: 500;
            color: var(--text-main, #334155);
            line-height: 1.4;
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

        body#receptionDashboardPage #appointmentTable .rcv-td-name {
            font-weight: 600;
            color: var(--text-main, #1e293b);
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

        body#receptionDashboardPage #appointmentTable .rcv-td-doctor {
            max-width: 220px;
        }
        body#receptionDashboardPage #appointmentTable .rcv-dr-name {
            display: block;
            font-weight: 600;
            color: var(--text-main, #1e293b);
            word-break: break-word;
        }
        body#receptionDashboardPage #appointmentTable .rcv-dr-spec {
            display: block;
            margin-top: 2px;
            font-size: 0.8125rem;
            font-weight: 500;
            color: var(--text-sub, #64748b);
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
        body#receptionDashboardPage #appointmentTable .rcv-pay-cell {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            gap: 6px;
        }
    </style>
    <script src="${pageContext.request.contextPath}/assets/js/script.js?v=20260517a"></script>
</body>
</html>