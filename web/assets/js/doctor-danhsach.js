(function initDoctorDanhsachBootstrap() {
    if (typeof AppBootstrap !== 'undefined') {
        var cp = AppBootstrap.getMetaContent('context-path');
        if (cp) window.CONTEXT_PATH = cp;
    }
})();

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


let DOCTOR_LIST_CONFIG = {
    USE_MOCK: false,
    API_BASE: (typeof window.CONTEXT_PATH !== 'undefined' && window.CONTEXT_PATH ? window.CONTEXT_PATH : '') + '/api/doctor',
    MOCK_DELAY_MS: 120
};

function listDelay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

async function listRequest(path, options = {}) {
    const method = options.method || 'GET';
    const body = options.body;
    const headers = options.headers || {};
    const fetchOptions = {
        method: method,
        headers: Object.assign({ 'Content-Type': 'application/json' }, headers)
    };
    if (body !== undefined && body !== null) fetchOptions.body = JSON.stringify(body);

    const res = await fetch(DOCTOR_LIST_CONFIG.API_BASE + path, fetchOptions);
    let json = null;
    try { json = await res.json(); } catch (e) { json = null; }
    if (!res.ok) throw new Error((json && json.message) || ('Lỗi HTTP: ' + res.status));
    return json;
}

function normalizeList(res) {
    if (!res) return [];
    if (Array.isArray(res)) return res;
    if (Array.isArray(res.data)) return res.data;
    return [];
}

const mockListSource = {
    listAppointments: async () => {
        await listDelay(DOCTOR_LIST_CONFIG.MOCK_DELAY_MS);
        return JSON.parse(JSON.stringify(appointments));
    },
    startExam: async (id) => {
        await listDelay(DOCTOR_LIST_CONFIG.MOCK_DELAY_MS);
        const target = appointments.find(a => a.id === id);
        if (!target) return { success: false, message: 'Không tìm thấy lịch hẹn' };
        target.status = 'examining';
        return { success: true };
    },
    completeExam: async (id) => {
        await listDelay(DOCTOR_LIST_CONFIG.MOCK_DELAY_MS);
        const target = appointments.find(a => a.id === id);
        if (!target) return { success: false, message: 'Không tìm thấy lịch hẹn' };
        if (target.status !== 'examining' && target.status !== 'waiting') {
            return { success: false, message: 'Chỉ có thể hoàn tất khi đang khám' };
        }
        target.status = 'completed';
        return { success: true };
    }
};

const apiListSource = {
    listAppointments: async () => normalizeList(await listRequest('/appointments')),
    startExam: async (id) => listRequest('/appointments', { method: 'PUT', body: { id, action: 'start' } }),
    completeExam: async (id) => listRequest('/appointments', { method: 'PUT', body: { id, action: 'complete' } })
};

const listDataSource = DOCTOR_LIST_CONFIG.USE_MOCK ? mockListSource : apiListSource;

async function withListGuard(action, onErrorMessage) {
    try {
        const res = await action();
        if (res && res.success === false) {
            AppNotify.error(res.message || onErrorMessage || 'Thao tác thất bại');
            return null;
        }
        return res || { success: true };
    } catch (error) {
        console.error('[doctor/danhsach] data error:', error);
        AppNotify.error(error.message || onErrorMessage || 'Lỗi kết nối dữ liệu');
        return null;
    }
}

async function loadAppointmentsFromServer() {
    const list = await withListGuard(() => listDataSource.listAppointments(), 'Không tải được danh sách lịch hẹn');
    if (!list) return;
    const rawData = normalizeList(list);
    appointments = rawData.map((item, index) => ({
        id: item.lichHenID,
        stt: index + 1,
        patientName: item.tenBenhNhan,
        patientPhone: item.soDienThoai,
        time: item.gioHen,
        doctor: item.tenBacSi,
        reason: item.lyDoKham,
        status: item.trangThai
    }));
    renderTable();
}

function updateDateTime() {
    const now = new Date();
    const dateStr = now.toLocaleDateString('vi-VN', { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' });
    const timeStr = now.toLocaleTimeString('vi-VN');
    const el = document.getElementById('currentDateTime');
    if (el) el.innerHTML = `<i class="fa-regular fa-calendar"></i> ${dateStr} | ${timeStr}`;
}

function renderTable() {
    const keyword = document.getElementById('searchInput').value.trim().toLowerCase();
    const status = document.getElementById('statusFilter').value;

    let filtered = appointments.filter(app => {
        const name = (app.patientName || '').toLowerCase();
        const phone = app.patientPhone || '';
        const matchSearch = name.includes(keyword) || phone.includes(keyword);
        const matchStatus = status === 'all' || app.status === status;
        return matchSearch && matchStatus;
    });

    const tbody = document.getElementById('appointmentTableBody');
    if (!tbody) return;

    if (filtered.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7"><div class="empty-state"><i class="fa-regular fa-calendar-xmark"></i><p>Không tìm thấy lịch hẹn</p></div></td></tr>';
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
            actionButton = `
                <button type="button" class="btn-exam" onclick="continueExamination(${app.id})"><i class="fa-solid fa-arrow-right"></i> Tiếp tục khám</button>
                <button type="button" class="btn-complete" onclick="confirmCompleteExam(${app.id})"><i class="fa-solid fa-circle-check"></i> Xác nhận hoàn tất</button>`;
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

async function startExamination(id) {
    const app = appointments.find(a => a.id === id);
    if (!app) return;
    const ok = await AppNotify.confirm({
        message: 'Bạn có muốn bắt đầu khám cho bệnh nhân <strong>' + app.patientName + '</strong> không?'
    });
    if (!ok) return;
    const res = await withListGuard(() => listDataSource.startExam(id), 'Không thể bắt đầu khám');
    if (!res) return;
    await loadAppointmentsFromServer();
    window.location.href = 'hoso.jsp?id=' + app.id
        + '&name=' + encodeURIComponent(app.patientName)
        + '&phone=' + encodeURIComponent(app.patientPhone || '')
        + '&reason=' + encodeURIComponent(app.reason || '');
}

function continueExamination(id) {
    const app = appointments.find(a => a.id === id);
    if (app) {
        window.location.href = `hoso.jsp?id=${app.id}&name=${encodeURIComponent(app.patientName)}&phone=${encodeURIComponent(app.patientPhone || '')}&reason=${encodeURIComponent(app.reason || '')}`;
    }
}

async function confirmCompleteExam(id) {
    const app = appointments.find(a => a.id === id);
    if (!app) return;
    const ok = await AppNotify.confirm({
        message: 'Bạn có muốn xác nhận đã khám cho bệnh nhân <strong>' + app.patientName + '</strong> không?'
    });
    if (!ok) return;
    const res = await withListGuard(() => listDataSource.completeExam(id), 'Không thể xác nhận hoàn tất khám');
    if (!res) return;
    await AppNotify.success('Đã xác nhận hoàn tất khám. Bệnh nhân chuyển sang lễ tân để thanh toán.');
    await loadAppointmentsFromServer();
}

function viewRecord(id) {
    const app = appointments.find(a => a.id === id);
    if (!app) return;
    AppNotify.alert({
        title: 'Hồ sơ bệnh nhân',
        html: '<strong>' + app.patientName + '</strong><br>📞 ' + (app.patientPhone || '—')
            + '<br>🦷 ' + (app.reason || '—') + '<br>👨‍⚕️ ' + (app.doctor || '—')
    });
}

document.addEventListener('DOMContentLoaded', () => {
    console.info('[doctor/danhsach] mode:', DOCTOR_LIST_CONFIG.USE_MOCK ? 'MOCK' : 'REAL API');
    updateDateTime();
    setInterval(updateDateTime, 1000);
    loadAppointmentsFromServer();

    const searchInput = document.getElementById('searchInput');
    const statusFilter = document.getElementById('statusFilter');
    const refreshBtn = document.getElementById('refreshBtn');

    if (searchInput) searchInput.addEventListener('input', renderTable);
    if (statusFilter) statusFilter.addEventListener('change', renderTable);
    if (refreshBtn) {
        refreshBtn.addEventListener('click', () => {
            if (searchInput) searchInput.value = '';
            if (statusFilter) statusFilter.value = 'all';
            loadAppointmentsFromServer();
        });
    }

});
