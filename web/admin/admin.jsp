<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nha Khoa 5AE - Quản Trị Hệ Thống</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:opsz,wght@14..32,300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/admin.css">
</head>
<body>

    <!-- ==================== HEADER ==================== -->
    <div class="header">
        <div class="logo">
            <div class="logo-icon"><i class="fas fa-tooth"></i></div>
            <div class="logo-text">
                <h1>NHA KHOA 5AE</h1>
                <p>HỆ THỐNG QUẢN TRỊ</p>
            </div>
        </div>
        <ul class="nav-menu">
            <li><a href="#" class="active" onclick="switchTab('services',this);return false;"><i class="fas fa-tooth"></i> Quản lý dịch vụ</a></li>
            <li><a href="#" onclick="switchTab('staff',this);return false;"><i class="fas fa-user-md"></i> Quản lý nhân sự</a></li>
            <li><a href="#" onclick="switchTab('accounts',this);return false;"><i class="fas fa-users-cog"></i> Quản lý tài khoản</a></li>
        </ul>
        <div class="user-info">
            <i class="fas fa-bell" style="color:#bfdbfe;cursor:pointer;"></i>
            <div class="avatar"><i class="fas fa-user-shield" style="color:white;"></i></div>
        </div>
    </div>

    <div class="container">

        <!-- ==================== TAB: DỊCH VỤ ==================== -->
        <div class="tab-panel active" id="panel-services">

            <!-- Stats -->
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
                    <div class="stat-header"><h3>Giá trung bình</h3><div class="stat-icon orange"><i class="fas fa-money-bill-wave"></i></div></div>
                    <div class="stat-number" id="statAvgPrice">0</div>
                    <div class="stat-change" style="font-size:0.65rem;">nghìn đồng / dịch vụ</div>
                </div>
            </div>

            <!-- Toolbar -->
            <div class="toolbar">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="svcSearch" placeholder="Tìm kiếm dịch vụ...">
                </div>
                <div class="filter-buttons">
                    <button class="filter-btn active" data-svc-filter="all">Tất cả</button>
                    <button class="filter-btn" data-svc-filter="kham">Khám & Chẩn đoán</button>
                    <button class="filter-btn" data-svc-filter="tham-my">Thẩm mỹ</button>
                    <button class="filter-btn" data-svc-filter="chinh-nha">Chỉnh nha</button>
                    <button class="filter-btn" data-svc-filter="phau-thuat">Phẫu thuật</button>
                </div>
                <button class="btn-add" onclick="openServiceModal()">
                    <i class="fas fa-plus"></i> Thêm dịch vụ
                </button>
            </div>

            <!-- Service cards -->
            <div class="service-grid" id="serviceGrid"></div>
            <div class="pagination" id="svcPagination"></div>
        </div>

        <!-- ==================== TAB: NHÂN SỰ ==================== -->
        <div class="tab-panel" id="panel-staff">

            <!-- Stats -->
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
                    <div class="stat-header"><h3>Đang hoạt động</h3><div class="stat-icon orange"><i class="fas fa-circle"></i></div></div>
                    <div class="stat-number" id="statActiveStaff">0</div>
                    <div class="stat-change"><i class="fas fa-check"></i> Đang làm việc</div>
                </div>
            </div>

            <!-- Toolbar -->
            <div class="toolbar">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="staffSearch" placeholder="Tìm kiếm nhân sự...">
                </div>
                <div class="filter-buttons">
                    <button class="filter-btn active" data-staff-filter="all">Tất cả</button>
                    <button class="filter-btn" data-staff-filter="doctor">Bác sĩ</button>
                    <button class="filter-btn" data-staff-filter="nurse">Điều dưỡng</button>
                    <button class="filter-btn" data-staff-filter="receptionist">Lễ tân</button>
                </div>
                <button class="btn-add" onclick="openStaffModal()">
                    <i class="fas fa-plus"></i> Thêm nhân sự
                </button>
            </div>

            <!-- Table -->
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Mã NS</th>
                            <th>Họ tên</th>
                            <th>Chức vụ</th>
                            <th>Chuyên khoa</th>
                            <th>Số điện thoại</th>
                            <th>Email</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="staffTableBody"></tbody>
                </table>
            </div>
            <div class="pagination" id="staffPagination"></div>
        </div>

        <!-- ==================== TAB: TÀI KHOẢN ==================== -->
        <div class="tab-panel" id="panel-accounts">

            <!-- Stats -->
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

            <!-- Toolbar -->
            <div class="toolbar">
                <div class="search-box">
                    <i class="fas fa-search"></i>
                    <input type="text" id="accSearch" placeholder="Tìm kiếm tài khoản...">
                </div>
                <div class="filter-buttons">
                    <button class="filter-btn active" data-acc-filter="all">Tất cả</button>
                    <button class="filter-btn" data-acc-filter="customer">Khách hàng</button>
                    <button class="filter-btn" data-acc-filter="doctor">Bác sĩ</button>
                    <button class="filter-btn" data-acc-filter="staff">Nhân viên</button>
                    <button class="filter-btn" data-acc-filter="admin">Admin</button>
                </div>
                <button class="btn-add" onclick="openAccountModal()">
                    <i class="fas fa-plus"></i> Thêm tài khoản
                </button>
            </div>

            <!-- Table -->
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Họ tên</th>
                            <th>Tên đăng nhập</th>
                            <th>Loại tài khoản</th>
                            <th>SĐT / Email</th>
                            <th>Ngày tạo</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="accTableBody"></tbody>
                </table>
            </div>
            <div class="pagination" id="accPagination"></div>
        </div>

    </div><!-- /container -->

    <!-- ==================== MODAL: DỊCH VỤ ==================== -->
    <div id="serviceModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="serviceModalTitle">Thêm dịch vụ mới</h3>
                <span class="close" onclick="closeServiceModal()">&times;</span>
            </div>
            <div class="modal-body">
                <input type="hidden" id="svcId">
                <div class="form-row">
                    <div class="form-group">
                        <label>Tên dịch vụ *</label>
                        <input type="text" id="svcName" placeholder="Nhập tên dịch vụ">
                    </div>
                    <div class="form-group">
                        <label>Danh mục *</label>
                        <select id="svcCat">
                            <option value="kham">Khám & Chẩn đoán</option>
                            <option value="tham-my">Thẩm mỹ</option>
                            <option value="chinh-nha">Chỉnh nha</option>
                            <option value="phau-thuat">Phẫu thuật</option>
                            <option value="tre-em">Trẻ em</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label>Mô tả</label>
                    <textarea id="svcDesc" rows="2" placeholder="Mô tả ngắn về dịch vụ..."></textarea>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Đơn giá (VNĐ) *</label>
                        <input type="number" id="svcPrice" placeholder="VD: 300000" min="0">
                    </div>
                    <div class="form-group">
                        <label>Thời gian thực hiện</label>
                        <input type="text" id="svcTime" placeholder="VD: 30 phút">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Tính theo đơn vị?</label>
                        <select id="svcPerUnit">
                            <option value="false">Không (tính theo lần)</option>
                            <option value="true">Có (tính theo răng/trụ...)</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Đơn vị (nếu có)</label>
                        <input type="text" id="svcUnit" placeholder="VD: răng, trụ">
                    </div>
                </div>
                <div class="form-group">
                    <label>Trạng thái</label>
                    <select id="svcStatus">
                        <option value="active">Đang hoạt động</option>
                        <option value="inactive">Tạm ngưng</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel" onclick="closeServiceModal()">Hủy</button>
                <button class="btn-save" onclick="saveService()">Lưu dịch vụ</button>
            </div>
        </div>
    </div>

    <!-- ==================== MODAL: NHÂN SỰ ==================== -->
    <div id="staffModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="staffModalTitle">Thêm nhân sự mới</h3>
                <span class="close" onclick="closeStaffModal()">&times;</span>
            </div>
            <div class="modal-body">
                <input type="hidden" id="staffId">
                <div class="form-row">
                    <div class="form-group">
                        <label>Họ và tên *</label>
                        <input type="text" id="staffName" placeholder="Nhập họ tên">
                    </div>
                    <div class="form-group">
                        <label>Chức vụ *</label>
                        <select id="staffRole">
                            <option value="doctor">Bác sĩ</option>
                            <option value="nurse">Điều dưỡng</option>
                            <option value="receptionist">Lễ tân</option>
                            <option value="technician">Kỹ thuật viên</option>
                        </select>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Chuyên khoa</label>
                        <input type="text" id="staffSpecialty" placeholder="VD: Chỉnh nha, Phục hình...">
                    </div>
                    <div class="form-group">
                        <label>Bằng cấp / Học vị</label>
                        <input type="text" id="staffDegree" placeholder="VD: CKI, Thạc sĩ, Tiến sĩ">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Số điện thoại *</label>
                        <input type="tel" id="staffPhone" placeholder="Nhập số điện thoại">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="staffEmail" placeholder="Nhập email">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Ngày bắt đầu làm</label>
                        <input type="date" id="staffStartDate">
                    </div>
                    <div class="form-group">
                        <label>Trạng thái</label>
                        <select id="staffStatus">
                            <option value="active">Đang làm việc</option>
                            <option value="inactive">Nghỉ việc</option>
                            <option value="leave">Nghỉ phép</option>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label>Ghi chú</label>
                    <textarea id="staffNotes" rows="2" placeholder="Ghi chú thêm..."></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel" onclick="closeStaffModal()">Hủy</button>
                <button class="btn-save" onclick="saveStaff()">Lưu nhân sự</button>
            </div>
        </div>
    </div>

    <!-- ==================== MODAL: TÀI KHOẢN ==================== -->
    <div id="accountModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="accModalTitle">Thêm tài khoản mới</h3>
                <span class="close" onclick="closeAccountModal()">&times;</span>
            </div>
            <div class="modal-body">
                <input type="hidden" id="accId">
                <div class="form-row">
                    <div class="form-group">
                        <label>Họ và tên *</label>
                        <input type="text" id="accName" placeholder="Nhập họ tên">
                    </div>
                    <div class="form-group">
                        <label>Tên đăng nhập *</label>
                        <input type="text" id="accUsername" placeholder="Nhập tên đăng nhập">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Loại tài khoản *</label>
                        <select id="accRole">
                            <option value="customer">Khách hàng</option>
                            <option value="doctor">Bác sĩ</option>
                            <option value="staff">Nhân viên</option>
                            <option value="admin">Admin</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu *</label>
                        <input type="password" id="accPassword" placeholder="Nhập mật khẩu">
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Số điện thoại</label>
                        <input type="tel" id="accPhone" placeholder="Nhập số điện thoại">
                    </div>
                    <div class="form-group">
                        <label>Email</label>
                        <input type="email" id="accEmail" placeholder="Nhập email">
                    </div>
                </div>
                <div class="form-group">
                    <label>Trạng thái</label>
                    <select id="accStatus">
                        <option value="active">Đang hoạt động</option>
                        <option value="inactive">Bị khóa</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel" onclick="closeAccountModal()">Hủy</button>
                <button class="btn-save" onclick="saveAccount()">Lưu tài khoản</button>
            </div>
        </div>
    </div>

    <!-- ==================== FOOTER ==================== -->
    <div class="footer">
        <p><i class="fas fa-tooth"></i> NHA KHOA 5AE - Chất lượng tạo niềm tin</p>
        <p style="font-size:12px;margin-top:5px;">© 2024 Nha Khoa 5AE - Hệ thống quản trị Admin</p>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
</body>
</html>
