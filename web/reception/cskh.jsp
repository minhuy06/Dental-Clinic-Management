<%-- 
    Document   : cskh
    Created on : Apr 20, 2026, 7:30:04 PM
    Author     : AHieu
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nha Khoa 5AE - Chăm Sóc Khách Hàng</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:opsz,wght@14..32,300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cskh.css">
</head>
<body>
    <!-- ==================== HEADER ==================== -->
    <jsp:include page="components/header.jsp"/>

    <div class="container">
        <!-- ==================== STATS CARDS ==================== -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon" style="background: #eef2ff;">
                    <i class="fas fa-headset"></i>
                </div>
                <div class="stat-info">
                    <h3>Tổng CSKH</h3>
                    <p id="totalStaff">0</p>
                    <span class="trend up"><i class="fas fa-arrow-up"></i> Đang hoạt động</span>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background: #dcfce7;">
                    <i class="fas fa-message"></i>
                </div>
                <div class="stat-info">
                    <h3>Tin nhắn hôm nay</h3>
                    <p id="todayMessages">0</p>
                    <span class="trend up"><i class="fas fa-arrow-up"></i> +12 tin nhắn</span>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background: #fef3c7;">
                    <i class="fas fa-phone-alt"></i>
                </div>
                <div class="stat-info">
                    <h3>Cuộc gọi hôm nay</h3>
                    <p id="todayCalls">0</p>
                    <span class="trend up"><i class="fas fa-arrow-up"></i> +8 cuộc gọi</span>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background: #fee2e2;">
                    <i class="fas fa-star"></i>
                </div>
                <div class="stat-info">
                    <h3>Đánh giá trung bình</h3>
                    <p id="avgRating">0</p>
                    <span class="trend up"><i class="fas fa-star" style="color: #fbbf24;"></i> 5 sao</span>
                </div>
            </div>
        </div>

        <!-- ==================== TOOLBAR ==================== -->
        <div class="toolbar">
            <div class="search-box">
                <i class="fas fa-search"></i>
                <input type="text" id="searchInput" placeholder="Tìm kiếm theo tên, số điện thoại, email...">
            </div>
            <div class="filter-buttons">
                <button class="filter-btn active" data-filter="all">Tất cả</button>
                <button class="filter-btn" data-filter="active">Đang hoạt động</button>
                <button class="filter-btn" data-filter="offline">Offline</button>
                <button class="filter-btn" data-filter="busy">Đang bận</button>
            </div>
            <button class="btn-add" id="openAddBtn">
                <i class="fas fa-plus"></i> Thêm CSKH
            </button>
        </div>

        <!-- ==================== DANH SÁCH CSKH ==================== -->
        <div class="staff-grid" id="staffGrid">
            <!-- Dữ liệu sẽ được load bằng JS -->
        </div>

        <!-- ==================== PAGINATION ==================== -->
        <div class="pagination" id="pagination"></div>

        <!-- ==================== TIN NHẮN GẦN ĐÂY ==================== -->
        <div class="card-panel">
            <div class="panel-header">
                <h4><i class="fas fa-comment-dots"></i> Tin nhắn gần đây</h4>
                <button class="btn-refresh" id="refreshMessages"><i class="fas fa-sync-alt"></i></button>
            </div>
            <div class="messages-list" id="messagesList"></div>
        </div>
    </div>

    <!-- ==================== MODAL THÊM/SỬA CSKH ==================== -->
    <div id="staffModal" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="modalTitle">Thêm nhân viên CSKH mới</h3>
                <span class="close">&times;</span>
            </div>
            <div class="modal-body">
                <form id="staffForm">
                    <input type="hidden" id="staffId">
                    <div class="form-row">
                        <div class="form-group">
                            <label>Họ tên *</label>
                            <input type="text" id="fullName" required placeholder="Nhập họ tên">
                        </div>
                        <div class="form-group">
                            <label>Số điện thoại *</label>
                            <input type="tel" id="phone" required placeholder="Nhập số điện thoại">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Email *</label>
                            <input type="email" id="email" required placeholder="Nhập email">
                        </div>
                        <div class="form-group">
                            <label>Chức vụ</label>
                            <select id="position">
                                <option value="Chăm sóc khách hàng">Chăm sóc khách hàng</option>
                                <option value="Trưởng phòng CSKH">Trưởng phòng CSKH</option>
                                <option value="Tư vấn viên">Tư vấn viên</option>
                                <option value="Xử lý khiếu nại">Xử lý khiếu nại</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Ngày sinh</label>
                            <input type="date" id="birthDate">
                        </div>
                        <div class="form-group">
                            <label>Trạng thái</label>
                            <select id="status">
                                <option value="active">Đang hoạt động</option>
                                <option value="offline">Offline</option>
                                <option value="busy">Đang bận</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Ghi chú</label>
                        <textarea id="notes" rows="2" placeholder="Ghi chú thêm..."></textarea>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel">Hủy</button>
                <button class="btn-save">Lưu lại</button>
            </div>
        </div>
    </div>

    <!-- ==================== MODAL GỬI TIN NHẮN ==================== -->
    <div id="messageModal" class="modal">
        <div class="modal-content" style="max-width: 500px;">
            <div class="modal-header">
                <h3>Gửi tin nhắn</h3>
                <span class="close-msg">&times;</span>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <label>Đến:</label>
                    <input type="text" id="receiverName" readonly style="background: #f3f4f6;">
                </div>
                <div class="form-group">
                    <label>Nội dung tin nhắn</label>
                    <textarea id="messageContent" rows="4" placeholder="Nhập nội dung tin nhắn..."></textarea>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn-cancel">Hủy</button>
                <button class="btn-save" id="sendMessageBtn">Gửi tin nhắn</button>
            </div>
        </div>
    </div>

    <!-- ==================== FOOTER ==================== -->
    <div class="footer">
        <p><i class="fas fa-headset"></i> NHA KHOA 5AE - Chăm sóc khách hàng chuyên nghiệp</p>
        <p>© 2024 Nha Khoa 5AE - Tổng đài CSKH: 1900 1234</p>
    </div>

    <script src="${pageContext.request.contextPath}/assets/js/cskh.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/dynamicStyle.js"></script>
</body>
</html>