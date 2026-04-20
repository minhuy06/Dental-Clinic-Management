/**
 * ==================== NHA KHOA 5AE - BÁO CÁO DOANH THU ====================
 * File: doanhthu.js
 * Chức năng: Xử lý biểu đồ, thống kê, bảng giao dịch
 * Cập nhật: Tháng 4/2026
 */

// ==================== DỮ LIỆU MẪU (CẬP NHẬT THÁNG 4/2026) ====================
let transactions = [
    { id: "HD-9921", customer: "Nguyễn Văn Hiển", service: "Nhổ răng khôn", date: "2026-04-20", method: "Chuyển khoản", amount: 2500000 },
    { id: "HD-9922", customer: "Trần Thị Thảo", service: "Lấy cao răng", date: "2026-04-20", method: "Tiền mặt", amount: 350000 },
    { id: "HD-9923", customer: "Lê Anh Nam", service: "Niềng răng", date: "2026-04-19", method: "Thẻ Visa", amount: 15000000 },
    { id: "HD-9924", customer: "Phạm Thu Hà", service: "Tẩy trắng răng", date: "2026-04-18", method: "Chuyển khoản", amount: 2500000 },
    { id: "HD-9925", customer: "Hoàng Văn Tuấn", service: "Cấy ghép Implant", date: "2026-04-17", method: "Tiền mặt", amount: 15000000 },
    { id: "HD-9926", customer: "Đặng Thị Hoa", service: "Bọc răng sứ", date: "2026-04-16", method: "Thẻ Mastercard", amount: 5000000 },
    { id: "HD-9927", customer: "Bùi Minh Quân", service: "Trám răng", date: "2026-04-15", method: "Chuyển khoản", amount: 800000 },
    { id: "HD-9928", customer: "Ngô Thị Lan", service: "Nhổ răng sữa", date: "2026-04-14", method: "Tiền mặt", amount: 200000 },
    { id: "HD-9929", customer: "Vũ Minh Đức", service: "Khám tổng quát", date: "2026-04-13", method: "Chuyển khoản", amount: 150000 },
    { id: "HD-9930", customer: "Đinh Bảo Ngọc", service: "Niềng răng", date: "2026-04-12", method: "Thẻ Visa", amount: 10000000 },
    { id: "HD-9931", customer: "Lý Thảo Ly", service: "Cạo vôi răng", date: "2026-04-11", method: "Tiền mặt", amount: 200000 },
    { id: "HD-9932", customer: "Trương Công Phát", service: "Trám răng Composite", date: "2026-04-10", method: "Chuyển khoản", amount: 600000 },
    { id: "HD-9933", customer: "Lâm Bích Ngọc", service: "Tẩy trắng răng", date: "2026-04-09", method: "Thẻ Visa", amount: 2500000 },
    { id: "HD-9934", customer: "Hồ Quốc Toàn", service: "Nhổ răng khôn", date: "2026-04-08", method: "Tiền mặt", amount: 2500000 },
    { id: "HD-9935", customer: "Châu Tú Anh", service: "Khám tổng quát", date: "2026-04-07", method: "Chuyển khoản", amount: 150000 }
];

// Thống kê dịch vụ
let serviceStats = [
    { name: "Niềng răng", percent: 40, color: "#2563eb", amount: 25000000 },
    { name: "Nhổ răng", percent: 25, color: "#10b981", amount: 15625000 },
    { name: "Tẩy trắng", percent: 15, color: "#f59e0b", amount: 9375000 },
    { name: "Cấy ghép", percent: 12, color: "#8b5cf6", amount: 7500000 },
    { name: "Khám tổng quát", percent: 8, color: "#ec4899", amount: 5000000 }
];

// Thống kê phương thức thanh toán
let paymentStats = [
    { name: "Chuyển khoản", icon: "fas fa-university", amount: 20000000, percent: 45, color: "#3b82f6" },
    { name: "Tiền mặt", icon: "fas fa-money-bill-wave", amount: 15000000, percent: 32, color: "#10b981" },
    { name: "Thẻ Visa/Master", icon: "fas fa-credit-card", amount: 10000000, percent: 23, color: "#f59e0b" }
];

// ==================== BIẾN TOÀN CỤC ====================
let currentFilter = 'week';
let currentPage = 1;
let rowsPerPage = 5;
let revenueChart = null;
let currentChartType = 'bar';

// ==================== HÀM TIỆN ÍCH ====================
function formatCurrency(amount) {
    if (!amount && amount !== 0) return "0đ";
    return amount.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + "đ";
}

function formatDate(dateStr) {
    if (!dateStr) return "";
    let date = new Date(dateStr);
    let day = date.getDate().toString().padStart(2, '0');
    let month = (date.getMonth() + 1).toString().padStart(2, '0');
    let year = date.getFullYear();
    return `${day}/${month}/${year}`;
}

function getMethodClass(method) {
    if (method === 'Tiền mặt') return 'method-cash';
    if (method === 'Chuyển khoản') return 'method-transfer';
    return 'method-card';
}

function getMethodIcon(method) {
    if (method === 'Tiền mặt') return 'fas fa-money-bill-wave';
    if (method === 'Chuyển khoản') return 'fas fa-university';
    return 'fas fa-credit-card';
}

function showToast(message, type = 'success') {
    let toast = document.createElement('div');
    toast.className = 'toast-message';
    toast.innerHTML = `<i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-info-circle'}"></i> ${message}`;
    toast.style.cssText = `
        position: fixed;
        bottom: 30px;
        right: 30px;
        background: ${type === 'success' ? '#10b981' : '#f59e0b'};
        color: white;
        padding: 12px 20px;
        border-radius: 12px;
        font-weight: 600;
        z-index: 3000;
        animation: slideInRight 0.3s;
    `;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

// Thêm style cho animation
if (!document.querySelector('#toast-style')) {
    let style = document.createElement('style');
    style.id = 'toast-style';
    style.textContent = `
        @keyframes slideInRight {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        .toast-message {
            animation: slideInRight 0.3s;
        }
    `;
    document.head.appendChild(style);
}

// ==================== LỌC DỮ LIỆU THEO NGÀY ====================
function getDateRange(filter) {
    let today = new Date();
    let start = new Date();
    let end = new Date();
    
    switch(filter) {
        case 'today':
            start = new Date(today.getFullYear(), today.getMonth(), today.getDate());
            end = new Date(today.getFullYear(), today.getMonth(), today.getDate());
            break;
        case 'week':
            let day = today.getDay();
            let startOfWeek = new Date(today);
            let diff = day === 0 ? 6 : day - 1;
            startOfWeek.setDate(today.getDate() - diff);
            start = new Date(startOfWeek.getFullYear(), startOfWeek.getMonth(), startOfWeek.getDate());
            end = new Date(today.getFullYear(), today.getMonth(), today.getDate());
            break;
        case 'month':
            start = new Date(today.getFullYear(), today.getMonth(), 1);
            end = new Date(today.getFullYear(), today.getMonth() + 1, 0);
            break;
        case 'year':
            start = new Date(today.getFullYear(), 0, 1);
            end = new Date(today.getFullYear(), 11, 31);
            break;
        default:
            start = new Date(today.getFullYear(), today.getMonth(), 1);
            end = new Date(today.getFullYear(), today.getMonth() + 1, 0);
    }
    return { start, end };
}

function filterTransactionsByDate(transactions, startDate, endDate) {
    return transactions.filter(t => {
        let tDate = new Date(t.date);
        return tDate >= startDate && tDate <= endDate;
    });
}

// ==================== THỐNG KÊ ====================
function updateStats(filteredTransactions) {
    let total = filteredTransactions.reduce((sum, t) => sum + t.amount, 0);
    let uniqueCustomers = [...new Set(filteredTransactions.map(t => t.customer))];
    let newCustomers = uniqueCustomers.length;
    let completedApps = filteredTransactions.length;
    let growth = 12.5;
    
    let totalElement = document.getElementById('totalRevenue');
    let newCustomersElement = document.getElementById('newCustomers');
    let completedElement = document.getElementById('completedAppointments');
    let growthElement = document.getElementById('growth');
    
    if (totalElement) totalElement.innerText = formatCurrency(total);
    if (newCustomersElement) newCustomersElement.innerText = newCustomers;
    if (completedElement) completedElement.innerText = completedApps;
    if (growthElement) growthElement.innerText = `+${growth}%`;
}

// ==================== BIỂU ĐỒ DOANH THU ====================
function getDailyRevenue(transactions, days = 7) {
    let dailyData = {};
    let today = new Date();
    let daysOfWeek = ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'];
    
    // Khởi tạo 7 ngày gần nhất
    for (let i = days - 1; i >= 0; i--) {
        let date = new Date(today);
        date.setDate(today.getDate() - i);
        let dayIndex = date.getDay();
        let dayName = daysOfWeek[dayIndex === 0 ? 6 : dayIndex - 1];
        dailyData[dayName] = 0;
    }
    
    // Cộng dồn doanh thu
    transactions.forEach(t => {
        let tDate = new Date(t.date);
        let dayIndex = tDate.getDay();
        let dayName = daysOfWeek[dayIndex === 0 ? 6 : dayIndex - 1];
        if (dailyData[dayName] !== undefined) {
            dailyData[dayName] += t.amount;
        }
    });
    
    return {
        labels: Object.keys(dailyData),
        values: Object.values(dailyData)
    };
}

function initChart() {
    // Lấy dữ liệu
    let range = getDateRange(currentFilter);
    let filtered = filterTransactionsByDate(transactions, range.start, range.end);
    let chartData = getDailyRevenue(filtered);
    
    console.log("Dữ liệu biểu đồ:", chartData);
    
    let canvas = document.getElementById('revenueChart');
    if (!canvas) {
        console.error("Không tìm thấy canvas revenueChart!");
        return;
    }
    
    let ctx = canvas.getContext('2d');
    
    // Hủy biểu đồ cũ
    if (revenueChart) {
        revenueChart.destroy();
    }
    
    // Tạo biểu đồ mới
    revenueChart = new Chart(ctx, {
        type: currentChartType,
        data: {
            labels: chartData.labels,
            datasets: [{
                label: 'Doanh thu (VNĐ)',
                data: chartData.values,
                backgroundColor: currentChartType === 'bar' ? '#2563eb' : 'rgba(37, 99, 235, 0.2)',
                borderColor: '#2563eb',
                borderWidth: 2,
                tension: 0.4,
                fill: currentChartType === 'line',
                pointBackgroundColor: '#2563eb',
                pointBorderColor: '#fff',
                pointRadius: currentChartType === 'line' ? 4 : 0,
                pointHoverRadius: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            plugins: {
                legend: {
                    position: 'top',
                    labels: { font: { size: 12 } }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return `Doanh thu: ${formatCurrency(context.raw)}`;
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            if (value >= 1000000) return (value / 1000000).toFixed(1) + 'tr';
                            if (value >= 1000) return (value / 1000).toFixed(0) + 'k';
                            return value;
                        }
                    },
                    title: {
                        display: true,
                        text: 'Doanh thu (VNĐ)',
                        font: { size: 12 }
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Ngày trong tuần',
                        font: { size: 12 }
                    }
                }
            }
        }
    });
    
    console.log("Biểu đồ đã được tạo thành công!");
}

// ==================== DỊCH VỤ PHỔ BIẾN ====================
function renderServices() {
    let container = document.getElementById('servicesList');
    if (!container) return;
    
    container.innerHTML = serviceStats.map(service => `
        <div class="service-item" style="margin-bottom: 20px;">
            <div class="service-header" style="display: flex; justify-content: space-between; margin-bottom: 8px;">
                <span class="service-name" style="font-weight: 600;">${service.name}</span>
                <span class="service-percent" style="color: ${service.color}; font-weight: 700;">${service.percent}%</span>
            </div>
            <div class="progress-bar" style="width: 100%; height: 8px; background: #e5e7eb; border-radius: 10px; overflow: hidden;">
                <div class="progress-fill" style="width: ${service.percent}%; height: 100%; background: ${service.color}; border-radius: 10px;"></div>
            </div>
            <div class="service-amount" style="font-size: 0.75rem; color: #6b7280; margin-top: 5px;">
                ${formatCurrency(service.amount)}
            </div>
        </div>
    `).join('');
}

// ==================== PHƯƠNG THỨC THANH TOÁN ====================
function renderPaymentMethods() {
    let container = document.getElementById('paymentMethods');
    if (!container) return;
    
    container.innerHTML = paymentStats.map(method => `
        <div class="payment-item" style="text-align: center; padding: 15px; background: #f3f4f6; border-radius: 12px;">
            <i class="${method.icon}" style="color: ${method.color}; font-size: 2rem;"></i>
            <div class="payment-name" style="margin-top: 8px; font-size: 0.8rem; color: #6b7280;">${method.name}</div>
            <div class="payment-amount" style="font-weight: 700; font-size: 1rem;">${formatCurrency(method.amount)}</div>
            <div class="payment-percent" style="color: ${method.color}; font-size: 0.7rem;">${method.percent}%</div>
        </div>
    `).join('');
}

// ==================== BẢNG GIAO DỊCH ====================
function renderTransactionTable() {
    let range = getDateRange(currentFilter);
    let filtered = filterTransactionsByDate(transactions, range.start, range.end);
    
    let searchTerm = document.getElementById('searchTransaction')?.value.toLowerCase() || '';
    
    let filteredSearch = filtered.filter(t => 
        t.id.toLowerCase().includes(searchTerm) ||
        t.customer.toLowerCase().includes(searchTerm) ||
        t.service.toLowerCase().includes(searchTerm)
    );
    
    let totalPages = Math.ceil(filteredSearch.length / rowsPerPage);
    let start = (currentPage - 1) * rowsPerPage;
    let end = start + rowsPerPage;
    let paginated = filteredSearch.slice(start, end);
    
    let tbody = document.getElementById('transactionBody');
    if (!tbody) return;
    
    if (paginated.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; padding: 40px;">📭 Không có dữ liệu giao dịch</td></tr>';
    } else {
        tbody.innerHTML = paginated.map(t => `
            <tr>
                <td style="font-weight: 600; color: #2563eb;">${t.id}</td>
                <td>${t.customer}</td>
                <td>${t.service}</td>
                <td>${formatDate(t.date)}</td>
                <td><span class="payment-method-badge ${getMethodClass(t.method)}" style="display: inline-flex; align-items: center; gap: 6px; padding: 4px 10px; border-radius: 20px; font-size: 0.7rem; font-weight: 600;"><i class="${getMethodIcon(t.method)}"></i> ${t.method}</span></td>
                <td style="color: #10b981; font-weight: 700;">${formatCurrency(t.amount)}</td>
            </tr>
        `).join('');
    }
    
    renderPagination(totalPages);
}

function renderPagination(totalPages) {
    let paginationDiv = document.getElementById('pagination');
    if (!paginationDiv) return;
    
    if (totalPages <= 1) {
        paginationDiv.innerHTML = '';
        return;
    }
    
    let html = '<div style="display: flex; gap: 8px; justify-content: center; margin-top: 20px;">';
    for (let i = 1; i <= totalPages; i++) {
        html += `<button class="page-btn ${i === currentPage ? 'active' : ''}" style="padding: 8px 14px; border: 1px solid #e5e7eb; background: white; border-radius: 8px; cursor: pointer; ${i === currentPage ? 'background: #2563eb; color: white; border-color: #2563eb;' : ''}" onclick="goToPage(${i})">${i}</button>`;
    }
    html += '</div>';
    paginationDiv.innerHTML = html;
}

function goToPage(page) {
    currentPage = page;
    renderTransactionTable();
}

// ==================== ÁP DỤNG BỘ LỌC ====================
function applyFilter() {
    let startDateInput = document.getElementById('startDate')?.value;
    let endDateInput = document.getElementById('endDate')?.value;
    
    if (startDateInput && endDateInput) {
        // Nếu có ngày tùy chỉnh, cập nhật currentFilter
        currentFilter = 'custom';
        document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
    } else {
        // Cập nhật active cho button filter
        document.querySelectorAll('.filter-btn').forEach(btn => {
            if (btn.getAttribute('data-filter') === currentFilter) {
                btn.classList.add('active');
            } else {
                btn.classList.remove('active');
            }
        });
    }
    
    currentPage = 1;
    let range = getDateRange(currentFilter);
    let filtered = filterTransactionsByDate(transactions, range.start, range.end);
    updateStats(filtered);
    initChart();
    renderTransactionTable();
}

// ==================== XUẤT EXCEL ====================
function exportToExcel() {
    let headers = ["Mã đơn", "Khách hàng", "Dịch vụ", "Ngày", "Phương thức thanh toán", "Số tiền"];
    let csvRows = [headers.join(",")];
    
    transactions.forEach(t => {
        let row = [
            `"${t.id}"`,
            `"${t.customer}"`,
            `"${t.service}"`,
            `"${t.date}"`,
            `"${t.method}"`,
            t.amount
        ];
        csvRows.push(row.join(","));
    });
    
    let csvContent = "\uFEFF" + csvRows.join("\n");
    let blob = new Blob([csvContent], { type: "text/csv;charset=utf-8;" });
    let link = document.createElement("a");
    let url = URL.createObjectURL(blob);
    link.href = url;
    link.setAttribute("download", `doanhthu_${new Date().toISOString().split('T')[0]}.csv`);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
    
    showToast("📊 Đã xuất báo cáo doanh thu thành công!");
}

// ==================== CẬP NHẬT NGÀY TỰ ĐỘNG ====================
function setDefaultDates() {
    let today = new Date();
    let weekAgo = new Date();
    weekAgo.setDate(weekAgo.getDate() - 7);
    
    let startDateInput = document.getElementById('startDate');
    let endDateInput = document.getElementById('endDate');
    
    if (startDateInput && endDateInput) {
        startDateInput.value = weekAgo.toISOString().split('T')[0];
        endDateInput.value = today.toISOString().split('T')[0];
    }
}

// ==================== KHỞI TẠO ====================
document.addEventListener('DOMContentLoaded', function() {
    console.log("Đang khởi tạo trang báo cáo doanh thu...");
    
    // Kiểm tra Chart.js đã load chưa
    if (typeof Chart === 'undefined') {
        console.error("Chart.js chưa được tải! Vui lòng kiểm tra lại.");
        return;
    }
    console.log("Chart.js đã sẵn sàng!");
    
    // Render các thành phần
    renderServices();
    renderPaymentMethods();
    
    // Set ngày mặc định
    setDefaultDates();
    
    // Áp dụng bộ lọc và vẽ biểu đồ
    applyFilter();
    
    // Gắn sự kiện cho các nút filter
    document.querySelectorAll('.filter-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            currentFilter = this.getAttribute('data-filter');
            document.getElementById('startDate').value = '';
            document.getElementById('endDate').value = '';
            applyFilter();
        });
    });
    
    // Gắn sự kiện cho nút áp dụng
    let applyBtn = document.getElementById('applyDateBtn');
    if (applyBtn) {
        applyBtn.addEventListener('click', function() {
            currentFilter = 'custom';
            document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
            applyFilter();
        });
    }
    
    // Gắn sự kiện cho nút xuất Excel
    let exportBtn = document.getElementById('exportExcelBtn');
    if (exportBtn) {
        exportBtn.addEventListener('click', exportToExcel);
    }
    
    // Gắn sự kiện cho ô tìm kiếm
    let searchInput = document.getElementById('searchTransaction');
    if (searchInput) {
        searchInput.addEventListener('keyup', function() {
            currentPage = 1;
            renderTransactionTable();
        });
    }
    
    // Gắn sự kiện cho nút chuyển đổi biểu đồ
    document.querySelectorAll('.chart-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            document.querySelectorAll('.chart-btn').forEach(b => b.classList.remove('active'));
            this.classList.add('active');
            currentChartType = this.getAttribute('data-chart');
            initChart();
        });
    });
    
    console.log("Khởi tạo hoàn tất!");
});