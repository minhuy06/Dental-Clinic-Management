<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dentalclinic.model.TaiKhoan" %>
<%
    TaiKhoan loggedDoctor = (TaiKhoan) session.getAttribute("loggedInUser");
    String doctorHeaderName = "Bác sĩ";
    if (loggedDoctor != null && loggedDoctor.getHoTen() != null && !loggedDoctor.getHoTen().trim().isEmpty()) {
        doctorHeaderName = loggedDoctor.getHoTen().trim();
        if (!doctorHeaderName.toLowerCase().startsWith("bs")) {
            doctorHeaderName = "BS. " + doctorHeaderName;
        }
    }
%>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phòng Khám Nha Khoa 5AE - Danh Sách Chờ Khám</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/doctor/css/danhsach.css">
    <style>
        /* Điều chỉnh lại layout do đã xóa sidebar */
        .main-container {
            display: block;
            padding: 20px;
        }
        .content {
            padding: 0;
        }
        /* Nút xác nhận hoàn tất — xanh lá mạ (đảm bảo hiển thị kể cả cache CSS cũ) */
        .appointment-table .btn-complete {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 4px;
            background: linear-gradient(135deg, #bbf7d0 0%, #86efac 55%, #4ade80 100%) !important;
            color: #14532d !important;
            border: 1px solid #6ee7b7 !important;
            padding: 8px 14px !important;
            border-radius: 8px !important;
            cursor: pointer;
            font-size: 0.8rem !important;
            font-weight: 700 !important;
            margin-left: 6px;
            margin-top: 4px;
            line-height: 1.2;
            white-space: nowrap;
            box-shadow: 0 2px 6px rgba(74, 222, 128, 0.35);
            font-family: 'Inter', system-ui, sans-serif;
        }
        .appointment-table .btn-complete:hover {
            background: linear-gradient(135deg, #86efac 0%, #4ade80 100%) !important;
            color: #052e16 !important;
            transform: translateY(-1px);
        }
        .appointment-table td:last-child .btn-exam {
            margin-bottom: 4px;
        }
    </style>
</head>
<body>

    <header>
        <a href="${pageContext.request.contextPath}/" class="header-left" style="text-decoration:none;color:inherit;">
            <i class="fa-solid fa-tooth"></i>
            <h1>NHA KHOA 5AE</h1>
        </a>
        <div class="header-right">
            <a href="${pageContext.request.contextPath}/doctor/dashboard" class="doctor-info" style="text-decoration:none;color:inherit;" title="Trang bác sĩ">
                <i class="fa-solid fa-user-doctor"></i>
                <span><%= doctorHeaderName %></span>
            </a>
            <div class="date-time" id="currentDateTime"></div>
        </div>
    </header>

    <div class="main-container">
        <!-- Đã xóa toàn bộ sidebar (menu Lịch hẹn, Bệnh nhân, Báo cáo, CSKH) -->

        <main class="content">
            <!-- Filter bar: giữ đầy đủ tìm kiếm, lọc trạng thái, lọc bác sĩ, làm mới -->
            <div class="filter-bar">
                <div class="search-box">
                    <i class="fa-solid fa-search"></i>
                    <input type="text" id="searchInput" placeholder="Tìm kiếm bệnh nhân...">
                </div>
                <div class="filter-group">
                    <select id="statusFilter">
                        <option value="all">Tất cả trạng thái</option>
                        <option value="waiting">Đang chờ</option>
                        <option value="examining">Đang khám</option>
                        <option value="completed">Đã khám</option>
                        <option value="cancelled">Đã hủy</option>
                    </select>
                    
                    <button class="btn-refresh" id="refreshBtn"><i class="fa-solid fa-rotate-right"></i> Làm mới</button>
                </div>
            </div>

            <!-- Đã xóa 4 thẻ thống kê (stats-cards) -->

            <!-- Bảng danh sách bệnh nhân -->
            <div class="appointment-table-container">
                <table class="appointment-table">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Bệnh nhân</th>
                            <th>Giờ hẹn</th>
                            <th>Bác sĩ</th>
                            <th>Lý do</th>
                            <th>Trạng thái</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody id="appointmentTableBody">
                        <!-- Dữ liệu sẽ được JavaScript render -->
                    </tbody>
                </table>
            </div>
        </main>
    </div>

    <!-- Modal xác nhận (giữ nguyên) -->
    <div id="confirmModal" class="modal">
        <div class="modal-content">
            <h3><i class="fa-solid fa-triangle-exclamation"></i> Xác nhận</h3>
            <p id="modalMessage">Bạn có muốn bắt đầu khám cho bệnh nhân này không?</p>
            <div class="modal-buttons">
                <button class="btn-cancel-modal">Hủy</button>
                <button class="btn-confirm" id="confirmBtn">Đồng ý</button>
            </div>
        </div>
    </div>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/js/all.min.js"></script>
    <script>
        window.CONTEXT_PATH = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/doctor/js/danhsach.js?v=20260516g"></script>
</body>
</html>