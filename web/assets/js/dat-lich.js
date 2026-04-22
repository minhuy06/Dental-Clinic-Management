var allServices = [
    {id:'1', name:'Khám tổng quát', desc:'Kiểm tra răng miệng toàn diện', time:'20 phút', price:100000, cat:'kham', perUnit:false},
    {id:'2', name:'Cạo vôi răng', desc:'Loại bỏ mảng bám, vôi răng', time:'30 phút', price:200000, cat:'kham', perUnit:false},
    {id:'3', name:'Trám răng Composite', desc:'Trám thẩm mỹ Composite', time:'30 phút', price:300000, cat:'kham', perUnit:true, unit:'răng'},
    {id:'4', name:'Nhổ răng sữa', desc:'Nhổ răng sữa cho trẻ em', time:'15 phút', price:100000, cat:'tre-em', perUnit:true, unit:'răng'},
    {id:'5', name:'Nhổ răng khôn mọc thẳng', desc:'Nhổ răng khôn không phẫu thuật', time:'30 phút', price:1000000, cat:'phau-thuat', perUnit:true, unit:'răng'},
    {id:'6', name:'Nhổ răng khôn mọc ngầm', desc:'Tiểu phẫu nhổ răng khôn phức tạp', time:'60 phút', price:3000000, cat:'phau-thuat', perUnit:true, unit:'răng'},
    {id:'7', name:'Tẩy trắng răng Laser', desc:'Tẩy trắng bằng công nghệ Laser', time:'45 phút', price:2500000, cat:'tham-my', perUnit:false},
    {id:'8', name:'Tẩy trắng răng tại nhà', desc:'Máng tẩy trắng tại nhà', time:'20 phút', price:1500000, cat:'tham-my', perUnit:false},
    {id:'9', name:'Lấy tủy răng cửa', desc:'Điều trị tủy răng cửa', time:'45 phút', price:800000, cat:'kham', perUnit:true, unit:'răng'},
    {id:'10', name:'Lấy tủy răng hàm', desc:'Điều trị tủy răng hàm', time:'60 phút', price:1500000, cat:'kham', perUnit:true, unit:'răng'},
    {id:'11', name:'Bọc răng sứ Titan', desc:'Răng sứ Titan tiêu chuẩn', time:'45 phút/răng', price:2000000, cat:'tham-my', perUnit:true, unit:'răng'},
    {id:'12', name:'Bọc răng sứ Cercon', desc:'Răng sứ Cercon Đức', time:'45 phút/răng', price:5000000, cat:'tham-my', perUnit:true, unit:'răng'},
    {id:'13', name:'Bọc răng sứ Zirconia', desc:'Răng sứ Zirconia nguyên khối', time:'45 phút/răng', price:6000000, cat:'tham-my', perUnit:true, unit:'răng'},
    {id:'14', name:'Mặt dán sứ Veneer', desc:'Dán sứ siêu mỏng thẩm mỹ', time:'60 phút/răng', price:7000000, cat:'tham-my', perUnit:true, unit:'răng'},
    {id:'15', name:'Cấy ghép Implant tiêu chuẩn', desc:'Trụ Implant Titanium', time:'60-90 phút', price:15000000, cat:'phau-thuat', perUnit:true, unit:'trụ'},
    {id:'16', name:'Cấy ghép Implant cao cấp', desc:'Trụ Implant cao cấp Châu Âu', time:'60-90 phút', price:30000000, cat:'phau-thuat', perUnit:true, unit:'trụ'},
    {id:'17', name:'Niềng răng mắc cài kim loại', desc:'Chỉnh nha mắc cài kim loại', time:'60 phút/lần', price:25000000, cat:'chinh-nha', perUnit:false},
    {id:'18', name:'Niềng răng mắc cài sứ', desc:'Chỉnh nha mắc cài sứ', time:'60 phút/lần', price:35000000, cat:'chinh-nha', perUnit:false},
    {id:'19', name:'Niềng răng Invisalign', desc:'Khay trong suốt chỉnh nha', time:'45 phút/lần', price:80000000, cat:'chinh-nha', perUnit:false},
    {id:'20', name:'Điều trị viêm nha chu', desc:'Điều trị viêm nướu nha chu', time:'30 phút', price:500000, cat:'kham', perUnit:false}
];

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
    var svc = allServices.find(function(s) { return s.id === id; });
    if (!svc) return;
    if (cb.checked) {
        selectedList.push({id: svc.id, name: svc.name, price: svc.price, time: svc.time, qty: 1, perUnit: svc.perUnit, unit: svc.unit || ''});
    } else {
        selectedList = selectedList.filter(function(s) { return s.id !== id; });
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
    selectedList = selectedList.filter(function(s) { return s.id !== id; });
    var cb = document.querySelector('#svcCheckboxGrid input[value="' + id + '"]');
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
    // Kiem tra dang nhap — neu chua login thi hien popup, khong xu ly tiep
    if (!window.IS_LOGGED_IN) {
        document.getElementById('loginRequiredModal').classList.add('show');
        return;
    }

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

    if (ok) {
        alert('✅ Đặt lịch thành công! Phòng khám sẽ liên hệ xác nhận trong 30 phút.');
        resetBookingForm();
    }
}

// === POPUP DANG NHAP ===
function closeLoginModal() {
    document.getElementById('loginRequiredModal').classList.remove('show');
}
function goToLogin() {
    window.location.href = window.CONTEXT_PATH + '/account/login.jsp';
}