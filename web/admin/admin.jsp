<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nha Khoa 5AE - Quản Trị Hệ Thống</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body>

    <!-- HEADER -->
    <div class="header">
        <div class="logo">
            <div class="logo-icon"><i class="fas fa-tooth"></i></div>
            <div class="logo-text"><h1>NHA KHOA 5AE</h1><p>HỆ THỐNG QUẢN TRỊ</p></div>
        </div>
        <ul class="nav-menu">
            <li><a href="#" class="active" onclick="switchTab('services',this);return false;"><i class="fas fa-tooth"></i> Quản lý dịch vụ</a></li>
            <li><a href="#" onclick="switchTab('staff',this);return false;"><i class="fas fa-user-md"></i> Quản lý nhân sự</a></li>
            <li><a href="#" onclick="switchTab('accounts',this);return false;"><i class="fas fa-users-cog"></i> Quản lý tài khoản</a></li>
            <li><a href="#" onclick="switchTab('revenue',this);return false;"><i class="fas fa-chart-line"></i> Quản lý doanh thu</a></li>
        </ul>
        <div class="user-info">
            <i class="fas fa-bell" style="color:#bfdbfe;cursor:pointer;"></i>
            <div class="avatar"><i class="fas fa-user-shield" style="color:white;"></i></div>
        </div>
    </div>

    <div class="container">

        <!-- ==================== TAB: DỊCH VỤ ==================== -->
        <div class="tab-panel active" id="panel-services">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header"><h3>Tổng dịch vụ</h3><div class="stat-icon"><i class="fas fa-list"></i></div></div>
                    <div class="stat-number" id="statTotalServices">0</div>
                    <div class="stat-change"><i class="fas fa-chart-line"></i> Tất cả dịch vụ</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Đang hoạt động</h3><div class="stat-icon green"><i class="fas fa-check-circle"></i></div></div>
                    <div class="stat-number" id="statActiveServices">0</div>
                    <div class="stat-change"><i class="fas fa-check"></i> Khả dụng</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Danh mục</h3><div class="stat-icon purple"><i class="fas fa-tags"></i></div></div>
                    <div class="stat-number" id="statCategories">5</div>
                    <div class="stat-change"><i class="fas fa-layer-group"></i> Loại dịch vụ</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Tạm ngưng</h3><div class="stat-icon red"><i class="fas fa-pause-circle"></i></div></div>
                    <div class="stat-number" id="statSuspended">0</div>
                    <div class="stat-change" style="color:var(--danger)"><i class="fas fa-exclamation-circle"></i> Không khả dụng</div>
                </div>
            </div>
            <div class="toolbar">
                <div class="search-box"><i class="fas fa-search"></i><input type="text" id="svcSearch" placeholder="Tìm kiếm dịch vụ..." oninput="svcPage=1;renderServices()"></div>
                <div class="filter-buttons">
                    <button class="filter-btn active" onclick="setSvcFilter('all',this)">Tất cả</button>
                    <button class="filter-btn" onclick="setSvcFilter('kham',this)">Khám & Chẩn đoán</button>
                    <button class="filter-btn" onclick="setSvcFilter('tham-my',this)">Thẩm mỹ</button>
                    <button class="filter-btn" onclick="setSvcFilter('chinh-nha',this)">Chỉnh nha</button>
                    <button class="filter-btn" onclick="setSvcFilter('phau-thuat',this)">Phẫu thuật</button>
                </div>
                <button class="btn-add" onclick="openServiceModal()"><i class="fas fa-plus"></i> Thêm dịch vụ</button>
            </div>
            <div class="service-grid" id="serviceGrid"></div>
            <div class="pagination" id="svcPagination"></div>
        </div>

        <!-- ==================== TAB: NHÂN SỰ ==================== -->
        <div class="tab-panel" id="panel-staff">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header"><h3>Tổng nhân sự</h3><div class="stat-icon"><i class="fas fa-users"></i></div></div>
                    <div class="stat-number" id="statTotalStaff">0</div>
                    <div class="stat-change"><i class="fas fa-chart-line"></i> Tất cả</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Bác sĩ</h3><div class="stat-icon green"><i class="fas fa-user-md"></i></div></div>
                    <div class="stat-number" id="statDoctors">0</div>
                    <div class="stat-change"><i class="fas fa-stethoscope"></i> Bác sĩ điều trị</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Nhân viên</h3><div class="stat-icon purple"><i class="fas fa-user-nurse"></i></div></div>
                    <div class="stat-number" id="statNurses">0</div>
                    <div class="stat-change"><i class="fas fa-hospital-user"></i> Hỗ trợ điều trị</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Đang làm việc</h3><div class="stat-icon orange"><i class="fas fa-circle"></i></div></div>
                    <div class="stat-number" id="statActiveStaff">0</div>
                    <div class="stat-change"><i class="fas fa-check"></i> Đang hoạt động</div>
                </div>
            </div>
            <div class="toolbar">
                <div class="search-box"><i class="fas fa-search"></i><input type="text" id="staffSearch" placeholder="Tìm kiếm nhân sự..." oninput="staffPage=1;renderStaff()"></div>
                <div class="filter-buttons">
                    <button class="filter-btn active" onclick="setStaffFilter('all',this)">Tất cả</button>
                    <button class="filter-btn" onclick="setStaffFilter('doctor',this)">Bác sĩ</button>
                    <button class="filter-btn" onclick="setStaffFilter('receptionist',this)">Lễ tân</button>
                </div>
                <button class="btn-add" onclick="openStaffModal()"><i class="fas fa-plus"></i> Thêm nhân sự</button>
            </div>
            <div class="table-container">
                <table>
                    <thead><tr><th>Mã NS</th><th>Họ tên</th><th>Chức vụ</th><th>Chuyên khoa / Học vị</th><th>Điện thoại</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
                    <tbody id="staffTableBody"></tbody>
                </table>
            </div>
            <div class="pagination" id="staffPagination"></div>
        </div>

        <!-- ==================== TAB: TÀI KHOẢN ==================== -->
        <div class="tab-panel" id="panel-accounts">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header"><h3>Tổng tài khoản</h3><div class="stat-icon"><i class="fas fa-id-card"></i></div></div>
                    <div class="stat-number" id="statTotalAccounts">0</div>
                    <div class="stat-change"><i class="fas fa-chart-line"></i> Tất cả</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Khách hàng</h3><div class="stat-icon green"><i class="fas fa-user"></i></div></div>
                    <div class="stat-number" id="statCustomers">0</div>
                    <div class="stat-change"><i class="fas fa-heart"></i> Bệnh nhân đăng ký</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Nhân viên / BS</h3><div class="stat-icon purple"><i class="fas fa-user-tie"></i></div></div>
                    <div class="stat-number" id="statStaffAcc">0</div>
                    <div class="stat-change"><i class="fas fa-building"></i> Tài khoản nội bộ</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Bị khóa</h3><div class="stat-icon red"><i class="fas fa-lock"></i></div></div>
                    <div class="stat-number" id="statLocked">0</div>
                    <div class="stat-change"><i class="fas fa-ban"></i> Đã vô hiệu hóa</div>
                </div>
            </div>
            <div class="toolbar">
                <div class="search-box"><i class="fas fa-search"></i><input type="text" id="accSearch" placeholder="Tìm kiếm tài khoản..." oninput="accPage=1;renderAccounts()"></div>
                <div class="filter-buttons">
                    <button class="filter-btn active" onclick="setAccFilter('all',this)">Tất cả</button>
                    <button class="filter-btn" onclick="setAccFilter('customer',this)">Khách hàng</button>
                    <button class="filter-btn" onclick="setAccFilter('doctor',this)">Bác sĩ</button>
                    <button class="filter-btn" onclick="setAccFilter('staff',this)">Nhân viên</button>
                    <button class="filter-btn" onclick="setAccFilter('admin',this)">Admin</button>
                </div>
                <button class="btn-add" onclick="openAccountModal()"><i class="fas fa-plus"></i> Thêm tài khoản</button>
            </div>
            <div class="table-container">
                <table>
                    <thead><tr><th>ID</th><th>Họ tên</th><th>Tên đăng nhập</th><th>Loại TK</th><th>Số điện thoại</th><th>Ngày tạo</th><th>Trạng thái</th><th>Thao tác</th></tr></thead>
                    <tbody id="accTableBody"></tbody>
                </table>
            </div>
            <div class="pagination" id="accPagination"></div>
        </div>

        <!-- ==================== TAB: DOANH THU ==================== -->
        <div class="tab-panel" id="panel-revenue">

            <!-- TOOLBAR LỌC THỐNG KÊ -->
            <div class="rev-filter-bar">
                <div class="rev-filter-modes">
                    <button class="rev-mode-btn active" id="revModeDay"   onclick="setRevMode('day')"  ><i class="fas fa-calendar-day"></i> Theo ngày</button>
                    <button class="rev-mode-btn"        id="revModeMonth" onclick="setRevMode('month')"><i class="fas fa-calendar-alt"></i> Theo tháng</button>
                    <button class="rev-mode-btn"        id="revModeYear"  onclick="setRevMode('year')" ><i class="fas fa-calendar"></i> Theo năm</button>
                </div>
                <div class="rev-filter-inputs">
                    <div id="revInputDay" class="rev-input-group">
                        <label>Từ ngày</label><input type="date" id="revDateFrom" value="2026-04-01">
                        <label>Đến ngày</label><input type="date" id="revDateTo" value="2026-04-29">
                    </div>
                    <div id="revInputMonth" class="rev-input-group" style="display:none">
                        <label>Tháng</label>
                        <select id="revMonthSel">
                            <option value="1">Tháng 1</option><option value="2">Tháng 2</option><option value="3">Tháng 3</option><option value="4" selected>Tháng 4</option>
                            <option value="5">Tháng 5</option><option value="6">Tháng 6</option><option value="7">Tháng 7</option><option value="8">Tháng 8</option>
                            <option value="9">Tháng 9</option><option value="10">Tháng 10</option><option value="11">Tháng 11</option><option value="12">Tháng 12</option>
                        </select>
                        <label>Năm</label>
                        <select id="revMonthYear"><option value="2025">2025</option><option value="2026" selected>2026</option></select>
                    </div>
                    <div id="revInputYear" class="rev-input-group" style="display:none">
                        <label>Năm</label>
                        <select id="revYearSel"><option value="2025">2025</option><option value="2026" selected>2026</option></select>
                    </div>
                </div>
                <button class="rev-search-btn" onclick="applyRevFilter()"><i class="fas fa-search"></i> Thống kê</button>
            </div>

            <!-- KẾT QUẢ LỌC -->
            <div class="rev-filter-result" id="revFilterResult" style="display:none">
                <div class="rev-fr-label" id="revFilterLabel"></div>
                <div class="rev-fr-stats">
                    <div class="rev-fr-card"><div class="rev-fr-val" id="revFrTotal">0</div><div class="rev-fr-sub">Tổng doanh thu</div></div>
                    <div class="rev-fr-card"><div class="rev-fr-val" id="revFrTxns">0</div><div class="rev-fr-sub">Số giao dịch</div></div>
                    <div class="rev-fr-card"><div class="rev-fr-val" id="revFrAppts">0</div><div class="rev-fr-sub">Lượt khám</div></div>
                    <div class="rev-fr-card"><div class="rev-fr-val" id="revFrAvg">0</div><div class="rev-fr-sub">Trung bình/GD</div></div>
                </div>
                <div class="rev-fr-chart" id="revFrChart"></div>
                <div style="overflow-x:auto;margin-top:16px">
                    <table class="rev-table">
                        <thead><tr><th>Mã GD</th><th>Ngày</th><th>Bệnh nhân</th><th>Dịch vụ</th><th>Bác sĩ</th><th>Hình thức</th><th style="text-align:right">Số tiền</th></tr></thead>
                        <tbody id="revFrTableBody"></tbody>
                    </table>
                </div>
            </div>

            <!-- 4 STATS CARDS GIỮ NGUYÊN -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-header"><h3>Doanh thu năm 2026</h3><div class="stat-icon green"><i class="fas fa-coins"></i></div></div>
                    <div class="stat-number" id="revTotalYear">0</div>
                    <div class="stat-change"><i class="fas fa-calendar"></i> Tổng cả năm</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Tháng gần nhất</h3><div class="stat-icon"><i class="fas fa-chart-bar"></i></div></div>
                    <div class="stat-number" id="revLastMonth">0</div>
                    <div class="stat-change" id="revLastMonthLabel"><i class="fas fa-calendar-alt"></i> Tháng 4/2026</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Tăng trưởng</h3><div class="stat-icon purple"><i class="fas fa-arrow-trend-up"></i></div></div>
                    <div class="stat-number" id="revGrowth">0%</div>
                    <div class="stat-change"><i class="fas fa-chart-line"></i> So tháng trước</div>
                </div>
                <div class="stat-card">
                    <div class="stat-header"><h3>Tổng lượt khám</h3><div class="stat-icon orange"><i class="fas fa-calendar-check"></i></div></div>
                    <div class="stat-number" id="revTotalAppt">0</div>
                    <div class="stat-change"><i class="fas fa-stethoscope"></i> Cả năm 2026</div>
                </div>
            </div>

            <!-- ROW 1: BIỂU ĐỒ CỘT + DONUT DANH MỤC -->
            <div class="rev-main-grid">

                <!-- BIỂU ĐỒ CỘT DOANH THU -->
                <div class="rev-panel rev-chart-panel">
                    <div class="rev-panel-header">
                        <div>
                            <div class="rev-panel-title"><i class="fas fa-chart-column"></i> Doanh thu theo tháng</div>
                            <div class="rev-panel-sub">Năm 2026 — đơn vị: triệu đồng</div>
                        </div>
                        <div class="rev-year-badge">2026</div>
                    </div>
                    <div class="rev-bar-chart" id="revenueBarChart"></div>
                    <div class="rev-bar-legend">
                        <span class="rev-legend-dot" style="background:var(--primary-color)"></span>
                        <span>Doanh thu (triệu đồng)</span>
                    </div>
                </div>

                <!-- DONUT: PHÂN BỔ THEO DANH MỤC -->
                <div class="rev-panel rev-donut-panel">
                    <div class="rev-panel-header">
                        <div>
                            <div class="rev-panel-title"><i class="fas fa-chart-pie"></i> Phân bổ doanh thu</div>
                            <div class="rev-panel-sub">Theo danh mục dịch vụ</div>
                        </div>
                    </div>
                    <div class="rev-donut-wrap">
                        <svg class="rev-donut-svg" viewBox="0 0 120 120" id="donutSvg"></svg>
                        <div class="rev-donut-center">
                            <div class="rev-donut-total" id="donutTotal">0</div>
                            <div class="rev-donut-label">Tổng năm</div>
                        </div>
                    </div>
                    <div class="rev-donut-legend" id="donutLegend"></div>
                </div>
            </div>

            <!-- ROW 2: TOP DỊCH VỤ + HÌNH THỨC THANH TOÁN -->
            <div class="rev-secondary-grid">

                <!-- TOP DỊCH VỤ DOANH THU CAO -->
                <div class="rev-panel">
                    <div class="rev-panel-header">
                        <div class="rev-panel-title"><i class="fas fa-trophy"></i> Top dịch vụ doanh thu cao</div>
                    </div>
                    <div class="rev-top-list" id="revTopServices"></div>
                </div>

                <!-- HÌNH THỨC THANH TOÁN -->
                <div class="rev-panel">
                    <div class="rev-panel-header">
                        <div class="rev-panel-title"><i class="fas fa-credit-card"></i> Hình thức thanh toán</div>
                    </div>
                    <div class="rev-payment-list" id="revPaymentMethods"></div>
                </div>

                <!-- DOANH THU THEO QUÝ -->
                <div class="rev-panel">
                    <div class="rev-panel-header">
                        <div class="rev-panel-title"><i class="fas fa-calendar-days"></i> Doanh thu theo quý</div>
                    </div>
                    <div class="rev-quarter-list" id="revQuarters"></div>
                </div>
            </div>

            <!-- ROW 3: BẢNG GIAO DỊCH GẦN ĐÂY -->
            <div class="rev-panel rev-table-panel">
                <div class="rev-panel-header">
                    <div>
                        <div class="rev-panel-title"><i class="fas fa-receipt"></i> Giao dịch gần đây</div>
                        <div class="rev-panel-sub">10 giao dịch mới nhất</div>
                    </div>
                    <button class="rev-export-btn" onclick="showToast('Đang xuất báo cáo...')">
                        <i class="fas fa-download"></i> Xuất Excel
                    </button>
                </div>
                <div style="overflow-x:auto;">
                    <table class="rev-table">
                        <thead>
                            <tr>
                                <th>Mã GD</th>
                                <th>Ngày</th>
                                <th>Bệnh nhân</th>
                                <th>Dịch vụ</th>
                                <th>Bác sĩ</th>
                                <th>Hình thức</th>
                                <th style="text-align:right">Số tiền</th>
                            </tr>
                        </thead>
                        <tbody id="revTableBody"></tbody>
                    </table>
                </div>
            </div>

        </div>



    <!-- MODAL: DỊCH VỤ -->
    <div id="serviceModal" class="modal">
        <div class="modal-content">
            <div class="modal-header"><h3 id="serviceModalTitle">Thêm dịch vụ mới</h3><span class="close" onclick="closeServiceModal()">&times;</span></div>
            <div class="modal-body">
                <input type="hidden" id="svcId">
                <div class="form-row">
                    <div class="form-group"><label>Tên dịch vụ *</label><input type="text" id="svcName" placeholder="Nhập tên dịch vụ"></div>
                    <div class="form-group"><label>Danh mục *</label>
                        <select id="svcCat">
                            <option value="kham">Khám & Chẩn đoán</option>
                            <option value="tham-my">Thẩm mỹ</option>
                            <option value="chinh-nha">Chỉnh nha</option>
                            <option value="phau-thuat">Phẫu thuật</option>
                            <option value="tre-em">Trẻ em</option>
                        </select>
                    </div>
                </div>
                <div class="form-group"><label>Mô tả</label><textarea id="svcDesc" rows="2" placeholder="Mô tả ngắn về dịch vụ..."></textarea></div>
                <div class="form-row">
                    <div class="form-group"><label>Đơn giá (VNĐ) *</label><input type="number" id="svcPrice" placeholder="VD: 300000" min="0"></div>
                    <div class="form-group"><label>Thời gian thực hiện</label><input type="text" id="svcTime" placeholder="VD: 30 phút"></div>
                </div>
                <div class="form-row">
                    <div class="form-group"><label>Tính theo đơn vị?</label>
                        <select id="svcPerUnit">
                            <option value="false">Không (tính theo lần)</option>
                            <option value="true">Có (tính theo răng/trụ...)</option>
                        </select>
                    </div>
                    <div class="form-group"><label>Đơn vị (nếu có)</label><input type="text" id="svcUnit" placeholder="VD: răng, trụ"></div>
                </div>
                <div class="form-group"><label>Trạng thái</label>
                    <select id="svcStatus"><option value="active">Đang hoạt động</option><option value="inactive">Tạm ngưng</option></select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel" onclick="closeServiceModal()">Hủy</button>
                <button class="btn-save" onclick="saveService()">Lưu dịch vụ</button>
            </div>
        </div>
    </div>

    <!-- MODAL: NHÂN SỰ -->
    <div id="staffModal" class="modal">
        <div class="modal-content">
            <div class="modal-header"><h3 id="staffModalTitle">Thêm nhân sự mới</h3><span class="close" onclick="closeStaffModal()">&times;</span></div>
            <div class="modal-body">
                <input type="hidden" id="staffId">
                <div class="form-row">
                    <div class="form-group"><label>Họ và tên *</label><input type="text" id="staffName" placeholder="Nhập họ tên"></div>
                    <div class="form-group"><label>Chức vụ *</label>
                        <select id="staffRole">
                            <option value="doctor">Bác sĩ</option>
                            <option value="receptionist">Lễ tân</option>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group"><label>Chuyên khoa</label>
                        <select id="staffSpecialty">
                            <option value="">-- Chọn chuyên khoa --</option>
                            <option value="Răng tổng quát">Răng tổng quát</option>
                            <option value="Chỉnh nha">Chỉnh nha</option>
                            <option value="Phục hình">Phục hình</option>
                            <option value="Thẩm mỹ nha">Thẩm mỹ nha</option>
                            <option value="Phẫu thuật miệng">Phẫu thuật miệng</option>
                            <option value="Nha chu">Nha chu</option>
                            <option value="Răng trẻ em">Răng trẻ em</option>
                            <option value="Tiếp nhận bệnh nhân">Tiếp nhận bệnh nhân</option>
                            <option value="Kỹ thuật phục hình">Kỹ thuật phục hình</option>
                            <option value="Kỹ thuật chỉnh nha">Kỹ thuật chỉnh nha</option>
                        </select>
                    </div>
                    <div class="form-group"><label>Bằng cấp / Học vị</label><input type="text" id="staffDegree" placeholder="VD: CKI, Thạc sĩ, Tiến sĩ"></div>
                </div>
                <div class="form-row">
                    <div class="form-group"><label>Số điện thoại *</label><input type="tel" id="staffPhone" placeholder="Nhập số điện thoại"></div>
                    <div class="form-group"><label>Ngày bắt đầu làm</label><input type="date" id="staffStartDate"></div>
                </div>
                <div class="form-group"><label>Trạng thái</label>
                    <select id="staffStatus">
                        <option value="active">Đang làm việc</option>
                        <option value="inactive">Nghỉ việc</option>
                        <option value="leave">Nghỉ phép</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel" onclick="closeStaffModal()">Hủy</button>
                <button class="btn-save" onclick="saveStaff()">Lưu nhân sự</button>
            </div>
        </div>
    </div>

    <!-- MODAL: TÀI KHOẢN -->
    <div id="accountModal" class="modal">
        <div class="modal-content">
            <div class="modal-header"><h3 id="accModalTitle">Thêm tài khoản mới</h3><span class="close" onclick="closeAccountModal()">&times;</span></div>
            <div class="modal-body">
                <input type="hidden" id="accId">
                <div class="form-row">
                    <div class="form-group"><label>Họ và tên *</label><input type="text" id="accName" placeholder="Nhập họ tên"></div>
                    <div class="form-group"><label>Loại tài khoản *</label>
                        <select id="accRole">
                            <option value="customer">Khách hàng</option>
                            <option value="doctor">Bác sĩ</option>
                            <option value="staff">Nhân viên</option>
                            <option value="admin">Admin</option>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group"><label>Số điện thoại *</label><input type="tel" id="accPhone" placeholder="Nhập số điện thoại"></div>
                    <div class="form-group"><label>Mật khẩu *</label><input type="password" id="accPassword" placeholder="Nhập mật khẩu"></div>
                </div>
                <div class="form-group"><label>Trạng thái</label>
                    <select id="accStatus"><option value="active">Đang hoạt động</option><option value="inactive">Bị khóa</option></select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel" onclick="closeAccountModal()">Hủy</button>
                <button class="btn-save" onclick="saveAccount()">Lưu tài khoản</button>
            </div>
        </div>
    </div>

    <div class="footer">
        <p><i class="fas fa-tooth"></i> NHA KHOA 5AE - Chất lượng tạo niềm tin &nbsp;|&nbsp; © 2026 Hệ thống quản trị Admin</p>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</body>
</html>
