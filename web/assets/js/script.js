(function initReceptionBootstrap() {
    if (typeof AppBootstrap === 'undefined') return;
    var cp = AppBootstrap.getMetaContent('context-path');
    if (cp) window.APP_CONTEXT_PATH = cp;
    var receptionCtx = AppBootstrap.readJsonScript('receptionContextJson', null);
    if (receptionCtx) window.RECEPTION_CONTEXT = receptionCtx;
    var servicesSeed = AppBootstrap.readJsonScript('receptionServicesJson', null);
    if (servicesSeed !== null) window.SERVICE_LIST_FROM_DB = servicesSeed;
    var roomsSeed = AppBootstrap.readJsonScript('receptionRoomsJson', null);
    if (roomsSeed !== null) window.ROOM_LIST_FROM_DB = roomsSeed;
})();

// ==================== DỮ LIỆU TỪ BACKEND ====================
const servicesList = [];

function normalizeServiceFromDb(service) {
    if (!service) return null;
    const id = Number(service.id || service.dichVuID || 0);
    const name = String(service.name || service.tenDichVu || '').trim();
    const price = Number(service.price || service.giaTien || 0);
    if (!id || !name || price <= 0) return null;
    const hasQuantity = Boolean(service.perUnit || service.tinhTheoRang);
    return {
        id: id,
        name: name,
        price: price,
        hasQuantity: hasQuantity,
        unit: hasQuantity ? (service.unit || 'răng') : undefined,
        time: service.time || (service.thoiLuongDuKien ? `${service.thoiLuongDuKien} phút` : '')
    };
}

function initServicesList() {
    const injected = Array.isArray(window.SERVICE_LIST_FROM_DB) ? window.SERVICE_LIST_FROM_DB : null;
    const source = (injected && injected.length > 0) ? injected : [];
    const normalized = source
        .map(normalizeServiceFromDb)
        .filter(Boolean);

    servicesList.length = 0;
    normalized.forEach(s => servicesList.push(s));
}

let appointments = [];

// ==================== BIẾN TOÀN CỤC ====================
let currentDate = new Date();
let currentView = 'day';
let editingId = null;
let selectedServices = [];
let currentStatusFilter = 'all';
let currentPaymentAppointment = null;

function isReceptionDashboard() {
    return document.body && document.body.id === 'receptionDashboardPage';
}

// ==================== CỜ HIỆU MOCK/REAL + DATA SOURCE ====================
let APPOINTMENT_CONFIG = {
    USE_MOCK: true,
    API_BASE: '/api',
    MOCK_DELAY_MS: 120
};

function dsDelay(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}

function dsClone(data) {
    return JSON.parse(JSON.stringify(data));
}

async function dsRequest(path, options = {}) {
    const method = options.method || 'GET';
    const body = options.body;
    const headers = options.headers || {};
    const fetchOptions = {
        method: method,
        headers: Object.assign({ 'Content-Type': 'application/json' }, headers)
    };
    if (body !== undefined && body !== null) fetchOptions.body = JSON.stringify(body);

    const res = await fetch(APPOINTMENT_CONFIG.API_BASE + '/' + path, fetchOptions);
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

const mockDataSource = {
    listServices: async () => {
        await dsDelay(APPOINTMENT_CONFIG.MOCK_DELAY_MS);
        return dsClone(servicesList);
    },
    listAppointments: async () => {
        await dsDelay(APPOINTMENT_CONFIG.MOCK_DELAY_MS);
        return dsClone(appointments);
    },
    createAppointment: async (payload) => {
        await dsDelay(APPOINTMENT_CONFIG.MOCK_DELAY_MS);
        const newId = Math.max(...appointments.map(a => a.id), 0) + 1;
        appointments.push(Object.assign({ id: newId, status: 'pending', paymentStatus: 'pending' }, payload));
        return { success: true };
    },
    updateAppointment: async (payload) => {
        await dsDelay(APPOINTMENT_CONFIG.MOCK_DELAY_MS);
        const idx = appointments.findIndex(a => a.id === payload.id);
        if (idx === -1) return { success: false, message: 'Không tìm thấy lịch hẹn' };
        appointments[idx] = Object.assign({}, appointments[idx], payload);
        return { success: true };
    },
    updateStatus: async (id, status) => {
        await dsDelay(APPOINTMENT_CONFIG.MOCK_DELAY_MS);
        const apt = appointments.find(a => a.id === id);
        if (!apt) return { success: false, message: 'Không tìm thấy lịch hẹn' };
        apt.status = status;
        return { success: true };
    },
    removeAppointment: async (id) => {
        await dsDelay(APPOINTMENT_CONFIG.MOCK_DELAY_MS);
        appointments = appointments.filter(a => a.id !== id);
        return { success: true };
    },
    confirmPayment: async (payload) => {
        await dsDelay(APPOINTMENT_CONFIG.MOCK_DELAY_MS);
        const apt = appointments.find(a => a.id === payload.appointmentId);
        if (!apt) return { success: false, message: 'Không tìm thấy lịch hẹn' };
        apt.paymentStatus = 'paid';
        return { success: true };
    }
};

const apiDataSource = {
    listServices: async () => normalizeList(await dsRequest('services')),
    listAppointments: async () => normalizeList(await dsRequest('appointments')),
    createAppointment: async (payload) => dsRequest('appointments/add', { method: 'POST', body: payload }),
    updateAppointment: async (payload) => dsRequest('appointments/update', { method: 'PUT', body: payload }),
    updateStatus: async (id, status) => dsRequest('appointments/toggle-status', { method: 'PUT', body: { id, status } }),
    removeAppointment: async (id) => dsRequest('appointments/delete?id=' + id, { method: 'DELETE' }),
    confirmPayment: async (payload) => dsRequest('appointments/confirm-payment', { method: 'PUT', body: payload })
};

const dataSource = APPOINTMENT_CONFIG.USE_MOCK ? mockDataSource : apiDataSource;

async function withDataGuard(action, successMsg) {
    try {
        const res = await action();
        if (res && res.success === false) {
            showToast(res.message || 'Thao tác thất bại', 'error');
            return null;
        }
        if (successMsg) showToast(successMsg, 'success');
        return res || { success: true };
    } catch (error) {
        console.error('[script.js] data error:', error);
        showToast(error.message || 'Lỗi kết nối dữ liệu', 'error');
        return null;
    }
}

async function loadServicesFromServer() {
    // Reception nhận list dịch vụ trực tiếp từ Servlet, không dùng dữ liệu mock.
    loadServicesGrid();
}

function initRoomOptions() {
    const roomSelect = document.getElementById('room');
    const rooms = Array.isArray(window.ROOM_LIST_FROM_DB) ? window.ROOM_LIST_FROM_DB : [];
    if (!roomSelect || rooms.length === 0) return;
    roomSelect.innerHTML = '<option value="">Chọn phòng</option>' +
        rooms.map(function(room) {
            return '<option value="' + room.id + '">' + room.name + '</option>';
        }).join('');
}

async function loadAppointmentsFromServer() {
    if (isReceptionDashboard()) return;
    const list = await withDataGuard(() => dataSource.listAppointments());
    if (!list) return;
    appointments = normalizeList(list);
    renderAppointments();
}

function applyReceptionTableFilters() {
    const doctor = document.getElementById('filterDoctor')?.value || '';
    const searchRaw = document.getElementById('searchInput')?.value || '';
    const search = searchRaw.toLowerCase().trim();
    document.querySelectorAll('#appointmentTableBody tr[data-lh-id]').forEach(tr => {
        let ok = true;
        if (!isReceptionDashboard() && currentStatusFilter !== 'all' && tr.dataset.status !== currentStatusFilter) ok = false;
        if (doctor && String(tr.dataset.bacsiId) !== String(doctor)) ok = false;
        if (search) {
            const pn = (tr.dataset.patientName || '').toLowerCase();
            const ph = String(tr.dataset.patientPhone || '').replace(/\s/g, '');
            const term = search.replace(/\s/g, '');
            if (!pn.includes(search) && !ph.includes(term)) ok = false;
        }
        tr.style.display = ok ? '' : 'none';
    });
}

/** URL lễ tân: thẻ thống kê không gắn view=tuần; loc≠all chỉ ngày + loc; Tuần/Tháng chỉ khi loc=all không hist. */
function buildReceptionDashboardUrl(selectedYmd, ctx) {
    if (!ctx || !ctx.baseUrl) return '';
    const ngay = encodeURIComponent(selectedYmd);
    const rawLoc = (ctx.loc == null ? '' : String(ctx.loc)).trim().toLowerCase();
    const locKey = rawLoc === '' ? 'all' : rawLoc;
    const loc = encodeURIComponent(locKey);
    let href = `${ctx.baseUrl}?ngay=${ngay}&loc=${loc}`;
    const isAllLoc = locKey === 'all';
    if (!isAllLoc) return href;
    if (ctx.histTotal === true || ctx.histTotal === 'true') {
        href += '&hist=1';
        return href;
    }
    href += '&view=' + encodeURIComponent(ctx.viewMode || 'day');
    return href;
}

function shiftReceptionDashboardDate(delta) {
    const ctx = window.RECEPTION_CONTEXT;
    if (!ctx || !ctx.baseUrl || !ctx.selectedDate) return;
    const d = new Date(ctx.selectedDate + 'T12:00:00');
    let vm = ctx.viewMode || 'day';
    if (ctx.loc === 'all' && (ctx.histTotal === true || ctx.histTotal === 'true')) {
        vm = 'day';
    }
    if (vm === 'day') d.setDate(d.getDate() + delta);
    else if (vm === 'week') d.setDate(d.getDate() + delta * 7);
    else if (vm === 'month') d.setMonth(d.getMonth() + delta);
    else if (vm === 'year') d.setFullYear(d.getFullYear() + delta);
    else d.setDate(d.getDate() + delta);
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const day = String(d.getDate()).padStart(2, '0');
    window.location.href = buildReceptionDashboardUrl(`${y}-${m}-${day}`, ctx);
}

// ==================== HÀM TIỆN ÍCH ====================
function formatDate(date) {
    let d = new Date(date);
    return `${d.getFullYear()}-${(d.getMonth()+1).toString().padStart(2,'0')}-${d.getDate().toString().padStart(2,'0')}`;
}

function formatDisplayDate(date) {
    let d = new Date(date);
    let weekdays = ['Chủ nhật', 'Thứ hai', 'Thứ ba', 'Thứ tư', 'Thứ năm', 'Thứ sáu', 'Thứ bảy'];
    return `${weekdays[d.getDay()]}, ${d.getDate()}/${d.getMonth()+1}/${d.getFullYear()}`;
}

function formatDisplayDateShort(date) {
    let d = new Date(date);
    return `${d.getDate()}/${d.getMonth()+1}/${d.getFullYear()}`;
}

function formatWeekRange(date) {
    let d = new Date(date);
    let day = d.getDay();
    let start = new Date(d);
    start.setDate(d.getDate() - day + (day === 0 ? -6 : 1));
    let end = new Date(start);
    end.setDate(start.getDate() + 6);
    return `${start.getDate()}/${start.getMonth()+1} - ${end.getDate()}/${end.getMonth()+1}/${end.getFullYear()}`;
}

function formatMonthYear(date) {
    let months = ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6', 'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'];
    return `${months[date.getMonth()]} ${date.getFullYear()}`;
}

function formatPrice(price) {
    return price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function getStatusClass(status) {
    let map = { 'confirmed': 'status-confirmed', 'pending': 'status-pending', 'cancelled': 'status-cancelled', 'completed': 'status-completed' };
    return map[status] || 'status-pending';
}

function getStatusText(status) {
    let map = { 'confirmed': 'Đã duyệt', 'pending': 'Chờ duyệt', 'cancelled': 'Đã hủy', 'completed': 'Đã hoàn thành' };
    return map[status] || status;
}

function getPaymentStatusText(paymentStatus) {
    return paymentStatus === 'paid' ? 'Đã thanh toán' : 'Chưa thanh toán';
}

function getPaymentStatusClass(paymentStatus) {
    return paymentStatus === 'paid' ? 'status-paid' : 'status-unpaid';
}

function getInitials(name) {
    return name.split(' ').slice(-1)[0].substring(0, 2).toUpperCase();
}

function showToast(message, type = 'success') {
    let oldToast = document.querySelector('.toast-message');
    if (oldToast) oldToast.remove();
    
    let toast = document.createElement('div');
    toast.className = 'toast-message';
    toast.innerHTML = `<i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-info-circle'}"></i> ${message}`;
    document.body.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

function generateInvoiceId() {
    let date = new Date();
    let random = Math.floor(Math.random() * 10000);
    return `HD-${date.getFullYear()}${(date.getMonth()+1).toString().padStart(2,'0')}${date.getDate().toString().padStart(2,'0')}-${random}`;
}

// ==================== LỌC THEO TUẦN VÀ THÁNG ====================
function getWeekDates(date) {
    let d = new Date(date);
    let day = d.getDay();
    let start = new Date(d);
    start.setDate(d.getDate() - day + (day === 0 ? -6 : 1));
    let dates = [];
    for (let i = 0; i < 7; i++) {
        let weekDate = new Date(start);
        weekDate.setDate(start.getDate() + i);
        dates.push(formatDate(weekDate));
    }
    return dates;
}

function getMonthDates(date) {
    let year = date.getFullYear();
    let month = date.getMonth();
    let firstDay = new Date(year, month, 1);
    let lastDay = new Date(year, month + 1, 0);
    let dates = [];
    for (let d = new Date(firstDay); d <= lastDay; d.setDate(d.getDate() + 1)) {
        dates.push(formatDate(d));
    }
    return dates;
}

function isInWeek(dateStr, weekDates) { return weekDates.includes(dateStr); }
function isInMonth(dateStr, monthDates) { return monthDates.includes(dateStr); }

// ==================== QUẢN LÝ TRẠNG THÁI ====================
function filterByStatus(status) {
    if (isReceptionDashboard()) {
        const ctx = window.RECEPTION_CONTEXT;
        if (!ctx || !ctx.baseUrl) return;
        const nm = ctx.selectedDate;
        const nextCtx = Object.assign({}, ctx, {
            loc: status === 'all' ? 'all' : status,
            histTotal: status === 'all'
        });
        window.location.href = buildReceptionDashboardUrl(nm, nextCtx);
        return;
    }
    currentStatusFilter = status;
    document.querySelectorAll('.stat-card').forEach(card => card.classList.remove('active'));
    if (status === 'all') document.getElementById('statAll').classList.add('active');
    else if (status === 'confirmed') document.getElementById('statConfirmed').classList.add('active');
    else if (status === 'pending') document.getElementById('statPending').classList.add('active');
    else if (status === 'completed') document.getElementById('statCompleted').classList.add('active');
    renderAppointments();
}

async function changeStatus(id, newStatus) {
    let apt = appointments.find(a => a.id === id);
    if (apt) {
        let statusText = newStatus === 'confirmed' ? 'duyệt' : newStatus === 'cancelled' ? 'hủy' : newStatus === 'completed' ? 'hoàn thành' : 'cập nhật';
        let res = await withDataGuard(() => dataSource.updateStatus(id, newStatus), `Đã ${statusText} lịch hẹn của ${apt.patientName}`);
        if (res) await loadAppointmentsFromServer();
    }
}

function toggleStatusMenu(id) {
    let menu = document.getElementById(`statusMenu_${id}`);
    if (menu) {
        let isShow = menu.classList.contains('show');
        document.querySelectorAll('.status-menu').forEach(m => m.classList.remove('show'));
        if (!isShow) menu.classList.add('show');
    }
}

function toggleReceptionStatusMenu(id) {
    var menu = document.getElementById('statusMenu_' + id);
    if (!menu) return;
    var isShow = menu.classList.contains('show');
    document.querySelectorAll('.status-menu').forEach(function(m) { m.classList.remove('show'); });
    if (!isShow) menu.classList.add('show');
}

async function postReceptionAction(action, lichHenId, extra) {
    const baseUrl = (window.RECEPTION_CONTEXT && window.RECEPTION_CONTEXT.baseUrl) ? window.RECEPTION_CONTEXT.baseUrl : '';
    const contextPath = baseUrl ? baseUrl.replace(/\/reception-dashboard.*$/, '') : '';
    const params = new URLSearchParams();
    params.append('action', action);
    params.append('lichHenId', String(lichHenId));
    if (extra && typeof extra === 'object') {
        if (extra.paymentMethod) params.append('paymentMethod', String(extra.paymentMethod));
        if (extra.totalAmount != null && extra.totalAmount !== '') {
            params.append('totalAmount', String(extra.totalAmount));
        }
    }
    const response = await fetch((contextPath || '') + '/reception-dashboard', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: params.toString()
    });
    let json = null;
    try { json = await response.json(); } catch (e) { json = null; }
    if (!response.ok || (json && json.success === false)) {
        throw new Error((json && json.message) ? json.message : 'Không cập nhật được trạng thái');
    }
    return { success: true };
}

async function setReceptionStatus(id, status) {
    const res = await withDataGuard(async function() {
        var action = status === 'approved' ? 'set-status' : 'set-status';
        const baseUrl = (window.RECEPTION_CONTEXT && window.RECEPTION_CONTEXT.baseUrl) ? window.RECEPTION_CONTEXT.baseUrl : '';
        const contextPath = baseUrl ? baseUrl.replace(/\/reception-dashboard.*$/, '') : '';
        const params = new URLSearchParams();
        params.append('action', action);
        params.append('lichHenId', String(id));
        params.append('status', status);
        const response = await fetch((contextPath || '') + '/reception-dashboard', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: params.toString()
        });
        let json = null;
        try { json = await response.json(); } catch (e) { json = null; }
        if (!response.ok || (json && json.success === false)) {
            throw new Error((json && json.message) ? json.message : 'Không cập nhật được trạng thái');
        }
        return { success: true };
    }, status === 'approved' ? 'Đã cập nhật trạng thái xác nhận (đã gỡ khỏi hàng chờ bác sĩ nếu trước đó là Đã đến)'
        : status === 'arrived' ? 'Đã chuyển sang Đã đến — bác sĩ sẽ thấy trong hàng chờ khám'
        : status === 'cancelled' ? 'Đã hủy lịch hẹn'
        : 'Đã chuyển về chờ duyệt');
    if (!res) return;
    window.location.reload();
}

// ==================== THANH TOÁN ====================
function openPaymentModal(id) {
    let apt = appointments.find(a => a.id === id);
    
    if (!apt) {
        showToast('Không tìm thấy lịch hẹn!', 'error');
        return;
    }
    
    if (apt.status !== 'completed' && apt.status !== 'confirmed') {
        showToast('Chỉ có thể thanh toán khi lịch hẹn đã DUYỆT hoặc HOÀN THÀNH!', 'error');
        return;
    }
    
    if (apt.paymentStatus === 'paid') {
        showToast('Lịch hẹn này đã được thanh toán rồi!', 'error');
        return;
    }
    
    currentPaymentAppointment = apt;
    
    let today = new Date();
    document.getElementById('invoiceId').innerText = generateInvoiceId();
    document.getElementById('invoiceDate').innerText = formatDisplayDateShort(today);
    document.getElementById('paymentDate').innerText = formatDisplayDateShort(today);
    document.getElementById('paymentPatientName').innerText = apt.patientName;
    document.getElementById('paymentPatientPhone').innerText = apt.patientPhone;
    document.getElementById('paymentAppointmentDate').innerText = formatDisplayDateShort(apt.date);
    document.getElementById('paymentDoctor').innerText = apt.doctor;
    
    let servicesBody = document.getElementById('invoiceServicesBody');
    servicesBody.innerHTML = apt.services.map(s => `
        <tr>
            <td class="service-name">${escapeHtml(s.name)}</td>
            <td style="text-align: center;">${s.quantity}</td>
            <td class="service-price">${formatPrice(s.price)}đ</td>
            <td class="service-price" style="color: #d97706; font-weight: 600;">${formatPrice(s.price * s.quantity)}đ</td>
        </tr>
    `).join('');
    
    let subtotal = apt.totalPrice;
    let vat = Math.floor(subtotal * 0.1);
    let total = subtotal + vat;
    
    document.getElementById('subtotal').innerHTML = formatPrice(subtotal) + 'đ';
    document.getElementById('vatAmount').innerHTML = formatPrice(vat) + 'đ';
    document.getElementById('totalAmountInvoice').innerHTML = formatPrice(total) + 'đ';
    
    document.getElementById('paymentMethod').value = 'Tiền mặt';
    document.getElementById('paymentModal').style.display = 'flex';
}

async function confirmPayment() {
    if (currentPaymentAppointment) {
        if (isReceptionDashboard()) {
            const totalPay = currentPaymentAppointment.totalWithVat
                || currentPaymentAppointment.totalPrice
                || 0;
            const resR = await withDataGuard(async function() {
                return postReceptionAction('pay', currentPaymentAppointment.id, {
                    paymentMethod: document.getElementById('paymentMethod')?.value || 'Tiền mặt',
                    totalAmount: totalPay
                });
            }, 'Đã thanh toán thành công');
            if (!resR) return;
            closePaymentModal();
            window.location.reload();
            return;
        }
        const payload = {
            appointmentId: currentPaymentAppointment.id,
            paymentMethod: document.getElementById('paymentMethod')?.value || 'Tiền mặt',
            invoiceId: document.getElementById('invoiceId')?.innerText || '',
            paidAt: new Date().toISOString()
        };
        let res = await withDataGuard(() => dataSource.confirmPayment(payload), `Đã thanh toán thành công cho ${currentPaymentAppointment.patientName}`);
        if (!res) return;
        closePaymentModal();
        await loadAppointmentsFromServer();
        currentPaymentAppointment = null;
    }
}

async function openReceptionPaymentModal(id) {
    const baseUrl = (window.RECEPTION_CONTEXT && window.RECEPTION_CONTEXT.baseUrl) ? window.RECEPTION_CONTEXT.baseUrl : '';
    const contextPath = baseUrl ? baseUrl.replace(/\/reception-dashboard.*$/, '') : '';
    const res = await withDataGuard(async function() {
        const params = new URLSearchParams();
        params.append('action', 'get-payment-detail');
        params.append('lichHenId', String(id));
        const response = await fetch((contextPath || '') + '/reception-dashboard', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: params.toString()
        });
        let json = null;
        try { json = await response.json(); } catch (e) { json = null; }
        if (!response.ok || !json || json.success === false) {
            throw new Error((json && json.message) ? json.message : 'Không tải được dữ liệu thanh toán');
        }
        return json;
    });
    if (!res) return;

    const services = Array.isArray(res.items) ? res.items.map(function(it) {
        return { name: it.name, quantity: Number(it.quantity || 1), price: Number(it.unitPrice || 0) };
    }) : [];
    const subtotal = Number(res.subtotal || 0);
    const vat = Math.floor(subtotal * 0.1);
    const total = subtotal + vat;

    currentPaymentAppointment = {
        id: id,
        patientName: res.patientName || '',
        patientPhone: res.patientPhone || '',
        date: res.ngayKham || '',
        doctor: res.doctorName || '',
        services: services,
        totalPrice: subtotal,
        totalWithVat: total
    };

    document.getElementById('invoiceId').innerText = generateInvoiceId();
    document.getElementById('invoiceDate').innerText = formatDisplayDateShort(new Date());
    document.getElementById('paymentDate').innerText = formatDisplayDateShort(new Date());
    document.getElementById('paymentPatientName').innerText = currentPaymentAppointment.patientName;
    document.getElementById('paymentPatientPhone').innerText = currentPaymentAppointment.patientPhone;
    document.getElementById('paymentAppointmentDate').innerText = (res.ngayKham || '') + ' ' + (res.gioKham || '').substring(0,5);
    document.getElementById('paymentDoctor').innerText = currentPaymentAppointment.doctor;
    document.getElementById('invoiceServicesBody').innerHTML = services.map(function(s) {
        return '<tr>'
            + '<td class="service-name">' + escapeHtml(s.name) + '</td>'
            + '<td style="text-align: center;">' + s.quantity + '</td>'
            + '<td class="service-price">' + formatPrice(s.price) + 'đ</td>'
            + '<td class="service-price" style="color: #d97706; font-weight: 600;">' + formatPrice(s.price * s.quantity) + 'đ</td>'
            + '</tr>';
    }).join('');
    document.getElementById('subtotal').innerText = formatPrice(subtotal) + 'đ';
    document.getElementById('vatAmount').innerText = formatPrice(vat) + 'đ';
    document.getElementById('totalAmountInvoice').innerText = formatPrice(total) + 'đ';
    document.getElementById('paymentMethod').value = 'Tiền mặt';
    document.getElementById('paymentModal').style.display = 'flex';
}

// ==================== HIỂN THỊ DỮ LIỆU ====================
function renderAppointments() {
    if (isReceptionDashboard()) return;
    let filterDoctor = document.getElementById('filterDoctor')?.value || '';
    let searchTerm = document.getElementById('searchInput')?.value.toLowerCase() || '';
    
    let weekDates = currentView === 'week' ? getWeekDates(currentDate) : [];
    let monthDates = currentView === 'month' ? getMonthDates(currentDate) : [];
    let currentDateStr = formatDate(currentDate);
    
    let filtered = appointments.filter(apt => {
        if (currentView === 'day' && apt.date !== currentDateStr) return false;
        if (currentView === 'week' && !isInWeek(apt.date, weekDates)) return false;
        if (currentView === 'month' && !isInMonth(apt.date, monthDates)) return false;
        if (filterDoctor && apt.doctor !== filterDoctor) return false;
        if (currentStatusFilter !== 'all' && apt.status !== currentStatusFilter) return false;
        if (searchTerm && !apt.patientName.toLowerCase().includes(searchTerm) && !apt.patientPhone.includes(searchTerm)) return false;
        return true;
    });
    
    filtered.sort((a, b) => a.time.localeCompare(b.time));
    
    let tbody = document.getElementById('appointmentTableBody');
    if (!tbody) return;
    
    tbody.innerHTML = filtered.map(apt => {
        const showPaymentButton = (apt.status === 'completed' || apt.status === 'confirmed') && apt.paymentStatus === 'pending';
        
        return `
        <tr>
            <td><div class="time-badge"><i class="fas fa-clock"></i> ${apt.time}</div></td>
            <td>
                <div class="patient-info">
                    <div class="patient-avatar">${getInitials(apt.patientName)}</div>
                    <div class="patient-details">
                        <h4>${escapeHtml(apt.patientName)}</h4>
                        <p><i class="fas fa-phone"></i> ${apt.patientPhone}</p>
                    </div>
                </div>
            </td>
            <td><div class="doctor-tag"><i class="fas fa-user-md"></i> ${apt.doctor}</div></td>
            <td>
                <div class="services-list">
                    ${apt.services.map(s => `<span class="service-tag"><i class="fas fa-tooth"></i> ${s.name}${s.quantity > 1 ? ` x${s.quantity}` : ''}</span>`).join('')}
                </div>
                <div class="total-price"><i class="fas fa-money-bill-wave"></i> ${formatPrice(apt.totalPrice)}đ</div>
            </td>
            <td><i class="fas fa-door-open"></i> ${apt.room}</td>
            <td>
                <div class="status-dropdown">
                    <div class="status-badge ${getStatusClass(apt.status)}" onclick="toggleStatusMenu(${apt.id})">
                        <i class="fas ${apt.status === 'confirmed' ? 'fa-check-circle' : apt.status === 'pending' ? 'fa-hourglass-half' : apt.status === 'completed' ? 'fa-check-double' : 'fa-times-circle'}"></i> ${getStatusText(apt.status)}
                        <i class="fas fa-chevron-down" style="font-size: 10px; margin-left: 5px;"></i>
                    </div>
                    <div class="status-menu" id="statusMenu_${apt.id}">
                        <div class="status-option" onclick="changeStatus(${apt.id}, 'pending')"><i class="fas fa-hourglass-half" style="color: #f59e0b;"></i> Chờ duyệt</div>
                        <div class="status-option" onclick="changeStatus(${apt.id}, 'confirmed')"><i class="fas fa-check-circle" style="color: #10b981;"></i> Đã duyệt</div>
                        <div class="status-option" onclick="changeStatus(${apt.id}, 'completed')"><i class="fas fa-check-double" style="color: #3b82f6;"></i> Đã hoàn thành</div>
                        <div class="status-option" onclick="changeStatus(${apt.id}, 'cancelled')"><i class="fas fa-times-circle" style="color: #ef4444;"></i> Đã hủy</div>
                    </div>
                </div>
            </td>
            <td>
                ${showPaymentButton ? 
                    `<button class="btn-payment" onclick="openPaymentModal(${apt.id})"><i class="fas fa-credit-card"></i> Thanh toán</button>` : 
                    `<span class="payment-status ${getPaymentStatusClass(apt.paymentStatus)}"><i class="fas ${apt.paymentStatus === 'paid' ? 'fa-check-circle' : 'fa-clock'}"></i> ${getPaymentStatusText(apt.paymentStatus)}</span>`
                }
            </td>
            <td>
                <button class="btn-action" onclick="editAppointment(${apt.id})"><i class="fas fa-edit"></i></button>
                <button class="btn-action" onclick="deleteAppointment(${apt.id})"><i class="fas fa-trash"></i></button>
            </td>
        </tr>
    `}).join('');
    
    updateStats();
}

function updateStats() {
    let total = appointments.length;
    let confirmed = appointments.filter(apt => apt.status === 'confirmed').length;
    let pending = appointments.filter(apt => apt.status === 'pending').length;
    let completed = appointments.filter(apt => apt.status === 'completed').length;
    
}

function filterAppointments() {
    if (isReceptionDashboard()) {
        applyReceptionTableFilters();
        return;
    }
    renderAppointments();
}

// ==================== ĐIỀU KHIỂN NGÀY THÁNG ====================
function changeDate(delta) {
    if (isReceptionDashboard()) {
        shiftReceptionDashboardDate(delta);
        return;
    }
    if (currentView === 'day') {
        currentDate.setDate(currentDate.getDate() + delta);
    } else if (currentView === 'week') {
        currentDate.setDate(currentDate.getDate() + (delta * 7));
    } else if (currentView === 'month') {
        currentDate.setMonth(currentDate.getMonth() + delta);
    }
    updateDateDisplay();
    renderAppointments();
}

function updateDateDisplay() {
    const cdEl = document.getElementById('currentDate');
    if (cdEl && cdEl.dataset.serverFixed === '1') return;
    let displayText = '';
    if (currentView === 'day') {
        displayText = formatDisplayDate(currentDate);
    } else if (currentView === 'week') {
        displayText = `Tuần ${formatWeekRange(currentDate)}`;
    } else if (currentView === 'month') {
        displayText = formatMonthYear(currentDate);
    }
    const out = document.getElementById('currentDate');
    if (out) out.innerText = displayText;
}

function goToday() {
    currentDate = new Date();
    updateDateDisplay();
    renderAppointments();
}

function changeView(view) {
    currentView = view;
    let btns = document.querySelectorAll('.view-options button');
    btns.forEach((btn, i) => {
        let isActive = (view === 'day' && i === 0) || (view === 'week' && i === 1) || (view === 'month' && i === 2);
        btn.classList.toggle('active', isActive);
    });
    updateDateDisplay();
    renderAppointments();
}

// ==================== QUẢN LÝ DỊCH VỤ ====================
function loadServicesGrid() {
    let grid = document.getElementById('servicesGrid');
    if (!grid) return;
    
    grid.innerHTML = servicesList.map(service => `
        <label class="service-checkbox">
            <input type="checkbox" value="${service.id}" data-price="${service.price}" data-name="${service.name}" data-has-quantity="${service.hasQuantity}" data-unit="${service.unit || ''}" onchange="toggleService(this)">
            <span class="service-name">${service.name}</span>
            <span class="service-price">${formatPrice(service.price)}đ</span>
        </label>
    `).join('');
}

function toggleService(checkbox) {
    let id = parseInt(checkbox.value);
    let name = checkbox.getAttribute('data-name');
    let price = parseInt(checkbox.getAttribute('data-price'));
    let hasQuantity = checkbox.getAttribute('data-has-quantity') === 'true';
    let unit = checkbox.getAttribute('data-unit') || '';
    
    if (checkbox.checked) {
        selectedServices.push({ id, name, price, quantity: 1, hasQuantity, unit });
    } else {
        selectedServices = selectedServices.filter(s => s.id !== id);
    }
    updateSelectedServicesDisplay();
}

function increaseQuantity(serviceId) {
    let service = selectedServices.find(s => s.id === serviceId);
    if (service) {
        service.quantity++;
        updateSelectedServicesDisplay();
    }
}

function decreaseQuantity(serviceId) {
    let service = selectedServices.find(s => s.id === serviceId);
    if (service && service.quantity > 1) {
        service.quantity--;
        updateSelectedServicesDisplay();
    }
}

function updateSelectedServicesDisplay() {
    let tagsDiv = document.getElementById('selectedTags');
    let totalSpan = document.getElementById('totalAmount');
    
    if (!tagsDiv || !totalSpan) return;
    
    let total = selectedServices.reduce((sum, s) => sum + (s.price * s.quantity), 0);
    
    tagsDiv.innerHTML = selectedServices.map(s => `
        <div class="selected-service-item">
            <div class="selected-service-header">
                <span class="selected-service-name"><i class="fas fa-tooth"></i> ${s.name}</span>
                <i class="fas fa-times-circle remove-service" onclick="removeService(${s.id})"></i>
            </div>
            ${s.hasQuantity ? `
                <div class="quantity-control">
                    <button type="button" class="qty-btn" onclick="decreaseQuantity(${s.id})">-</button>
                    <input type="number" class="qty-value" value="${s.quantity}" min="1" onchange="updateQuantity(${s.id}, this.value)">
                    <button type="button" class="qty-btn" onclick="increaseQuantity(${s.id})">+</button>
                    <span style="margin-left: 10px; font-size: 0.8rem;">${s.unit}</span>
                </div>
                <div class="service-price-detail">
                    ${formatPrice(s.price)}đ × ${s.quantity} = <strong style="color:#fbbf24">${formatPrice(s.price * s.quantity)}đ</strong>
                </div>
            ` : `
                <div class="service-price-detail">
                    ${formatPrice(s.price)}đ
                </div>
            `}
        </div>
    `).join('');
    
    totalSpan.innerHTML = formatPrice(total);
}

function removeService(serviceId) {
    selectedServices = selectedServices.filter(s => s.id !== serviceId);
    let checkbox = document.querySelector(`.service-checkbox input[value="${serviceId}"]`);
    if (checkbox) checkbox.checked = false;
    updateSelectedServicesDisplay();
}

function updateQuantity(serviceId, value) {
    let quantity = parseInt(value);
    if (isNaN(quantity) || quantity < 1) quantity = 1;
    let service = selectedServices.find(s => s.id === serviceId);
    if (service) {
        service.quantity = quantity;
        updateSelectedServicesDisplay();
    }
}

function resetForm() {
    let form = document.getElementById('appointmentForm');
    if (form) form.reset();
    let today = new Date();
    let todayStr = formatDate(today);
    let dateInput = document.getElementById('appointmentDate');
    if (dateInput) {
        dateInput.value = todayStr;
        dateInput.setAttribute('min', todayStr);
    }
    selectedServices = [];
    let checkboxes = document.querySelectorAll('.service-checkbox input');
    checkboxes.forEach(cb => cb.checked = false);
    updateSelectedServicesDisplay();
}

/** Đặt lịch từ trang BN: đọc sessionStorage và mở modal (reception-dashboard?openBooking=1). */
var RCV_BOOKING_PREFILL_KEY = 'rcv_booking_prefill';

function applyBookingPrefillToForm(data) {
    if (!data) return;
    var pn = document.getElementById('patientName');
    var pp = document.getElementById('patientPhone');
    var nt = document.getElementById('notes');
    var nameVal = data.patientName != null ? String(data.patientName).trim() : '';
    var phoneVal = data.patientPhone != null ? String(data.patientPhone).trim() : '';
    if (pn && nameVal) pn.value = nameVal;
    if (pp && phoneVal) pp.value = phoneVal;
    if (nt && data.notes != null) nt.value = data.notes;
}

function readBookingPrefillPayload() {
    var params = new URLSearchParams(window.location.search || '');
    if (!params.get('openBooking')) return null;

    var data = {
        patientName: params.get('patientName') ? decodeURIComponent(params.get('patientName')) : '',
        patientPhone: params.get('patientPhone') ? decodeURIComponent(params.get('patientPhone')) : '',
        notes: ''
    };

    var raw = null;
    try { raw = sessionStorage.getItem(RCV_BOOKING_PREFILL_KEY); } catch (e) { raw = null; }
    if (raw) {
        try {
            var stored = JSON.parse(raw);
            if (stored) {
                if (!data.patientName && stored.patientName) data.patientName = stored.patientName;
                if (!data.patientPhone && stored.patientPhone) data.patientPhone = stored.patientPhone;
                if (stored.notes) data.notes = stored.notes;
            }
        } catch (err) { /* noop */ }
        try { sessionStorage.removeItem(RCV_BOOKING_PREFILL_KEY); } catch (e2) { /* noop */ }
    }

    if (!data.patientName && !data.patientPhone) return null;
    return data;
}

function cleanBookingPrefillFromUrl() {
    var params = new URLSearchParams(window.location.search || '');
    params.delete('openBooking');
    params.delete('patientName');
    params.delete('patientPhone');
    params.delete('benhNhanId');
    var q = params.toString();
    window.history.replaceState({}, document.title,
        q ? (window.location.pathname + '?' + q + window.location.hash) : (window.location.pathname + window.location.hash));
}

function maybeApplyPatientBookingPrefill() {
    if (!isReceptionDashboard()) return;
    var data = readBookingPrefillPayload();
    if (!data) return;

    cleanBookingPrefillFromUrl();

    var title = document.getElementById('modalTitle');
    if (title && data.patientName) {
        title.innerText = 'Đặt lịch hẹn mới';
    }

    openAddModal(data);

    setTimeout(function () {
        applyBookingPrefillToForm(data);
    }, 0);
    setTimeout(function () {
        applyBookingPrefillToForm(data);
        var modal = document.getElementById('appointmentModal');
        if (modal) modal.style.display = 'flex';
    }, 80);
}

// ==================== CRUD LỊCH HẸN ====================
function openAddModal(prefill) {
    editingId = null;
    document.getElementById('modalTitle').innerText = 'Đặt lịch hẹn mới';
    resetForm();
    if (prefill) {
        applyBookingPrefillToForm(prefill);
    }
    document.getElementById('appointmentModal').style.display = 'flex';
}

function editAppointment(id) {
    let apt = appointments.find(a => a.id === id);
    if (apt) {
        editingId = id;
        document.getElementById('modalTitle').innerText = 'Sửa lịch hẹn';
        document.getElementById('appointmentId').value = apt.id;
        document.getElementById('patientName').value = apt.patientName;
        document.getElementById('patientPhone').value = apt.patientPhone;
        document.getElementById('appointmentDate').value = apt.date;
        document.getElementById('appointmentTime').value = apt.time;
        document.getElementById('doctorName').value = apt.doctor;
        document.getElementById('room').value = apt.room;
        
        selectedServices = apt.services.map(s => ({ ...s }));
        let checkboxes = document.querySelectorAll('.service-checkbox input');
        checkboxes.forEach(cb => {
            let serviceId = parseInt(cb.value);
            cb.checked = selectedServices.some(s => s.id === serviceId);
        });
        updateSelectedServicesDisplay();
        document.getElementById('appointmentModal').style.display = 'flex';
    }
}

async function deleteAppointment(id) {
    const ok = await AppNotify.confirm({ message: 'Bạn có chắc chắn muốn xóa lịch hẹn này?' });
    if (!ok) return;
    let res = await withDataGuard(() => dataSource.removeAppointment(id), 'Đã xóa lịch hẹn');
    if (res) await loadAppointmentsFromServer();
}

async function saveAppointment() {
    let patientName = document.getElementById('patientName').value.trim();
    let patientPhone = document.getElementById('patientPhone').value.trim();
    let date = document.getElementById('appointmentDate').value;
    let time = document.getElementById('appointmentTime').value;
    let doctor = document.getElementById('doctorName').value;
    let room = document.getElementById('room').value;
    
    if (!patientName || !patientPhone || !date || !time || !doctor || !room) {
        showToast('Vui lòng điền đầy đủ thông tin!', 'error');
        return;
    }
    
    if (selectedServices.length === 0) {
        showToast('Vui lòng chọn ít nhất một dịch vụ!', 'error');
        return;
    }
    
    let notes = (document.getElementById('notes') && document.getElementById('notes').value || '').trim();
    let totalPrice = selectedServices.reduce((sum, s) => sum + (s.price * s.quantity), 0);
    
    if (isReceptionDashboard() && !editingId) {
        const baseUrl = (window.RECEPTION_CONTEXT && window.RECEPTION_CONTEXT.baseUrl) ? window.RECEPTION_CONTEXT.baseUrl : '';
        const contextPath = baseUrl ? baseUrl.replace(/\/reception-dashboard.*$/, '') : '';
        const params = new URLSearchParams();
        params.append('patientName', patientName);
        params.append('patientPhone', patientPhone);
        params.append('ngayKham', date);
        params.append('gioKham', time);
        params.append('bacSiId', doctor);
        params.append('phongId', room);
        params.append('ghiChu', notes || selectedServices.map(s => `${s.name}${s.quantity > 1 ? ` x${s.quantity}` : ''}`).join(', '));
        selectedServices.forEach(s => params.append('dichVu', String(s.id)));

        const res = await withDataGuard(async () => {
            const response = await fetch(`${contextPath}/reception-dashboard`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: params.toString()
            });
            if (!response.ok) {
                const msg = await response.text();
                throw new Error(msg || 'Không tạo được lịch hẹn');
            }
            return { success: true };
        }, 'Đã tạo lịch hẹn mới');
        if (!res) return;
        closeModal();
        window.location.href = `${contextPath}/reception-dashboard`;
        return;
    }

    if (isReceptionDashboard() && editingId) {
        const baseUrl = (window.RECEPTION_CONTEXT && window.RECEPTION_CONTEXT.baseUrl) ? window.RECEPTION_CONTEXT.baseUrl : '';
        const contextPath = baseUrl ? baseUrl.replace(/\/reception-dashboard.*$/, '') : '';
        const params = new URLSearchParams();
        params.append('action', 'update-booking');
        params.append('lichHenId', String(editingId));
        params.append('patientName', patientName);
        params.append('patientPhone', patientPhone);
        params.append('ngayKham', date);
        params.append('gioKham', time);
        params.append('bacSiId', doctor);
        params.append('phongId', room);
        params.append('ghiChu', notes);
        selectedServices.forEach(s => params.append('dichVu', String(s.id)));
        const res = await withDataGuard(async () => {
            const response = await fetch(`${contextPath}/reception-dashboard`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: params.toString()
            });
            let json = null;
            try { json = await response.json(); } catch (e) { json = null; }
            if (!response.ok || (json && json.success === false)) {
                throw new Error((json && json.message) ? json.message : 'Không cập nhật được lịch hẹn');
            }
            return { success: true };
        }, 'Đã cập nhật lịch hẹn');
        if (!res) return;
        closeModal();
        window.location.href = `${contextPath}/reception-dashboard`;
        return;
    }

    if (editingId) {
        const payload = {
            id: editingId,
            patientName, patientPhone, date, time, doctor, room,
            services: selectedServices.map(s => ({ ...s })),
            totalPrice: totalPrice
        };
        const res = await withDataGuard(() => dataSource.updateAppointment(payload), 'Đã cập nhật lịch hẹn');
        if (!res) return;
    } else {
        const payload = {
            patientName, patientPhone, date, time, doctor, room,
            services: selectedServices.map(s => ({ ...s })),
            totalPrice: totalPrice,
            status: 'pending',
            paymentStatus: 'pending'
        };
        const res = await withDataGuard(() => dataSource.createAppointment(payload), 'Đã thêm lịch hẹn mới');
        if (!res) return;
    }
    
    closeModal();
    await loadAppointmentsFromServer();
}

function closeModal() {
    document.getElementById('appointmentModal').style.display = 'none';
    editingId = null;
}

function closePaymentModal() {
    document.getElementById('paymentModal').style.display = 'none';
    currentPaymentAppointment = null;
}

function escapeHtml(text) {
    let div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ==================== KHỞI TẠO ====================
document.addEventListener('click', function(e) {
    if (!e.target.closest('.status-dropdown')) {
        document.querySelectorAll('.status-menu').forEach(m => m.classList.remove('show'));
    }
});

function viewDetail(id) {
    if (!isReceptionDashboard()) {
        showToast('Chi tiết lịch hẹn #' + id, 'info');
        return;
    }
    const baseUrl = (window.RECEPTION_CONTEXT && window.RECEPTION_CONTEXT.baseUrl) ? window.RECEPTION_CONTEXT.baseUrl : '';
    const contextPath = baseUrl ? baseUrl.replace(/\/reception-dashboard.*$/, '') : '';
    withDataGuard(async function() {
        const params = new URLSearchParams();
        params.append('action', 'get-detail');
        params.append('lichHenId', String(id));
        const response = await fetch(`${contextPath}/reception-dashboard`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: params.toString()
        });
        let json = null;
        try { json = await response.json(); } catch (e) { json = null; }
        if (!response.ok || !json || json.success === false) {
            throw new Error((json && json.message) ? json.message : 'Không tải được chi tiết lịch hẹn');
        }

        editingId = id;
        document.getElementById('modalTitle').innerText = 'Chi tiết / Sửa lịch hẹn';
        document.getElementById('appointmentId').value = id;
        document.getElementById('patientName').value = json.patientName || '';
        document.getElementById('patientPhone').value = json.patientPhone || '';
        document.getElementById('appointmentDate').value = (json.ngayKham || '');
        document.getElementById('appointmentTime').value = (json.gioKham || '').substring(0,5);
        document.getElementById('doctorName').value = String(json.bacSiId || '');
        document.getElementById('room').value = String(json.phongId || '');
        document.getElementById('notes').value = json.ghiChu || '';

        const qtyById = {};
        if (Array.isArray(json.dichVuItems)) {
            json.dichVuItems.forEach(function(it) {
                if (it && it.id != null) qtyById[String(it.id)] = Math.max(1, Number(it.quantity) || 1);
            });
        }
        const selectedIdSet = new Set((json.dichVuIds || []).map(function(x){ return String(x); }));
        selectedServices = servicesList.filter(function(s){ return selectedIdSet.has(String(s.id)); }).map(function(s){
            return {
                id: s.id, name: s.name, price: s.price,
                quantity: qtyById[String(s.id)] || 1,
                hasQuantity: s.hasQuantity, unit: s.unit
            };
        });
        document.querySelectorAll('.service-checkbox input').forEach(function(cb) {
            cb.checked = selectedIdSet.has(String(cb.value));
        });
        updateSelectedServicesDisplay();
        document.getElementById('appointmentModal').style.display = 'flex';
        return { success: true };
    });
}

function approveAppointment(id) {
    showToast('Duyệt lịch #' + id + ' — vui lòng kết nối servlet cập nhật trạng thái.', 'info');
}

async function deleteReceptionAppointment(id) {
    const ok = await AppNotify.confirm({ message: 'Bạn có chắc muốn xóa lịch hẹn này?' });
    if (!ok) return;
    const res = await withDataGuard(async function() {
        return postReceptionAction('delete', id);
    }, 'Đã xóa lịch hẹn');
    if (!res) return;
    window.location.reload();
}

document.addEventListener('DOMContentLoaded', () => {
    initServicesList();
    initRoomOptions();
    console.info('[script.js] mode:', APPOINTMENT_CONFIG.USE_MOCK ? 'MOCK' : 'REAL API');
    if (isReceptionDashboard() && window.RECEPTION_CONTEXT && window.RECEPTION_CONTEXT.selectedDate) {
        currentDate = new Date(window.RECEPTION_CONTEXT.selectedDate + 'T12:00:00');
        currentView = 'day';
    }
    updateDateDisplay();
    loadServicesFromServer();
    if (!isReceptionDashboard()) {
        loadAppointmentsFromServer();
    }
    if (isReceptionDashboard()) {
        applyReceptionTableFilters();
    } else {
        filterByStatus('all');
    }
    
    let today = new Date();
    let todayStr = formatDate(today);
    let dateInput = document.getElementById('appointmentDate');
    if (dateInput) dateInput.setAttribute('min', todayStr);
    
    // Gán sự kiện cho các nút
    let prevDayBtn = document.getElementById('prevDayBtn');
    let nextDayBtn = document.getElementById('nextDayBtn');
    let todayBtn = document.getElementById('todayBtn');
    let addBtn = document.getElementById('addAppointmentBtn');
    
    if (prevDayBtn) prevDayBtn.onclick = () => changeDate(-1);
    if (nextDayBtn) nextDayBtn.onclick = () => changeDate(1);
    if (todayBtn) {
        if (isReceptionDashboard()) {
            const panel = document.getElementById('receptionCalendarPanel');
            const pick = document.getElementById('receptionDatePick');
            const applyBtn = document.getElementById('receptionCalendarApply');
            todayBtn.onclick = (ev) => {
                ev.preventDefault();
                ev.stopPropagation();
                if (!panel) return;
                const open = panel.style.display !== 'none';
                panel.style.display = open ? 'none' : 'block';
                if (!open && pick && window.RECEPTION_CONTEXT) pick.value = window.RECEPTION_CONTEXT.selectedDate;
            };
            if (applyBtn) {
                applyBtn.onclick = () => {
                    const v = pick && pick.value;
                    if (!v) {
                        showToast('Vui lòng chọn ngày', 'error');
                        return;
                    }
                    const ctx = window.RECEPTION_CONTEXT;
                    if (!ctx) return;
                    window.location.href = buildReceptionDashboardUrl(v, ctx);
                };
            }
            document.addEventListener('click', (e) => {
                if (!panel || panel.style.display === 'none') return;
                if (todayBtn.contains(e.target)) return;
                if (panel.contains(e.target)) return;
                panel.style.display = 'none';
            });
        } else {
            todayBtn.onclick = goToday;
        }
    }
    if (addBtn) addBtn.onclick = openAddModal;
    
    maybeApplyPatientBookingPrefill();

    // View options
    let viewBtns = document.querySelectorAll('.view-options button');
    viewBtns.forEach(btn => {
        btn.onclick = () => changeView(btn.getAttribute('data-view'));
    });
    
    // Filter
    let filterDoctor = document.getElementById('filterDoctor');
    let searchInput = document.getElementById('searchInput');
    if (filterDoctor) filterDoctor.onchange = filterAppointments;
    if (searchInput) searchInput.onkeyup = filterAppointments;
    
    // Modal close buttons
    let closeBtns = document.querySelectorAll('.close, .close-payment');
    closeBtns.forEach(btn => {
        btn.onclick = function() {
            document.getElementById('appointmentModal').style.display = 'none';
            document.getElementById('paymentModal').style.display = 'none';
        };
    });
    
    let cancelBtns = document.querySelectorAll('.btn-cancel');
    cancelBtns.forEach(btn => {
        btn.onclick = function() {
            document.getElementById('appointmentModal').style.display = 'none';
            document.getElementById('paymentModal').style.display = 'none';
        };
    });
    
    let saveBtn = document.querySelector('.btn-save');
    if (saveBtn) saveBtn.onclick = saveAppointment;
    
    let confirmPaymentBtn = document.getElementById('confirmPaymentBtn');
    if (confirmPaymentBtn) confirmPaymentBtn.onclick = confirmPayment;
    
    // Click outside modal
    window.onclick = (e) => { 
        if (e.target.classList.contains('modal')) {
            document.getElementById('appointmentModal').style.display = 'none';
            document.getElementById('paymentModal').style.display = 'none';
        }
    };
});