var BOOKING_CONFIG = {
    USE_MOCK: true,
    API_BASE: '/api',
    MOCK_DELAY_MS: 120
};

function bookingDelay(ms) {
    return new Promise(function(resolve) { setTimeout(resolve, ms); });
}

function bookingClone(data) {
    return JSON.parse(JSON.stringify(data));
}

async function bookingRequest(path, options) {
    var opts = options || {};
    var method = opts.method || 'GET';
    var body = opts.body;
    var headers = opts.headers || {};
    var fetchOptions = {
        method: method,
        headers: Object.assign({'Content-Type': 'application/json'}, headers)
    };
    if (body !== undefined && body !== null) fetchOptions.body = JSON.stringify(body);

    var res = await fetch(BOOKING_CONFIG.API_BASE + '/' + path, fetchOptions);
    var json = null;
    try { json = await res.json(); } catch (e) { json = null; }
    if (!res.ok) throw new Error((json && json.message) ? json.message : ('Lỗi HTTP: ' + res.status));
    return json;
}

function normalizeBookingList(res) {
    if (!res) return [];
    if (Array.isArray(res)) return res;
    if (Array.isArray(res.data)) return res.data;
    return [];
}

var mockBookingSource = {
    servicesList: async function() {
        return bookingClone(window.allServices || []);
    },
    createBooking: async function(payload) {
        await bookingDelay(BOOKING_CONFIG.MOCK_DELAY_MS);
        console.info('[dat-lich] mock booking created:', payload);
        return {success:true};
    }
};

var apiBookingSource = {
    servicesList: async function() { return normalizeBookingList(await bookingRequest('services')); },
    createBooking: async function(payload) { return bookingRequest('appointments/add', {method:'POST', body:payload}); }
};

var bookingSource = BOOKING_CONFIG.USE_MOCK ? mockBookingSource : apiBookingSource;

async function withBookingGuard(action, successMsg) {
    try {
        var res = await action();
        if (res && res.success === false) {
            alert(res.message || 'Không thể xử lý đặt lịch');
            return null;
        }
        if (successMsg) alert(successMsg);
        return res || {success:true};
    } catch (error) {
        console.error('[dat-lich] data error:', error);
        alert(error.message || 'Có lỗi kết nối dữ liệu!');
        return null;
    }
}

// Dùng data được inject từ backend (BookingServlet)
var allServices = (window.allServices && window.allServices.length > 0)
    ? window.allServices
    : [];

function normalizeServiceItem(s) {
    if (!s) return null;
    var id = String(s.id != null ? s.id : '');
    if (!id) return null;
    return {
        id: id,
        name: s.name || '',
        desc: s.desc || s.name || '',
        time: s.time || '',
        price: Number(s.price || 0),
        cat: s.cat || 'all',
        perUnit: Boolean(s.perUnit),
        unit: s.unit || ''
    };
}

allServices = allServices.map(normalizeServiceItem).filter(function(s) { return s !== null; });


// === RENDER BANG DICH VU ===
function renderServiceTable(list, showAll) {
    var tbody = document.getElementById('serviceTableBody');
    var html = '';
    list.forEach(function(s, i) {
        var cls = (!showAll && i >= 10) ? ' class="hidden-row"' : '';
        html += '<tr' + cls + '><td>' + (i+1) + '</td><td>' + s.name + '<br><small style="color:var(--text-muted)">' + s.desc + '</small></td><td class="time-cell">🕐 ' + s.time + '</td><td class="price">' + s.price.toLocaleString('vi-VN') + '</td></tr>';
    });
    tbody.innerHTML = html;
}

var servicesExpanded = false;
function filterServices() {
    var search = document.getElementById('serviceSearch').value.toLowerCase();
    var cat = document.getElementById('serviceFilter').value;
    var filtered = allServices.filter(function(s) {
        return (cat === 'all' || s.cat === cat) && (!search || s.name.toLowerCase().indexOf(search) > -1 || s.desc.toLowerCase().indexOf(search) > -1);
    });
    renderServiceTable(filtered, true);
    document.getElementById('showMoreBtn').style.display = filtered.length <= 10 ? 'none' : 'inline-flex';
}

function toggleServices() {
    servicesExpanded = !servicesExpanded;
    document.querySelectorAll('.hidden-row').forEach(function(r) {
        r.style.display = servicesExpanded ? 'table-row' : 'none';
    });
    document.getElementById('showMoreBtn').textContent = servicesExpanded ? 'Thu gọn ↑' : 'Xem thêm dịch vụ ↓';
}

renderServiceTable(allServices, false);

// === BOOKING FORM - CHECKBOX GRID ===
var selectedList = [];

function buildCheckboxGrid() {
    var grid = document.getElementById('svcCheckboxGrid');
    var html = '';
    allServices.forEach(function(s) {
        html += '<label class="svc-cb-item">';
        html += '<input type="checkbox" value="' + s.id + '" onchange="onSvcCheck(this,\'' + s.id + '\')">';
        html += '<span class="svc-cb-name">' + s.name + '</span>';
        html += '<span class="svc-cb-price">' + s.price.toLocaleString('vi-VN') + 'đ</span>';
        html += '</label>';
    });
    grid.innerHTML = html;
}
buildCheckboxGrid();

function onSvcCheck(cb, id) {
    var idStr = String(id);
    var svc = allServices.find(function(s) { return String(s.id) === idStr; });
    if (!svc) return;
    if (cb.checked) {
        selectedList.push({id: svc.id, name: svc.name, price: svc.price, time: svc.time, qty: 1, perUnit: svc.perUnit, unit: svc.unit || ''});
    } else {
        selectedList = selectedList.filter(function(s) { return String(s.id) !== idStr; });
    }
    renderNbSelected();
    document.getElementById('serviceGroup').classList.remove('error');
}

function changeQty(id, delta) {
    var item = selectedList.find(function(s) { return s.id === id; });
    if (!item) return;
    item.qty = Math.max(1, item.qty + delta);
    renderNbSelected();
}

function removeNbItem(id) {
    var idStr = String(id);
    selectedList = selectedList.filter(function(s) { return String(s.id) !== idStr; });
    var cb = document.querySelector('#svcCheckboxGrid input[value="' + idStr + '"]');
    if (cb) cb.checked = false;
    renderNbSelected();
}

function renderNbSelected() {
    var box = document.getElementById('selectedServicesBox');
    var list = document.getElementById('nbSelectedList');
    if (selectedList.length === 0) {
        box.style.display = 'none';
        return;
    }
    box.style.display = 'block';
    var html = '', total = 0;
    selectedList.forEach(function(s) {
        var sub = s.price * s.qty;
        total += sub;
        html += '<div class="nb-svc-item">';
        html += '<button type="button" class="nb-svc-remove" onclick="removeNbItem(\'' + s.id + '\')" title="Xóa">✕</button>';
        html += '<span class="nb-svc-name">🦷 ' + s.name + '</span>';
        html += '<span class="nb-svc-time">⏱ ' + s.time + '</span>';
        if (s.perUnit) {
            html += '<div class="nb-qty-row">';
            html += '<button type="button" class="nb-qty-btn" onclick="changeQty(\'' + s.id + '\',-1)">-</button>';
            html += '<span class="nb-qty-val">' + s.qty + '</span>';
            html += '<button type="button" class="nb-qty-btn" onclick="changeQty(\'' + s.id + '\',1)">+</button>';
            html += '<span class="nb-qty-unit">' + s.unit + '</span>';
            html += '</div>';
            html += '<div class="nb-svc-calc">' + s.price.toLocaleString('vi-VN') + 'đ × ' + s.qty + ' = <strong>' + sub.toLocaleString('vi-VN') + 'đ</strong></div>';
        } else {
            html += '<div class="nb-svc-calc"><strong>' + sub.toLocaleString('vi-VN') + 'đ</strong></div>';
        }
        html += '</div>';
    });
    list.innerHTML = html;
    document.getElementById('nbTotalValue').textContent = total.toLocaleString('vi-VN');
}

// === NGAY + GIO ===
(function() {
    document.getElementById('bookingDate').min = new Date().toISOString().split('T')[0];
})();

var weekdayTimes = ['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30','13:00','13:30','14:00','14:30','15:00','15:30','16:00','16:30'];
var sundayTimes = ['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30'];

function buildTimeOptions(times) {
    var sel = document.getElementById('bookingTimeSelect');
    sel.innerHTML = '<option value="">Chọn giờ</option>';
    times.forEach(function(t) {
        sel.innerHTML += '<option value="' + t + '">' + t + '</option>';
    });
}
buildTimeOptions(weekdayTimes);

document.getElementById('bookingDate').addEventListener('change', function() {
    var day = new Date(this.value).getDay();
    buildTimeOptions(day === 0 ? sundayTimes : weekdayTimes);
    document.getElementById('dateGroup').classList.remove('error');
});

document.getElementById('bookingTimeSelect').addEventListener('change', function() {
    if (this.value) document.getElementById('timeGroup').classList.remove('error');
});

// === RESET + SUBMIT ===
function resetBookingForm() {
    selectedList = [];
    document.querySelectorAll('#svcCheckboxGrid input[type="checkbox"]').forEach(function(cb) { cb.checked = false; });
    renderNbSelected();
    document.getElementById('bookingDate').value = '';
    document.getElementById('bookingTimeSelect').value = '';
    document.getElementById('bookingNote').value = '';
    document.getElementById('dateGroup').classList.remove('error');
    document.getElementById('timeGroup').classList.remove('error');
    document.getElementById('serviceGroup').classList.remove('error');
}

function handleBooking() {
    var ok = true;
    if (selectedList.length === 0) {
        document.getElementById('serviceGroup').classList.add('error');
        ok = false;
    } else document.getElementById('serviceGroup').classList.remove('error');

    if (!document.getElementById('bookingDate').value) {
        document.getElementById('dateGroup').classList.add('error');
        ok = false;
    } else document.getElementById('dateGroup').classList.remove('error');

    if (!document.getElementById('bookingTimeSelect').value) {
        document.getElementById('timeGroup').classList.add('error');
        ok = false;
    } else document.getElementById('timeGroup').classList.remove('error');

    if (ok) submitBooking();
}

async function submitBooking() {
    var bookingDate = document.getElementById('bookingDate').value;
    var bookingTime = document.getElementById('bookingTimeSelect').value;
    var bookingNote = document.getElementById('bookingNote').value.trim();
    var totalAmount = selectedList.reduce(function(sum, s){ return sum + (s.price * s.qty); }, 0);

    var payload = {
        date: bookingDate,
        time: bookingTime,
        note: bookingNote,
        services: selectedList.map(function(s) {
            return {
                id: s.id,
                name: s.name,
                price: s.price,
                quantity: s.qty
            };
        }),
        totalAmount: totalAmount
    };

    var res = await withBookingGuard(
        function() { return bookingSource.createBooking(payload); },
        '✅ Đặt lịch thành công! Phòng khám sẽ liên hệ xác nhận trong 30 phút.'
    );
    if (res) resetBookingForm();
}

async function loadServicesForBooking() {
    var list = await withBookingGuard(function() { return bookingSource.servicesList(); });
    if (!list) return;
    allServices = normalizeBookingList(list).map(normalizeServiceItem).filter(function(s) { return s !== null; });
    renderServiceTable(allServices, false);
    buildCheckboxGrid();
}

document.addEventListener('DOMContentLoaded', function() {
    console.info('[dat-lich] mode:', BOOKING_CONFIG.USE_MOCK ? 'MOCK' : 'REAL API');
    loadServicesForBooking();
});