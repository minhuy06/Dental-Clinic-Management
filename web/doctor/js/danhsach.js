// ========================
// DANH SÁCH CHỜ KHÁM (đã xóa lọc bác sĩ)
// ========================

// Dữ liệu mẫu (giữ nguyên)
let appointments = [
    { id: 1, stt: 1, patientName: 'Nguyễn Văn Hiển', patientPhone: '0987 654 321', time: '08:00', doctor: 'BS. Nguyễn Hoàng', reason: 'Đau răng hàm dưới', status: 'waiting' },
    { id: 2, stt: 2, patientName: 'Trần Thị Lan', patientPhone: '0912 345 678', time: '08:30', doctor: 'BS. Nguyễn Hoàng', reason: 'Khám tổng quát', status: 'waiting' },
    { id: 3, stt: 3, patientName: 'Lê Văn Minh', patientPhone: '0933 456 789', time: '09:00', doctor: 'BS. Trần Thị Mai', reason: 'Lấy cao răng', status: 'examining' },
    { id: 4, stt: 4, patientName: 'Phạm Thị Hoa', patientPhone: '0977 123 456', time: '09:30', doctor: 'BS. Nguyễn Hoàng', reason: 'Hàn răng số 46', status: 'waiting' },
    { id: 5, stt: 5, patientName: 'Hoàng Văn Tùng', patientPhone: '0966 789 012', time: '10:00', doctor: 'BS. Lê Văn Nam', reason: 'Nhổ răng khôn', status: 'completed' },
    { id: 6, stt: 6, patientName: 'Nguyễn Thị Hồng', patientPhone: '0944 567 890', time: '10:30', doctor: 'BS. Trần Thị Mai', reason: 'Điều trị tủy', status: 'cancelled' },
    { id: 7, stt: 7, patientName: 'Trương Quốc Bảo', patientPhone: '0909 888 777', time: '11:00', doctor: 'BS. Nguyễn Hoàng', reason: 'Bọc răng sứ', status: 'waiting' },
    { id: 8, stt: 8, patientName: 'Mai Thị Thanh', patientPhone: '0978 654 321', time: '13:30', doctor: 'BS. Lê Văn Nam', reason: 'Tư vấn niềng răng', status: 'waiting' },
    { id: 9, stt: 9, patientName: 'Đỗ Văn Cường', patientPhone: '0965 432 109', time: '14:00', doctor: 'BS. Nguyễn Hoàng', reason: 'Đau buốt khi ăn', status: 'waiting' },
    { id: 10, stt: 10, patientName: 'Vũ Thị Kim Ngân', patientPhone: '0938 765 432', time: '14:30', doctor: 'BS. Trần Thị Mai', reason: 'Cấy ghép Implant', status: 'completed' }
];

let currentSelected = null;
let pendingAction = null;

// Cập nhật đồng hồ
function updateDateTime() {
    const now = new Date();
    const dateStr = now.toLocaleDateString('vi-VN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
    const timeStr = now.toLocaleTimeString('vi-VN');
    const el = document.getElementById('currentDateTime');
    if (el) el.innerHTML = `<i class="fa-regular fa-calendar"></i> ${dateStr} | ${timeStr}`;
}

// Render bảng với bộ lọc: tìm kiếm + trạng thái
function renderTable() {
    const keyword = document.getElementById('searchInput').value.trim().toLowerCase();
    const status = document.getElementById('statusFilter').value;

    let filtered = appointments.filter(app => {
        const matchSearch = app.patientName.toLowerCase().includes(keyword) || app.patientPhone.includes(keyword);
        const matchStatus = status === 'all' || app.status === status;
        return matchSearch && matchStatus;
    });

    const tbody = document.getElementById('appointmentTableBody');
    if (!tbody) return;

    if (filtered.length === 0) {
        tbody.innerHTML = `</td><td colspan="7"><div class="empty-state"><i class="fa-regular fa-calendar-xmark"></i><p>Không tìm thấy lịch hẹn</p></div></td></tr>`;
        return;
    }

    let html = '';
    filtered.forEach(app => {
        let statusText = '', statusClass = '';
        switch (app.status) {
            case 'waiting':   statusText = 'Đang chờ';   statusClass = 'status-waiting'; break;
            case 'examining': statusText = 'Đang khám'; statusClass = 'status-examining'; break;
            case 'completed': statusText = 'Đã khám';   statusClass = 'status-completed'; break;
            case 'cancelled': statusText = 'Đã hủy';    statusClass = 'status-cancelled'; break;
        }

        let actionButton = '';
        if (app.status === 'waiting') {
            actionButton = `<button class="btn-exam" onclick="startExamination(${app.id})"><i class="fa-solid fa-stethoscope"></i> Khám ngay</button>`;
        } else if (app.status === 'examining') {
            actionButton = `<button class="btn-exam" onclick="continueExamination(${app.id})"><i class="fa-solid fa-arrow-right"></i> Tiếp tục khám</button>`;
        } else {
            actionButton = `<button class="btn-view" onclick="viewRecord(${app.id})"><i class="fa-regular fa-eye"></i> Xem hồ sơ</button>`;
        }

        html += `
            <tr>
                <td>${app.stt}</td>
                <td>
                    <div class="patient-info">
                        <span class="patient-name">${app.patientName}</span>
                        <span class="patient-phone">${app.patientPhone}</span>
                    </div>
                </td>
                <td>${app.time}</td>
                <td>${app.doctor}</td>
                <td>${app.reason}</td>
                <td><span class="status-badge ${statusClass}">${statusText}</span></td>
                <td>${actionButton}</td>
            </tr>
        `;
    });
    tbody.innerHTML = html;
}

// Bắt đầu khám (mở modal)
function startExamination(id) {
    const app = appointments.find(a => a.id === id);
    if (!app) return;
    currentSelected = app;
    pendingAction = 'start';
    document.getElementById('modalMessage').innerHTML = `Bạn có muốn bắt đầu khám cho bệnh nhân <strong>${app.patientName}</strong> không?`;
    document.getElementById('confirmModal').style.display = 'flex';
}

// Tiếp tục khám (chuyển thẳng)
function continueExamination(id) {
    const app = appointments.find(a => a.id === id);
    if (app) {
        window.location.href = `hoso.jsp?id=${app.id}&name=${encodeURIComponent(app.patientName)}`;
    }
}

// Xem hồ sơ tạm (có thể thay bằng trang chi tiết)
function viewRecord(id) {
    const app = appointments.find(a => a.id === id);
    alert(`📋 Hồ sơ bệnh nhân: ${app.patientName}\n📞 SĐT: ${app.patientPhone}\n🦷 Lý do: ${app.reason}\n👨‍⚕️ Bác sĩ: ${app.doctor}`);
}

// Đóng modal
function closeModal() {
    document.getElementById('confirmModal').style.display = 'none';
    currentSelected = null;
    pendingAction = null;
}

// Khởi tạo sự kiện khi DOM sẵn sàng
document.addEventListener('DOMContentLoaded', () => {
    updateDateTime();
    setInterval(updateDateTime, 1000);
    renderTable();

    // Gắn sự kiện cho các ô lọc và nút làm mới
    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');
    const refreshBtn = document.getElementById('refreshBtn');

    if (searchInput) searchInput.addEventListener('input', renderTable);
    if (statusFilter) statusFilter.addEventListener('change', renderTable);
    if (refreshBtn) {
        refreshBtn.addEventListener('click', () => {
            if (searchInput) searchInput.value = '';
            if (statusFilter) statusFilter.value = 'all';
            renderTable();
        });
    }

    // Xử lý nút xác nhận trong modal
    const confirmBtn = document.getElementById('confirmBtn');
    if (confirmBtn) {
        confirmBtn.addEventListener('click', () => {
            if (pendingAction === 'start' && currentSelected) {
                const { id, patientName } = currentSelected;
                // Cập nhật trạng thái bệnh nhân thành 'examining'
                const original = appointments.find(a => a.id === id);
                if (original && original.status === 'waiting') {
                    original.status = 'examining';
                }
                renderTable();
                closeModal();
                // Chuyển hướng sang hoso.jsp
                window.location.href = `hoso.jsp?id=${id}&name=${encodeURIComponent(patientName)}`;
            }
        });
    }

    // Đóng modal khi nhấn nút Hủy hoặc click ra ngoài
    const cancelModalBtn = document.querySelector('.btn-cancel-modal');
    if (cancelModalBtn) cancelModalBtn.addEventListener('click', closeModal);
    window.addEventListener('click', (e) => {
        const modal = document.getElementById('confirmModal');
        if (e.target === modal) closeModal();
    });
});