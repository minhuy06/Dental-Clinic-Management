<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nha Khoa 5AE - Báo Cáo Doanh Thu</title>
    <link href="https://fonts.googleapis.com/css2?family=Inter:opsz,wght@14..32,300;400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/baocao.css">
    <!-- Chart.js - ĐẶT Ở ĐẦU ĐỂ ĐẢM BẢO TẢI TRƯỚC -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>
</head>
<body>
    <!-- ==================== HEADER ==================== -->
    <div class="header">
        <div class="logo">
            <div class="logo-icon">
                <i class="fas fa-tooth"></i>
            </div>
            <div class="logo-text">
                <h1>NHA KHOA 5AE</h1>
                <p>HỆ THỐNG BÁO CÁO DOANH THU</p>
            </div>
        </div>
        <ul class="nav-menu">
            <li><a href="index.jsp">Lịch hẹn</a></li>
            <li><a href="benhnhan.jsp">Bệnh nhân</a></li>
            <li><a href="baocao.jsp" class="active">Báo cáo</a></li>
            <li><a href="cskh.jsp">CSKH</a></li>
        </ul>
        <div class="user-info">
            <i class="fas fa-bell" style="color: #bfdbfe; cursor: pointer;"></i>
            <div class="avatar">
                <i class="fas fa-user" style="color: white;"></i>
            </div>
        </div>
    </div>

    <div class="container">
        <!-- ==================== STATS CARDS ==================== -->
        <div class="stats-grid">
            <div class="stat-card">
                <div class="stat-icon" style="background: #eef2ff;">
                    <i class="fas fa-wallet"></i>
                </div>
                <div class="stat-info">
                    <h3>Tổng doanh thu</h3>
                    <p id="totalRevenue">0đ</p>
                    <span class="trend up"><i class="fas fa-arrow-up"></i> +15.2%</span>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background: #dcfce7;">
                    <i class="fas fa-users"></i>
                </div>
                <div class="stat-info">
                    <h3>Khách hàng mới</h3>
                    <p id="newCustomers">0</p>
                    <span class="trend up"><i class="fas fa-arrow-up"></i> +8.5%</span>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background: #fef3c7;">
                    <i class="fas fa-calendar-check"></i>
                </div>
                <div class="stat-info">
                    <h3>Lịch hẹn hoàn thành</h3>
                    <p id="completedAppointments">0</p>
                    <span class="trend up"><i class="fas fa-arrow-up"></i> +12.3%</span>
                </div>
            </div>
            <div class="stat-card">
                <div class="stat-icon" style="background: #fee2e2;">
                    <i class="fas fa-chart-line"></i>
                </div>
                <div class="stat-info">
                    <h3>Tăng trưởng</h3>
                    <p id="growth">0%</p>
                    <span class="trend up"><i class="fas fa-arrow-up"></i> so với tháng trước</span>
                </div>
            </div>
        </div>

        <!-- ==================== TOOLBAR ==================== -->
        <div class="toolbar">
            <div class="date-range">
                <div class="date-picker-group">
                    <label>Từ ngày</label>
                    <input type="date" id="startDate" class="date-input">
                </div>
                <div class="date-picker-group">
                    <label>Đến ngày</label>
                    <input type="date" id="endDate" class="date-input">
                </div>
                <button class="btn-apply" id="applyDateBtn"><i class="fas fa-search"></i> Áp dụng</button>
            </div>
            <div class="filter-buttons">
                <button class="filter-btn" data-filter="today">Hôm nay</button>
                <button class="filter-btn active" data-filter="week">Tuần này</button>
                <button class="filter-btn" data-filter="month">Tháng này</button>
                <button class="filter-btn" data-filter="year">Năm nay</button>
            </div>
            <button class="btn-export" id="exportExcelBtn"><i class="fas fa-file-excel"></i> Xuất Excel</button>
        </div>

        <!-- ==================== CHART & SERVICES ==================== -->
        <div class="report-grid">
            <!-- Biểu đồ doanh thu -->
            <div class="card-panel">
                <div class="panel-header">
                    <h4><i class="fas fa-chart-line"></i> Doanh thu theo ngày</h4>
                    <div class="chart-controls">
                        <button class="chart-btn active" data-chart="bar"><i class="fas fa-chart-bar"></i></button>
                        <button class="chart-btn" data-chart="line"><i class="fas fa-chart-line"></i></button>
                    </div>
                </div>
                <div class="chart-container">
                    <canvas id="revenueChart" width="400" height="200" style="width:100%; height:250px;"></canvas>
                </div>
            </div>

            <!-- Dịch vụ phổ biến -->
            <div class="card-panel">
                <div class="panel-header">
                    <h4><i class="fas fa-chart-pie"></i> Dịch vụ phổ biến</h4>
                </div>
                <div class="services-list" id="servicesList"></div>
            </div>
        </div>

        <!-- ==================== PHƯƠNG THỨC THANH TOÁN ==================== -->
        <div class="payment-methods">
            <div class="card-panel">
                <div class="panel-header">
                    <h4><i class="fas fa-credit-card"></i> Phương thức thanh toán</h4>
                </div>
                <div class="payment-grid" id="paymentMethods"></div>
            </div>
        </div>

        <!-- ==================== BẢNG GIAO DỊCH ==================== -->
        <div class="card-panel">
            <div class="panel-header">
                <h4><i class="fas fa-receipt"></i> Giao dịch thanh toán gần nhất</h4>
                <div class="table-controls">
                    <input type="text" id="searchTransaction" placeholder="🔍 Tìm kiếm...">
                </div>
            </div>
            <div class="table-container">
                <table id="transactionTable">
                    <thead>
                        <tr>
                            <th>Mã đơn</th>
                            <th>Khách hàng</th>
                            <th>Dịch vụ</th>
                            <th>Ngày</th>
                            <th>PT Thanh toán</th>
                            <th>Số tiền</th>
                        </tr>
                    </thead>
                    <tbody id="transactionBody"></tbody>
                </table>
            </div>
            <div class="pagination" id="pagination"></div>
        </div>
    </div>

    <!-- ==================== FOOTER ==================== -->
    <div class="footer">
        <p><i class="fas fa-chart-line"></i> NHA KHOA 5AE - Báo cáo doanh thu chuyên nghiệp</p>
        <p>© 2024 Nha Khoa 5AE - Hệ thống quản lý doanh thu thông minh</p>
    </div>

    <!-- File JS của bạn - ĐẶT SAU Chart.js -->
    <script src="${pageContext.request.contextPath}/js/baocao.js"></script>
</body>
</html>