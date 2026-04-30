// ==================== DỮ LIỆU MẪU ====================
const servicesList = [
    { id: 1, name: "Khám tổng quát", price: 100000, hasQuantity: false },
    { id: 2, name: "Cạo vôi răng", price: 200000, hasQuantity: false },
    { id: 3, name: "Trám răng Composite", price: 300000, hasQuantity: false },
    { id: 4, name: "Nhổ răng sữa", price: 100000, hasQuantity: true, unit: "răng" },
    { id: 5, name: "Nhổ răng khôn", price: 1000000, hasQuantity: true, unit: "răng" },
    { id: 6, name: "Nhổ răng khôn mọc ngầm", price: 3000000, hasQuantity: true, unit: "răng" },
    { id: 7, name: "Tẩy trắng răng Laser", price: 2500000, hasQuantity: false },
    { id: 8, name: "Tẩy trắng răng tại nhà", price: 1500000, hasQuantity: false },
    { id: 9, name: "Lấy tủy răng", price: 800000, hasQuantity: true, unit: "răng" },
    { id: 10, name: "Bọc răng sứ Titan", price: 2000000, hasQuantity: true, unit: "răng" },
    { id: 11, name: "Bọc răng sứ Cercon", price: 5000000, hasQuantity: true, unit: "răng" },
    { id: 12, name: "Bọc răng sứ Zirconia", price: 6000000, hasQuantity: true, unit: "răng" },
    { id: 13, name: "Mặt dán sứ Veneer", price: 7000000, hasQuantity: true, unit: "răng" },
    { id: 14, name: "Cấy ghép Implant", price: 15000000, hasQuantity: true, unit: "trụ" },
    { id: 15, name: "Niềng răng mắc cài kim loại", price: 25000000, hasQuantity: false },
    { id: 16, name: "Niềng răng Invisalign", price: 80000000, hasQuantity: false },
    { id: 17, name: "Điều trị viêm nha chu", price: 500000, hasQuantity: false }
];

let appointments = [
    { id: 1, time: "08:00", patientName: "Nguyễn Văn An", patientPhone: "0901000001", doctor: "BS. Nguyễn Hải", services: [{ id: 1, name: "Khám tổng quát", price: 100000, quantity: 1 }], totalPrice: 100000, room: "Phòng 1", date: "2026-04-22", status: "confirmed", paymentStatus: "paid" },
    { id: 2, time: "09:00", patientName: "Trần Thị Bích", patientPhone: "0901000002", doctor: "BS. Trần Tâm", services: [{ id: 15, name: "Niềng răng mắc cài kim loại", price: 25000000, quantity: 1 }], totalPrice: 25000000, room: "Phòng 2", date: "2026-04-22", status: "confirmed", paymentStatus: "pending" },
    { id: 3, time: "10:00", patientName: "Lê Hoàng Nam", patientPhone: "0901000003", doctor: "BS. Lê Quang", services: [{ id: 5, name: "Nhổ răng khôn", price: 1000000, quantity: 2 }], totalPrice: 2000000, room: "Phòng 3", date: "2026-04-22", status: "pending", paymentStatus: "pending" },
    { id: 4, time: "14:00", patientName: "Phạm Thu Hà", patientPhone: "0901000004", doctor: "BS. Phạm Hương", services: [{ id: 7, name: "Tẩy trắng răng Laser", price: 2500000, quantity: 1 }], totalPrice: 2500000, room: "Phòng 1", date: "2026-04-22", status: "confirmed", paymentStatus: "paid" },
    { id: 5, time: "15:30", patientName: "Hoàng Vĩnh Khang", patientPhone: "0901000005", doctor: "BS. Hoàng Quân", services: [{ id: 14, name: "Cấy ghép Implant", price: 15000000, quantity: 1 }], totalPrice: 15000000, room: "Phòng 4", date: "2026-04-22", status: "completed", paymentStatus: "pending" },
    { id: 6, time: "08:30", patientName: "Đặng Phương Hoa", patientPhone: "0901000006", doctor: "BS. Nguyễn Hải", services: [{ id: 4, name: "Nhổ răng sữa", price: 100000, quantity: 3 }, { id: 1, name: "Khám tổng quát", price: 100000, quantity: 1 }], totalPrice: 400000, room: "Phòng 1", date: "2026-04-23", status: "pending", paymentStatus: "pending" },
    { id: 7, time: "13:30", patientName: "Nguyễn Thị Lan", patientPhone: "0901000007", doctor: "BS. Phạm Hương", services: [{ id: 7, name: "Tẩy trắng răng Laser", price: 2500000, quantity: 1 }], totalPrice: 2500000, room: "Phòng 1", date: "2026-04-21", status: "completed", paymentStatus: "pending" },
    { id: 8, time: "16:00", patientName: "Trần Văn Nam", patientPhone: "0901000008", doctor: "BS. Nguyễn Hải", services: [{ id: 3, name: "Trám răng Composite", price: 300000, quantity: 2 }], totalPrice: 600000, room: "Phòng 1", date: "2026-04-20", status: "completed", paymentStatus: "paid" }
];

// ==================== BIẾN TOÀN CỤC ====================
let currentDate = new Date();
let currentView = 'day';
let editingId = null;
let selectedServices = [];
let currentStatusFilter = 'all';
let currentPaymentAppointment = null;

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
    currentStatusFilter = status;
    document.querySelectorAll('.stat-card').forEach(card => card.classList.remove('active'));
    if (status === 'all') document.getElementById('statAll').classList.add('active');
    else if (status === 'confirmed') document.getElementById('statConfirmed').classList.add('active');
    else if (status === 'pending') document.getElementById('statPending').classList.add('active');
    else if (status === 'completed') document.getElementById('statCompleted').classList.add('active');
    renderAppointments();
}

function changeStatus(id, newStatus) {
    let apt = appointments.find(a => a.id === id);
    if (apt) {
        apt.status = newStatus;
        renderAppointments();
        let statusText = newStatus === 'confirmed' ? 'duyệt' : newStatus === 'cancelled' ? 'hủy' : newStatus === 'completed' ? 'hoàn thành' : 'cập nhật';
        showToast(`Đã ${statusText} lịch hẹn của ${apt.patientName}`);
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

function confirmPayment() {
    if (currentPaymentAppointment) {
        currentPaymentAppointment.paymentStatus = 'paid';
        renderAppointments();
        closePaymentModal();
        showToast(`Đã thanh toán thành công cho ${currentPaymentAppointment.patientName}`);
        currentPaymentAppointment = null;
    }
}

// ==================== HIỂN THỊ DỮ LIỆU ====================
function renderAppointments() {
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
    
    document.getElementById('totalAppointments').innerText = total;
    document.getElementById('confirmedCount').innerText = confirmed;
    document.getElementById('pendingCount').innerText = pending;
    document.getElementById('completedCount').innerText = completed;
}

function filterAppointments() { renderAppointments(); }

// ==================== ĐIỀU KHIỂN NGÀY THÁNG ====================
function changeDate(delta) {
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
    let displayText = '';
    if (currentView === 'day') {
        displayText = formatDisplayDate(currentDate);
    } else if (currentView === 'week') {
        displayText = `Tuần ${formatWeekRange(currentDate)}`;
    } else if (currentView === 'month') {
        displayText = formatMonthYear(currentDate);
    }
    document.getElementById('currentDate').innerText = displayText;
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

// ==================== CRUD LỊCH HẸN ====================
function openAddModal() {
    editingId = null;
    document.getElementById('modalTitle').innerText = 'Đặt lịch hẹn mới';
    resetForm();
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

function deleteAppointment(id) {
    if (confirm('Bạn có chắc chắn muốn xóa lịch hẹn này?')) {
        appointments = appointments.filter(a => a.id !== id);
        renderAppointments();
        showToast('Đã xóa lịch hẹn', 'success');
    }
}

function saveAppointment() {
    let patientName = document.getElementById('patientName').value.trim();
    let patientPhone = document.getElementById('patientPhone').value.trim();
    let date = document.getElementById('appointmentDate').value;
    let time = document.getElementById('appointmentTime').value;
    let doctor = document.getElementById('doctorName').value;
    let room = document.getElementById('room').value;
    
    if (!patientName || !patientPhone || !date || !time || !doctor || !room) {
        alert('Vui lòng điền đầy đủ thông tin!');
        return;
    }
    
    if (selectedServices.length === 0) {
        alert('Vui lòng chọn ít nhất một dịch vụ!');
        return;
    }
    
    let totalPrice = selectedServices.reduce((sum, s) => sum + (s.price * s.quantity), 0);
    
    if (editingId) {
        let index = appointments.findIndex(a => a.id === editingId);
        if (index !== -1) {
            appointments[index] = { 
                ...appointments[index], 
                patientName, patientPhone, date, time, doctor, room,
                services: selectedServices.map(s => ({ ...s })),
                totalPrice: totalPrice
            };
            showToast('Đã cập nhật lịch hẹn', 'success');
        }
    } else {
        let newId = Math.max(...appointments.map(a => a.id), 0) + 1;
        appointments.push({ 
            id: newId, patientName, patientPhone, date, time, doctor, room,
            services: selectedServices.map(s => ({ ...s })),
            totalPrice: totalPrice,
            status: 'pending',
            paymentStatus: 'pending'
        });
        showToast('Đã thêm lịch hẹn mới', 'success');
    }
    
    closeModal();
    renderAppointments();
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

document.addEventListener('DOMContentLoaded', () => {
    updateDateDisplay();
    loadServicesGrid();
    renderAppointments();
    filterByStatus('all');
    
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
    if (todayBtn) todayBtn.onclick = goToday;
    if (addBtn) addBtn.onclick = openAddModal;
    
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