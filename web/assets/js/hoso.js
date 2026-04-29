var svcList20 = [
    {id:'1', name:'Khám tổng quát', time:'20 phút', price:100000, perUnit:false, unit:''},
    {id:'2', name:'Cạo vôi răng', time:'30 phút', price:200000, perUnit:false, unit:''},
    {id:'3', name:'Trám răng Composite', time:'30 phút', price:300000, perUnit:true, unit:'răng'},
    {id:'4', name:'Nhổ răng sữa', time:'15 phút', price:100000, perUnit:true, unit:'răng'},
    {id:'5', name:'Nhổ răng khôn mọc thẳng', time:'30 phút', price:1000000, perUnit:true, unit:'răng'},
    {id:'6', name:'Nhổ răng khôn mọc ngầm', time:'60 phút', price:3000000, perUnit:true, unit:'răng'},
    {id:'7', name:'Tẩy trắng răng Laser', time:'45 phút', price:2500000, perUnit:false, unit:''},
    {id:'8', name:'Tẩy trắng răng tại nhà', time:'20 phút', price:1500000, perUnit:false, unit:''},
    {id:'9', name:'Lấy tủy răng cửa', time:'45 phút', price:800000, perUnit:true, unit:'răng'},
    {id:'10', name:'Lấy tủy răng hàm', time:'60 phút', price:1500000, perUnit:true, unit:'răng'},
    {id:'11', name:'Bọc răng sứ Titan', time:'45 phút/răng', price:2000000, perUnit:true, unit:'răng'},
    {id:'12', name:'Bọc răng sứ Cercon', time:'45 phút/răng', price:5000000, perUnit:true, unit:'răng'},
    {id:'13', name:'Bọc răng sứ Zirconia', time:'45 phút/răng', price:6000000, perUnit:true, unit:'răng'},
    {id:'14', name:'Mặt dán sứ Veneer', time:'60 phút/răng', price:7000000, perUnit:true, unit:'răng'},
    {id:'15', name:'Cấy ghép Implant tiêu chuẩn', time:'60-90 phút', price:15000000, perUnit:true, unit:'trụ'},
    {id:'16', name:'Cấy ghép Implant cao cấp', time:'60-90 phút', price:30000000, perUnit:true, unit:'trụ'},
    {id:'17', name:'Niềng răng mắc cài kim loại', time:'60 phút/lần', price:25000000, perUnit:false, unit:''},
    {id:'18', name:'Niềng răng mắc cài sứ', time:'60 phút/lần', price:35000000, perUnit:false, unit:''},
    {id:'19', name:'Niềng răng Invisalign', time:'45 phút/lần', price:80000000, perUnit:false, unit:''},
    {id:'20', name:'Điều trị viêm nha chu', time:'30 phút', price:500000, perUnit:false, unit:''}
];

// === DATA LICH HEN ===
var appointmentDetails = {
    1: {
        status:'paid', statusText:'Đã khám', date:'01/03/2024', time:'08:00',
        customerNote:'Muốn cạo vôi định kỳ.',
        doctorName:'BS. Nguyễn Hải', doctorSpec:'Tổng quát', doctorDegree:'CKI',
        doctorAvatar:'https://images.unsplash.com/photo-1612349317150-e413f6a5b16d?w=100&h=100&fit=crop',
        room:'Phòng 101',
        diagnosis:'Vôi răng nhiều, cần cạo vôi tổng quát.',
        doctorNote:'Tái khám sau 6 tháng.',
        services:[
            {id:'1', name:'Khám tổng quát', price:100000, time:'20 phút', qty:1, perUnit:false, unit:''},
            {id:'2', name:'Cạo vôi răng', price:200000, time:'30 phút', qty:1, perUnit:false, unit:''}
        ],
        total:300000
    },
    2: {
        status:'confirmed', statusText:'Đã xác nhận', date:'15/04/2024', time:'09:30',
        customerNote:'Răng cửa mọc lệch.',
        doctorName:'BS. Trần Tâm', doctorSpec:'Chỉnh nha', doctorDegree:'Thạc sĩ',
        doctorAvatar:'https://images.unsplash.com/photo-1559839734-2b71ea197ec2?w=100&h=100&fit=crop',
        room:'Phòng 203',
        diagnosis:null, doctorNote:null,
        services:[
            {id:'1', name:'Khám tổng quát', price:100000, time:'20 phút', qty:1, perUnit:false, unit:''}
        ],
        total:100000
    },
    3: {
        status:'pending', statusText:'Chờ xác nhận', date:'20/05/2024', time:'14:00',
        customerNote:'Muốn bọc sứ 2 răng cửa và tẩy trắng.',
        doctorName:null, doctorSpec:null, doctorDegree:null, doctorAvatar:null,
        room:null, diagnosis:null, doctorNote:null,
        services:[
            {id:'13', name:'Bọc răng sứ Zirconia', price:6000000, time:'45 phút/răng', qty:2, perUnit:true, unit:'răng'},
            {id:'7', name:'Tẩy trắng răng Laser', price:2500000, time:'45 phút', qty:1, perUnit:false, unit:''}
        ],
        total:14500000
    }
};

var currentCancelId = null;
var currentEditId = null;

// === TAB SWITCH ===
function switchTab(t, el) {
    document.querySelectorAll('.tab-content').forEach(function(c) { c.classList.remove('active'); });
    document.querySelectorAll('.sidebar-menu-item').forEach(function(i) { i.classList.remove('active'); });
    document.getElementById('tab-' + t).classList.add('active');
    el.classList.add('active');
}

// === FILTER HISTORY ===
function filterHistory(s, btn) {
    document.querySelectorAll('.filter-chip').forEach(function(c) { c.classList.remove('active'); });
    btn.classList.add('active');
    var stt = 0;
    document.querySelectorAll('#historyTable tbody tr').forEach(function(r) {
        var ok = s === 'all' || r.getAttribute('data-status') === s;
        if (ok) {
            stt++;
            r.style.display = '';
            r.querySelector('.stt-cell').textContent = stt;
        } else {
            r.style.display = 'none';
        }
    });
}

// === EDIT INFO MODAL ===
function openEditModal() { document.getElementById('editInfoModal').classList.add('show'); }
function closeEditModal() { document.getElementById('editInfoModal').classList.remove('show'); }
function saveEditInfo() {
    var phone = document.getElementById('editPhone').value.trim();
    var email = document.getElementById('editEmail').value.trim();
    if (!phone && !email) { alert('Cần ít nhất SĐT hoặc Email'); return; }
    document.getElementById('dispPhone').textContent = phone || 'Chưa cập nhật';
    document.getElementById('dispEmail').textContent = email || 'Chưa cập nhật';
    var dob = document.getElementById('editDob').value;
    if (dob) {
        var p = dob.split('-');
        document.getElementById('dispDob').textContent = p[2] + '/' + p[1] + '/' + p[0];
    }
    document.getElementById('dispGender').textContent = document.getElementById('editGender').value;
    alert('✅ Cập nhật thành công!');
    closeEditModal();
}

// === DETAIL MODAL (xem phieu kham) ===
function showDetail(id) {
    var d = appointmentDetails[id];
    if (!d) return;
    var isPaid = d.status === 'paid';

    var html = '<div class="receipt-hero"><span class="receipt-status-pill ' + d.status + '">' + d.statusText + '</span><h3>Chi tiết phiếu khám</h3><div class="receipt-subtitle">Nha Khoa Kvone - 48 Cao Thắng, Đà Nẵng</div></div><div class="receipt-content">';

    // Lich hen
    html += '<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">📅</span> Lịch hẹn</div><div class="info-row">';
    html += '<div class="info-cell"><div class="cell-icon">🗓️</div><div class="cell-text"><small>Ngày</small><strong>' + d.date + '</strong></div></div>';
    html += '<div class="info-cell"><div class="cell-icon">🕐</div><div class="cell-text"><small>Giờ</small><strong>' + d.time + '</strong></div></div>';
    html += '<div class="info-cell"><div class="cell-icon">🏥</div><div class="cell-text"><small>Phòng</small><strong>' + (d.room || '<i style="color:#8c8c9a">Chờ phân phòng</i>') + '</strong></div></div>';
    html += '</div></div>';

    // Bac si
    if (d.doctorName) {
        html += '<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">👨‍⚕️</span> Bác sĩ</div><div class="doctor-card-mini"><div class="doctor-avatar-mini"><img src="' + d.doctorAvatar + '"></div><div class="doctor-details"><h4>' + d.doctorName + '</h4><p>' + d.doctorDegree + '</p><span class="specialty-tag">' + d.doctorSpec + '</span></div></div></div>';
    } else {
        html += '<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">👨‍⚕️</span> Bác sĩ</div><div class="medical-empty">⏳ Đang chờ phân bác sĩ...</div></div>';
    }

    // Ghi chu cua khach
    if (d.customerNote) {
        html += '<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">📝</span> Ghi chú của bạn</div><div class="medical-item"><div class="medical-text">' + d.customerNote + '</div></div></div>';
    }

    // Ket qua kham (chi khi da khAM)
    if (isPaid && d.diagnosis) {
        html += '<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">🩺</span> Kết quả khám</div><div class="medical-item"><div class="medical-label">Chẩn đoán</div><div class="medical-text diagnosis">' + d.diagnosis + '</div></div>';
        if (d.doctorNote) {
            html += '<div class="medical-item"><div class="medical-label">Ghi chú bác sĩ</div><div class="medical-text">' + d.doctorNote + '</div></div>';
        }
        html += '</div>';
    }

    // Dich vu
    html += '<div class="receipt-card"><div class="receipt-card-title"><span class="title-icon">💎</span> Dịch vụ</div>';
    if (d.services.length > 0) {
        html += '<div class="services-list">';
        d.services.forEach(function(s) {
            var sub = s.price * s.qty;
            html += '<div class="service-row"><div class="service-info"><div class="service-name">' + s.name + (s.qty > 1 ? ' <small>x' + s.qty + '</small>' : '') + '</div><div class="service-position">⏱ ' + s.time + '</div></div><div class="service-price">' + sub.toLocaleString('vi-VN') + ' đ</div></div>';
        });
        html += '</div>';
    } else {
        html += '<div class="medical-empty">Chưa có dịch vụ</div>';
    }
    html += '</div>';

    if (d.total > 0) {
        html += '<div class="receipt-total-box"><div class="total-label">' + (isPaid ? 'Tổng chi phí' : 'Chi phí dự kiến') + '</div><div class="total-value">' + d.total.toLocaleString('vi-VN') + ' đ</div></div>';
    }
    html += '</div>';

    document.getElementById('modalBody').innerHTML = html;
    document.getElementById('detailModal').classList.add('show');
}
function closeModal() { document.getElementById('detailModal').classList.remove('show'); }

// === CANCEL PENDING ===
function confirmCancel(id) { currentCancelId = id; document.getElementById('cancelModal').classList.add('show'); }
function closeCancelModal() { document.getElementById('cancelModal').classList.remove('show'); }
function doCancelAppointment() {
    alert('✅ Đã hủy lịch hẹn!');
    var r = document.querySelector('tr[data-id="' + currentCancelId + '"]');
    if (r) r.remove();
    var stt = 1;
    document.querySelectorAll('#historyTable tbody tr').forEach(function(r) {
        if (r.style.display !== 'none') r.querySelector('.stt-cell').textContent = stt++;
    });
    closeCancelModal();
}

// === CANCEL CONFIRMED (2 buoc) ===
function confirmCancelConfirmed(id) { document.getElementById('cancelConfirmedModal').classList.add('show'); }
function closeCancelConfirmedModal() { document.getElementById('cancelConfirmedModal').classList.remove('show'); }
function doCancelConfirmed() {
    closeCancelConfirmedModal();
    document.getElementById('cancelInfoModal').classList.add('show');
}
function closeCancelInfoModal() { document.getElementById('cancelInfoModal').classList.remove('show'); }

// === EDIT APPOINTMENT (giong giao dien dat lich) ===
var editSelectedList = [];

function openEditAppointment(id) {
    currentEditId = id;
    var d = appointmentDetails[id];
    if (!d) return;

    // Copy services
    editSelectedList = d.services.map(function(s) {
        return {id:s.id, name:s.name, price:s.price, time:s.time, qty:s.qty, perUnit:s.perUnit, unit:s.unit || ''};
    });

    // Fill date/time
    var parts = d.date.split('/');
    document.getElementById('editDate').value = parts[2] + '-' + parts[1] + '-' + parts[0];
    document.getElementById('editNote').value = d.customerNote || '';

    // Build time options
    var day = new Date(document.getElementById('editDate').value).getDay();
    buildEditTimeOptions(day === 0 ? sundayTimesE : weekdayTimesE, d.time);

    // Build checkbox grid
    buildEditCheckboxGrid();
    renderEditSelected();

    document.getElementById('editAppointmentModal').classList.add('show');
}

var weekdayTimesE = ['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30','13:00','13:30','14:00','14:30','15:00','15:30','16:00','16:30'];
var sundayTimesE = ['08:00','08:30','09:00','09:30','10:00','10:30','11:00','11:30'];

function buildEditTimeOptions(times, selected) {
    var sel = document.getElementById('editTimeSelect');
    sel.innerHTML = '<option value="">Chọn giờ</option>';
    times.forEach(function(t) {
        var s = (t === selected) ? ' selected' : '';
        sel.innerHTML += '<option value="' + t + '"' + s + '>' + t + '</option>';
    });
}

function buildEditCheckboxGrid() {
    var grid = document.getElementById('editSvcGrid');
    var html = '';
    svcList20.forEach(function(s) {
        var checked = editSelectedList.find(function(e) { return e.id === s.id; }) ? 'checked' : '';
        html += '<label class="svc-cb-item">';
        html += '<input type="checkbox" value="' + s.id + '" ' + checked + ' onchange="onEditSvcCheck(this,\'' + s.id + '\')">';
        html += '<span class="svc-cb-name">' + s.name + '</span>';
        html += '<span class="svc-cb-price">' + s.price.toLocaleString('vi-VN') + 'đ</span>';
        html += '</label>';
    });
    grid.innerHTML = html;
}

function onEditSvcCheck(cb, id) {
    var svc = svcList20.find(function(s) { return s.id === id; });
    if (!svc) return;
    if (cb.checked) {
        editSelectedList.push({id:svc.id, name:svc.name, price:svc.price, time:svc.time, qty:1, perUnit:svc.perUnit, unit:svc.unit});
    } else {
        editSelectedList = editSelectedList.filter(function(s) { return s.id !== id; });
    }
    renderEditSelected();
    document.getElementById('editServiceGroup').classList.remove('error');
}

function changeEditQty(id, delta) {
    var item = editSelectedList.find(function(s) { return s.id === id; });
    if (!item) return;
    item.qty = Math.max(1, item.qty + delta);
    renderEditSelected();
}

function removeEditItem(id) {
    editSelectedList = editSelectedList.filter(function(s) { return s.id !== id; });
    var cb = document.querySelector('#editSvcGrid input[value="' + id + '"]');
    if (cb) cb.checked = false;
    renderEditSelected();
}

function renderEditSelected() {
    var box = document.getElementById('editSelectedBox');
    var list = document.getElementById('editSelectedList');
    if (editSelectedList.length === 0) {
        box.style.display = 'none';
        return;
    }
    box.style.display = 'block';
    var html = '', total = 0;
    editSelectedList.forEach(function(s) {
        var sub = s.price * s.qty;
        total += sub;
        html += '<div class="nb-svc-item">';
        html += '<button type="button" class="nb-svc-remove" onclick="removeEditItem(\'' + s.id + '\')" title="Xóa">✕</button>';
        html += '<span class="nb-svc-name">🦷 ' + s.name + '</span>';
        html += '<span class="nb-svc-time">⏱ ' + s.time + '</span>';
        if (s.perUnit) {
            html += '<div class="nb-qty-row">';
            html += '<button type="button" class="nb-qty-btn" onclick="changeEditQty(\'' + s.id + '\',-1)">-</button>';
            html += '<span class="nb-qty-val">' + s.qty + '</span>';
            html += '<button type="button" class="nb-qty-btn" onclick="changeEditQty(\'' + s.id + '\',1)">+</button>';
            html += '<span class="nb-qty-unit">' + s.unit + '</span>';
            html += '</div>';
            html += '<div class="nb-svc-calc">' + s.price.toLocaleString('vi-VN') + 'đ × ' + s.qty + ' = <strong>' + sub.toLocaleString('vi-VN') + 'đ</strong></div>';
        } else {
            html += '<div class="nb-svc-calc"><strong>' + sub.toLocaleString('vi-VN') + 'đ</strong></div>';
        }
        html += '</div>';
    });
    list.innerHTML = html;
    document.getElementById('editTotalValue').textContent = total.toLocaleString('vi-VN');
}

document.getElementById('editDate').addEventListener('change', function() {
    var day = new Date(this.value).getDay();
    buildEditTimeOptions(day === 0 ? sundayTimesE : weekdayTimesE, '');
    document.getElementById('editDateGroup').classList.remove('error');
});
document.getElementById('editTimeSelect').addEventListener('change', function() {
    if (this.value) document.getElementById('editTimeGroup').classList.remove('error');
});

function closeEditAppointment() { document.getElementById('editAppointmentModal').classList.remove('show'); }

function saveEditAppointment() {
    var ok = true;
    if (editSelectedList.length === 0) {
        document.getElementById('editServiceGroup').classList.add('error');
        ok = false;
    } else document.getElementById('editServiceGroup').classList.remove('error');
    if (!document.getElementById('editDate').value) {
        document.getElementById('editDateGroup').classList.add('error');
        ok = false;
    } else document.getElementById('editDateGroup').classList.remove('error');
    if (!document.getElementById('editTimeSelect').value) {
        document.getElementById('editTimeGroup').classList.add('error');
        ok = false;
    } else document.getElementById('editTimeGroup').classList.remove('error');
    if (!ok) return;

    // Cap nhat data
    var d = appointmentDetails[currentEditId];
    d.services = editSelectedList.slice();
    d.total = editSelectedList.reduce(function(sum, s) { return sum + s.price * s.qty; }, 0);
    d.time = document.getElementById('editTimeSelect').value;
    var dv = document.getElementById('editDate').value.split('-');
    d.date = dv[2] + '/' + dv[1] + '/' + dv[0];
    d.customerNote = document.getElementById('editNote').value;

    // Cap nhat table row
    var row = document.querySelector('tr[data-id="' + currentEditId + '"]');
    if (row) {
        row.children[1].textContent = d.date;
        row.children[2].textContent = d.time;
        row.children[3].textContent = editSelectedList.map(function(s) {
            return s.name + (s.qty > 1 ? ' x' + s.qty : '');
        }).join(', ');
    }

    alert('✅ Cập nhật lịch hẹn thành công!');
    closeEditAppointment();
}

// === DOI MAT KHAU ===
function checkStrength(p) {
    var bar = document.getElementById('strengthBar');
    var s = 0;
    if (/[a-z]/.test(p)) s++;
    if (/[A-Z]/.test(p)) s++;
    if (/[0-9]/.test(p)) s++;
    if (/[!@#$%^&*]/.test(p)) s++;
    if (p.length >= 8) s++;
    bar.className = 'password-strength-bar';
    if (s <= 2) bar.classList.add('weak');
    else if (s <= 4) bar.classList.add('medium');
    else bar.classList.add('strong');
}

function changePassword(e) {
    e.preventDefault();
    var ok = true;
    if (!document.getElementById('oldPassword').value) {
        document.getElementById('oldPassGroup').classList.add('error');
        ok = false;
    } else document.getElementById('oldPassGroup').classList.remove('error');

    var np = document.getElementById('newPassword').value;
    if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).{8,}$/.test(np)) {
        document.getElementById('newPassGroup').classList.add('error');
        ok = false;
    } else document.getElementById('newPassGroup').classList.remove('error');

    if (document.getElementById('confirmNewPassword').value !== np) {
        document.getElementById('confirmPassGroup').classList.add('error');
        ok = false;
    } else document.getElementById('confirmPassGroup').classList.remove('error');

    if (ok) {
        alert('✅ Đổi mật khẩu thành công!');
        document.getElementById('passwordForm').reset();
        document.getElementById('strengthBar').className = 'password-strength-bar';
    }
    return false;
}

document.getElementById('oldPassword').addEventListener('input', function() {
    document.getElementById('oldPassGroup').classList.remove('error');
});
document.getElementById('newPassword').addEventListener('input', function() {
    document.getElementById('newPassGroup').classList.remove('error');
});
document.getElementById('confirmNewPassword').addEventListener('input', function() {
    document.getElementById('confirmPassGroup').classList.remove('error');
});