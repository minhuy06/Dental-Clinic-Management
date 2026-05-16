/**
 * ==================== NHA KHOA 5AE - BÁO CÁO DOANH THU ====================
 * File: doanhthu.js
 * Chức năng: Xử lý biểu đồ, thống kê, bảng giao dịch
 * Cập nhật: Tháng 4/2026
 */

/** Dữ liệu từ API — không dùng mẫu cứng */
let transactions = [];
let serviceStats = [];
let paymentStats = [];
const TOP_SERVICES_LIMIT = 5;
let servicesExpanded = false;

// ==================== BIẾN TOÀN CỤC ====================
let currentFilter = 'week';
let currentPage = 1;
let rowsPerPage = 5;
let revenueChart = null;
let currentChartType = 'bar';

const REPORT_API_BASE = (function () {
    const cp = typeof window.APP_CONTEXT_PATH === 'string' ? window.APP_CONTEXT_PATH : '';
    return cp + '/api';
})();

async function reportRequest(path, options = {}) {
    const method = options.method || 'GET';
    const body = options.body;
    const headers = options.headers || {};
    const fetchOptions = {
        method: method,
        credentials: 'same-origin',
        headers: Object.assign({ 'Content-Type': 'application/json' }, headers)
    };
    if (body !== undefined && body !== null) fetchOptions.body = JSON.stringify(body);

    const res = await fetch(REPORT_API_BASE + '/' + path, fetchOptions);
    let json = null;
    try { json = await res.json(); } catch (e) { json = null; }
    if (!res.ok) throw new Error((json && json.message) || ('Lỗi HTTP: ' + res.status));
    return json;
}

function applyReportPayload(data) {
    if (!data || typeof data !== 'object') return;
    transactions = Array.isArray(data.transactions) ? data.transactions : [];
    serviceStats = Array.isArray(data.serviceStats) ? data.serviceStats : [];
    serviceStats.sort(function(a, b) { return (b.amount || 0) - (a.amount || 0); });
    servicesExpanded = false;
    paymentStats = Array.isArray(data.paymentStats) ? data.paymentStats : [];
}

async function loadReportData() {
    const seeded = window.INITIAL_REPORT_FROM_SERVER;
    if (seeded && typeof seeded === 'object' && Object.keys(seeded).length > 0) {
        applyReportPayload(seeded);
        console.info('[baocao] Seed từ server:', transactions.length, 'giao dịch');
    }

    try {
        const data = await reportRequest('reports/revenue-summary');
        applyReportPayload(data);
        console.info('[baocao] API:', transactions.length, 'giao dịch');
    } catch (error) {
        console.error('[baocao] API error:', error);
        if (transactions.length === 0) {
            showToast(error.message || 'Không tải được dữ liệu báo cáo. Hãy đăng nhập lễ tân.', 'info');
        }
    }
}

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
    let totalElement = document.getElementById('totalRevenue');
    let newCustomersElement = document.getElementById('newCustomers');
    let completedElement = document.getElementById('completedAppointments');
    let growthElement = document.getElementById('growth');
    
    if (totalElement) totalElement.innerText = formatCurrency(total);
    if (newCustomersElement) newCustomersElement.innerText = newCustomers;
    if (completedElement) completedElement.innerText = completedApps;
    if (growthElement) growthElement.innerText = completedApps > 0 ? '—' : '0%';
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

    if (!serviceStats.length) {
        container.innerHTML = '<p style="color:#6b7280;font-size:0.85rem;">Chưa có dữ liệu dịch vụ từ hóa đơn.</p>';
        return;
    }
    
    const total = serviceStats.length;
    const visible = servicesExpanded ? serviceStats : serviceStats.slice(0, TOP_SERVICES_LIMIT);
    let servicesHtml = visible.map(service => `
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
    if (total > TOP_SERVICES_LIMIT) {
        const hidden = total - TOP_SERVICES_LIMIT;
        const label = servicesExpanded ? 'Thu gọn' : ('Xem thêm (' + hidden + ' dịch vụ)');
        servicesHtml += '<button type="button" class="btn-services-more" id="btnServicesMore">' + label + '</button>';
    }
    container.innerHTML = servicesHtml;
    const btn = document.getElementById('btnServicesMore');
    if (btn) {
        btn.addEventListener('click', function() {
            servicesExpanded = !servicesExpanded;
            renderServices();
        });
    }
}

// ==================== PHƯƠNG THỨC THANH TOÁN ====================
function renderPaymentMethods() {
    let container = document.getElementById('paymentMethods');
    if (!container) return;

    if (!paymentStats.length) {
        container.innerHTML = '<p style="color:#6b7280;font-size:0.85rem;">Chưa có dữ liệu thanh toán.</p>';
        return;
    }
    
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
document.addEventListener('DOMContentLoaded', async function() {
    console.info('[baocao] JS version', window.BAOCAO_JS_VERSION || 'unknown');
    console.log("Đang khởi tạo trang báo cáo doanh thu...");
    // Kiểm tra Chart.js đã load chưa
    if (typeof Chart === 'undefined') {
        console.error("Chart.js chưa được tải! Vui lòng kiểm tra lại.");
        return;
    }
    console.log("Chart.js đã sẵn sàng!");
    
    await loadReportData();

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