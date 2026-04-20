<%-- 
    Document   : benhnha
    Created on : Apr 20, 2026, 7:21:53 PM
    Author     : AHieu
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nha Khoa 5AE - Quản Lý Bệnh Nhân</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:opsz,wght@14..32,300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/benhnhan.css">
</head>
<body>
    <!-- ==================== HEADER (GIỐNG TRANG LỊCH HẸN) ==================== -->
    <jsp:include page="components/header.jsp"/>

    <div class="container">
        <!-- ==================== STATS CARDS ==================== -->
        <div class="stats-grid">
            <div class="stat-card" onclick="filterByStatus('all')" id="statAll">
                <div class="stat-header">
                    <h3>Tổng bệnh nhân</h3>
                    <div class="stat-icon"><i class="fas fa-users"></i></div>
                </div>
                <div class="stat-number" id="totalPatients">0</div>
                <div class="stat-change"><i class="fas fa-chart-line"></i> Tất cả bệnh nhân</div>
            </div>
            <div class="stat-card" onclick="filterByStatus('active')" id="statActive">
                <div class="stat-header">
                    <h3>Đang điều trị</h3>
                    <div class="stat-icon"><i class="fas fa-stethoscope"></i></div>
                </div>
                <div class="stat-number" id="activePatients">0</div>
                <div class="stat-change"><i class="fas fa-check"></i> Đang điều trị</div>
            </div>
            <div class="stat-card" onclick="filterByStatus('completed')" id="statCompleted">
                <div class="stat-header">
                    <h3>Hoàn thành</h3>
                    <div class="stat-icon"><i class="fas fa-check-double"></i></div>
                </div>
                <div class="stat-number" id="completedPatients">0</div>
                <div class="stat-change"><i class="fas fa-trophy"></i> Đã kết thúc</div>
            </div>
            <div class="stat-card" onclick="filterByStatus('followup')" id="statFollowup">
                <div class="stat-header">
                    <h3>Tái khám</h3>
                    <div class="stat-icon"><i class="fas fa-calendar-check"></i></div>
                </div>
                <div class="stat-number" id="followupPatients">0</div>
                <div class="stat-change"><i class="fas fa-bell"></i> Cần tái khám</div>
            </div>
        </div>

        <!-- ==================== TOOLBAR ==================== -->
        <div class="toolbar">
            <div class="search-box">
                <i class="fas fa-search"></i>
                <input type="text" id="searchInput" placeholder="Tìm kiếm theo tên, số điện thoại, mã bệnh nhân...">
            </div>
            <div class="filter-buttons">
                <button class="filter-btn active" data-filter="all">Tất cả</button>
                <button class="filter-btn" data-filter="active">Đang điều trị</button>
                <button class="filter-btn" data-filter="completed">Hoàn thành</button>
                <button class="filter-btn" data-filter="followup">Tái khám</button>
            </div>
            <button class="btn-add" id="openAddBtn">
                <i class="fas fa-plus"></i> Thêm bệnh nhân
            </button>
        </div>

        <!-- ==================== BẢNG BỆNH NHÂN ==================== -->
        <div class="table-container">
            <table id="patientTable">
                <thead>
                    <tr>
                        <th>Mã BN</th>
                        <th>Bệnh nhân</th>
                        <th>Giới tính</th>
                        <th>Số điện thoại</th>
                        <th>Dị ứng thuốc</th>
                        <th>Tiền sử bệnh</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody id="patientTableBody"></tbody>
            </table>
        </div>

        <!-- ==================== PAGINATION ==================== -->
        <div class="pagination" id="pagination"></div>
    </div>

    <!-- ==================== MODAL THÊM/SỬA BỆNH NHÂN ==================== -->
    <div id="patientModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="modalTitle">Thêm bệnh nhân mới</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <form id="patientForm">
                    <input type="hidden" id="patientId">
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Họ tên *</label>
                            <input type="text" id="fullName" required placeholder="Nhập họ tên">
                        </div>
                        <div class="form-group">
                            <label>Giới tính *</label>
                            <select id="gender" required>
                                <option value="Nam">Nam</option>
                                <option value="Nữ">Nữ</option>
                                <option value="Khác">Khác</option>
                            </select>
                        </div>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Số điện thoại *</label>
                            <input type="tel" id="phone" required placeholder="Nhập số điện thoại">
                        </div>
                        <div class="form-group">
                            <label>Ngày sinh</label>
                            <input type="date" id="birthDate">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label>Địa chỉ</label>
                        <input type="text" id="address" placeholder="Nhập địa chỉ">
                    </div>
                    
                    <div class="form-group">
                        <label><i class="fas fa-allergies"></i> Dị ứng thuốc</label>
                        <textarea id="allergy" rows="2" placeholder="Ví dụ: Penicillin, Amoxicillin, Paracetamol, ... (nếu không có thì để trống)"></textarea>
                        <small>Ghi rõ loại thuốc bệnh nhân bị dị ứng</small>
                    </div>
                    
                    <div class="form-group">
                        <label><i class="fas fa-notes-medical"></i> Tiền sử bệnh</label>
                        <textarea id="medicalHistory" rows="3" placeholder="Ví dụ: Cao huyết áp, Tiểu đường, Hen suyễn, Bệnh tim mạch, ..."></textarea>
                        <small>Ghi rõ các bệnh lý nền, bệnh mãn tính (nếu có)</small>
                    </div>
                    
                    <div class="form-row">
                        <div class="form-group">
                            <label>Trạng thái *</label>
                            <select id="status" required>
                                <option value="active">Đang điều trị</option>
                                <option value="completed">Đã hoàn thành</option>
                                <option value="followup">Hẹn tái khám</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Ngày đăng ký</label>
                            <input type="date" id="registerDate">
                        </div>
                    </div>
                    
                    <div class="form-group">
                        <label>Ghi chú</label>
                        <textarea id="notes" rows="2" placeholder="Ghi chú thêm về bệnh nhân..."></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel">Hủy</button>
                <button class="btn-save">Lưu lại</button>
            </div>
        </div>
    </div>

    <!-- ==================== FOOTER (GIỐNG TRANG LỊCH HẸN) ==================== -->
    <div class="footer">
        <p><i class="fas fa-tooth"></i> NHA KHOA 5AE - Chất lượng tạo niềm tin</p>
        <p style="font-size: 12px; margin-top: 5px;">© 2024 Nha Khoa 5AE - Hệ thống quản lý bệnh nhân chuyên nghiệp</p>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/benhnhan.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/dynamicStyle.js"></script>
</body>
</html>