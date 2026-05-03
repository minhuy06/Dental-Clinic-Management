<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phòng Khám Nha Khoa 5AE - Danh Sách Chờ Khám</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="css/danhsach.css">
    <style>
        /* Điều chỉnh lại layout do đã xóa sidebar */
        .main-container {
            display: block;
            padding: 20px;
        }
        .content {
            padding: 0;
        }
    </style>
</head>
<body>

    <header>
        <div class="header-left">
            <i class="fa-solid fa-tooth"></i>
            <h1>NHA KHOA 5AE</h1>
        </div>
        <div class="header-right">
            <div class="doctor-info">
                <i class="fa-solid fa-user-doctor"></i>
                <span>BS. Nguyễn Hoàng</span>
            </div>
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
    <script src="js/danhsach.js"></script>
</body>
</html>