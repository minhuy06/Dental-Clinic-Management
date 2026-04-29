var services = [
    {id:1, name:'Khám tổng quát', desc:'Kiểm tra răng miệng toàn diện', time:'20 phút', price:100000, cat:'kham', perUnit:false, unit:'', status:'active'},
    {id:2, name:'Cạo vôi răng', desc:'Loại bỏ mảng bám, vôi răng', time:'30 phút', price:200000, cat:'kham', perUnit:false, unit:'', status:'active'},
    {id:3, name:'Trám răng Composite', desc:'Trám thẩm mỹ Composite cao cấp', time:'30 phút', price:300000, cat:'kham', perUnit:true, unit:'răng', status:'active'},
    {id:4, name:'Nhổ răng sữa', desc:'Nhổ răng sữa cho trẻ em', time:'15 phút', price:100000, cat:'tre-em', perUnit:true, unit:'răng', status:'active'},
    {id:5, name:'Nhổ răng khôn mọc thẳng', desc:'Nhổ răng khôn không phẫu thuật', time:'30 phút', price:1000000, cat:'phau-thuat', perUnit:true, unit:'răng', status:'active'},
    {id:6, name:'Nhổ răng khôn mọc ngầm', desc:'Tiểu phẫu nhổ răng khôn phức tạp', time:'60 phút', price:3000000, cat:'phau-thuat', perUnit:true, unit:'răng', status:'active'},
    {id:7, name:'Tẩy trắng răng Laser', desc:'Tẩy trắng bằng công nghệ Laser hiện đại', time:'45 phút', price:2500000, cat:'tham-my', perUnit:false, unit:'', status:'active'},
    {id:8, name:'Tẩy trắng răng tại nhà', desc:'Máng tẩy trắng tại nhà tiện lợi', time:'20 phút', price:1500000, cat:'tham-my', perUnit:false, unit:'', status:'active'},
    {id:9, name:'Lấy tủy răng cửa', desc:'Điều trị tủy răng cửa', time:'45 phút', price:800000, cat:'kham', perUnit:true, unit:'răng', status:'active'},
    {id:10, name:'Lấy tủy răng hàm', desc:'Điều trị tủy răng hàm', time:'60 phút', price:1500000, cat:'kham', perUnit:true, unit:'răng', status:'active'},
    {id:11, name:'Bọc răng sứ Titan', desc:'Răng sứ Titan tiêu chuẩn', time:'45 phút/răng', price:2000000, cat:'tham-my', perUnit:true, unit:'răng', status:'active'},
    {id:12, name:'Bọc răng sứ Cercon', desc:'Răng sứ Cercon Đức chính hãng', time:'45 phút/răng', price:5000000, cat:'tham-my', perUnit:true, unit:'răng', status:'active'},
    {id:13, name:'Bọc răng sứ Zirconia', desc:'Răng sứ Zirconia nguyên khối cao cấp', time:'45 phút/răng', price:6000000, cat:'tham-my', perUnit:true, unit:'răng', status:'active'},
    {id:14, name:'Mặt dán sứ Veneer', desc:'Dán sứ siêu mỏng thẩm mỹ', time:'60 phút/răng', price:7000000, cat:'tham-my', perUnit:true, unit:'răng', status:'active'},
    {id:15, name:'Cấy ghép Implant tiêu chuẩn', desc:'Trụ Implant Titanium nhập khẩu', time:'60-90 phút', price:15000000, cat:'phau-thuat', perUnit:true, unit:'trụ', status:'active'},
    {id:16, name:'Cấy ghép Implant cao cấp', desc:'Trụ Implant cao cấp Châu Âu', time:'60-90 phút', price:30000000, cat:'phau-thuat', perUnit:true, unit:'trụ', status:'active'},
    {id:17, name:'Niềng răng mắc cài kim loại', desc:'Chỉnh nha mắc cài kim loại chuẩn', time:'60 phút/lần', price:25000000, cat:'chinh-nha', perUnit:false, unit:'', status:'active'},
    {id:18, name:'Niềng răng mắc cài sứ', desc:'Chỉnh nha mắc cài sứ thẩm mỹ', time:'60 phút/lần', price:35000000, cat:'chinh-nha', perUnit:false, unit:'', status:'active'},
    {id:19, name:'Niềng răng Invisalign', desc:'Khay trong suốt chỉnh nha tiên tiến', time:'45 phút/lần', price:80000000, cat:'chinh-nha', perUnit:false, unit:'', status:'active'},
    {id:20, name:'Điều trị viêm nha chu', desc:'Điều trị viêm nướu nha chu chuyên sâu', time:'30 phút', price:500000, cat:'kham', perUnit:false, unit:'', status:'inactive'}
];

var staffList = [
    {id:1, name:'BS. Nguyễn Hải', role:'doctor', specialty:'Răng tổng quát', degree:'CKI', phone:'0901234561', email:'nguyen.hai@5ae.com', startDate:'2020-01-10', status:'active', notes:''},
    {id:2, name:'BS. Trần Tâm', role:'doctor', specialty:'Chỉnh nha', degree:'Thạc sĩ', phone:'0901234562', email:'tran.tam@5ae.com', startDate:'2019-06-01', status:'active', notes:''},
    {id:3, name:'BS. Lê Quang', role:'doctor', specialty:'Phục hình', degree:'Tiến sĩ', phone:'0901234563', email:'le.quang@5ae.com', startDate:'2018-03-15', status:'active', notes:''},
    {id:4, name:'BS. Phạm Hương', role:'doctor', specialty:'Thẩm mỹ nha', degree:'CKI', phone:'0901234564', email:'pham.huong@5ae.com', startDate:'2021-09-01', status:'active', notes:''},
    {id:5, name:'BS. Hoàng Quân', role:'doctor', specialty:'Phẫu thuật miệng', degree:'CKII', phone:'0901234565', email:'hoang.quan@5ae.com', startDate:'2017-11-20', status:'active', notes:''},
    {id:6, name:'Nguyễn Thị Lan', role:'nurse', specialty:'Hỗ trợ điều trị', degree:'', phone:'0912345601', email:'lan.nv@5ae.com', startDate:'2022-01-05', status:'active', notes:''},
    {id:7, name:'Trần Văn Hùng', role:'receptionist', specialty:'Tiếp nhận bệnh nhân', degree:'', phone:'0912345602', email:'hung.tv@5ae.com', startDate:'2022-04-10', status:'active', notes:''},
    {id:8, name:'Lê Thị Mai', role:'nurse', specialty:'Hỗ trợ phẫu thuật', degree:'', phone:'0912345603', email:'mai.lt@5ae.com', startDate:'2021-07-15', status:'leave', notes:'Đang nghỉ thai sản'}
];

var accounts = [
    {id:1, name:'Admin Hệ Thống', username:'admin', role:'admin', phone:'0900000001', email:'admin@5ae.com', createdDate:'2020-01-01', status:'active'},
    {id:2, name:'BS. Nguyễn Hải', username:'bs.nguyenhai', role:'doctor', phone:'0901234561', email:'nguyen.hai@5ae.com', createdDate:'2020-01-10', status:'active'},
    {id:3, name:'BS. Trần Tâm', username:'bs.trantam', role:'doctor', phone:'0901234562', email:'tran.tam@5ae.com', createdDate:'2019-06-01', status:'active'},
    {id:4, name:'BS. Lê Quang', username:'bs.lequang', role:'doctor', phone:'0901234563', email:'le.quang@5ae.com', createdDate:'2018-03-15', status:'active'},
    {id:5, name:'BS. Phạm Hương', username:'bs.phamhuong', role:'doctor', phone:'0901234564', email:'pham.huong@5ae.com', createdDate:'2021-09-01', status:'active'},
    {id:6, name:'BS. Hoàng Quân', username:'bs.hoangquan', role:'doctor', phone:'0901234565', email:'hoang.quan@5ae.com', createdDate:'2017-11-20', status:'active'},
    {id:7, name:'Nguyễn Thị Lan', username:'nv.lan', role:'staff', phone:'0912345601', email:'lan.nv@5ae.com', createdDate:'2022-01-05', status:'active'},
    {id:8, name:'Trần Văn Hùng', username:'nv.hung', role:'staff', phone:'0912345602', email:'hung.tv@5ae.com', createdDate:'2022-04-10', status:'active'},
    {id:9, name:'Nguyễn Văn Hiển', username:'0908123456', role:'customer', phone:'0908123456', email:'', createdDate:'2024-01-10', status:'active'},
    {id:10, name:'Trần Thị Thảo', username:'0918765432', role:'customer', phone:'0918765432', email:'', createdDate:'2024-02-15', status:'active'},
    {id:11, name:'Lê Anh Nam', username:'0987654321', role:'customer', phone:'0987654321', email:'', createdDate:'2023-11-20', status:'inactive'},
    {id:12, name:'Phạm Thu Hà', username:'0978123456', role:'customer', phone:'0978123456', email:'', createdDate:'2024-03-05', status:'active'}
];

// ==================== BIẾN TOÀN CỤC ====================
var svcFilter = 'all', svcPage = 1, svcPerPage = 6, editingSvcId = null;
var staffFilter = 'all', staffPage = 1, staffPerPage = 8, editingStaffId = null;
var accFilter = 'all', accPage = 1, accPerPage = 8, editingAccId = null;

// ==================== TIỆN ÍCH ====================
function escapeHtml(t) {
    if (!t) return '';
    var d = document.createElement('div');
    d.textContent = t;
    return d.innerHTML;
}

function formatPrice(p) {
    if (p >= 1000000) return (p/1000000).toFixed(p%1000000===0?0:1) + ' triệu';
    if (p >= 1000) return (p/1000).toFixed(0) + 'K';
    return p.toLocaleString('vi-VN');
}

function formatDate(d) {
    if (!d) return '';
    var parts = d.split('-');
    return parts[2] + '/' + parts[1] + '/' + parts[0];
}

function showToast(msg, type) {
    var t = document.createElement('div');
    t.className = 'toast-message' + (type === 'error' ? ' error' : '');
    t.innerHTML = '<i class="fas ' + (type === 'error' ? 'fa-times-circle' : 'fa-check-circle') + '"></i> ' + msg;
    document.body.appendChild(t);
    setTimeout(function() { t.remove(); }, 3000);
}

function renderPagination(containerId, total, perPage, currentPage, onClickFn) {
    var totalPages = Math.ceil(total / perPage);
    var el = document.getElementById(containerId);
    if (totalPages <= 1) { el.innerHTML = ''; return; }
    var html = '';
    for (var i = 1; i <= totalPages; i++) {
        html += '<button class="page-btn' + (i === currentPage ? ' active' : '') + '" onclick="' + onClickFn + '(' + i + ')">' + i + '</button>';
    }
    el.innerHTML = html;
}

// ==================== TAB SWITCHING ====================
function switchTab(tab, el) {
    // Ẩn tất cả panel
    document.querySelectorAll('.tab-panel').forEach(function(p) { p.classList.remove('active'); });
    // Bỏ active tất cả nav link
    document.querySelectorAll('.nav-menu a').forEach(function(a) { a.classList.remove('active'); });
    // Hiện panel được chọn
    document.getElementById('panel-' + tab).classList.add('active');
    // Active đúng link trên nav
    if (el) el.classList.add('active');
    return false;
}

// ==================== DỊCH VỤ ====================
var catConfig = {
    'kham':      {label:'Khám & Chẩn đoán', color:'#2563eb', bg:'#dbeafe', icon:'fa-stethoscope'},
    'tham-my':   {label:'Thẩm mỹ',          color:'#ec4899', bg:'#fce7f3', icon:'fa-star'},
    'chinh-nha': {label:'Chỉnh nha',        color:'#8b5cf6', bg:'#ede9fe', icon:'fa-teeth'},
    'phau-thuat':{label:'Phẫu thuật',       color:'#ef4444', bg:'#fee2e2', icon:'fa-scalpel'},
    'tre-em':    {label:'Trẻ em',           color:'#10b981', bg:'#d1fae5', icon:'fa-child'}
};

function renderServices() {
    var search = document.getElementById('svcSearch').value.toLowerCase();
    var filtered = services.filter(function(s) {
        return (svcFilter === 'all' || s.cat === svcFilter) &&
               (!search || s.name.toLowerCase().indexOf(search) > -1 || s.desc.toLowerCase().indexOf(search) > -1);
    });

    var total = filtered.length;
    var start = (svcPage - 1) * svcPerPage;
    var paged = filtered.slice(start, start + svcPerPage);

    var grid = document.getElementById('serviceGrid');
    if (paged.length === 0) {
        grid.innerHTML = '<div class="empty-state" style="grid-column:1/-1"><i class="fas fa-inbox"></i><p>Không có dịch vụ nào</p></div>';
    } else {
        grid.innerHTML = paged.map(function(s) {
            var cat = catConfig[s.cat] || {label: s.cat, color:'#6b7280', bg:'#f3f4f6', icon:'fa-tooth'};
            var statusBadge = s.status === 'active'
                ? '<span class="badge badge-active"><i class="fas fa-check-circle"></i> Đang dùng</span>'
                : '<span class="badge badge-inactive"><i class="fas fa-pause-circle"></i> Tạm ngưng</span>';
            return '<div class="service-card">' +
                '<div class="service-card-header">' +
                    '<div class="service-icon" style="background:' + cat.bg + ';color:' + cat.color + '"><i class="fas ' + cat.icon + '"></i></div>' +
                    statusBadge +
                '</div>' +
                '<span class="service-cat" style="background:' + cat.bg + ';color:' + cat.color + '">' + cat.label + '</span>' +
                '<div class="service-name">' + escapeHtml(s.name) + '</div>' +
                '<div class="service-desc">' + escapeHtml(s.desc) + '</div>' +
                '<div class="service-meta">' +
                    '<div class="service-price">' + formatPrice(s.price) + (s.perUnit ? ' / ' + s.unit : '') + '</div>' +
                    '<div class="service-time"><i class="fas fa-clock"></i> ' + s.time + '</div>' +
                '</div>' +
                '<div class="service-actions">' +
                    '<button class="btn-action btn-edit" onclick="editService(' + s.id + ')" title="Sửa"><i class="fas fa-edit"></i></button>' +
                    '<button class="btn-action btn-delete" onclick="deleteService(' + s.id + ')" title="Xóa"><i class="fas fa-trash"></i></button>' +
                    '<button class="btn-action ' + (s.status==='active' ? 'btn-toggle-active' : 'btn-toggle-inactive') + '" onclick="toggleServiceStatus(' + s.id + ')" title="' + (s.status==='active' ? 'Tạm ngưng' : 'Kích hoạt') + '">' +
                        '<i class="fas ' + (s.status==='active' ? 'fa-pause' : 'fa-play') + '"></i>' +
                    '</button>' +
                '</div>' +
            '</div>';
        }).join('');
    }

    renderPagination('svcPagination', total, svcPerPage, svcPage, 'goSvcPage');
    updateSvcStats();
}

function updateSvcStats() {
    document.getElementById('statTotalServices').innerText = services.length;
    document.getElementById('statActiveServices').innerText = services.filter(function(s){return s.status==='active';}).length;
    document.getElementById('statSuspended').innerText = services.filter(function(s){return s.status==='inactive';}).length;
}

function goSvcPage(p) { svcPage = p; renderServices(); }

function openServiceModal(id) {
    editingSvcId = null;
    document.getElementById('serviceModalTitle').innerText = 'Thêm dịch vụ mới';
    document.getElementById('svcId').value = '';
    document.getElementById('svcName').value = '';
    document.getElementById('svcCat').value = 'kham';
    document.getElementById('svcDesc').value = '';
    document.getElementById('svcPrice').value = '';
    document.getElementById('svcTime').value = '';
    document.getElementById('svcPerUnit').value = 'false';
    document.getElementById('svcUnit').value = '';
    document.getElementById('svcStatus').value = 'active';
    document.getElementById('serviceModal').style.display = 'flex';
}

function editService(id) {
    var s = services.find(function(x){return x.id===id;});
    if (!s) return;
    editingSvcId = id;
    document.getElementById('serviceModalTitle').innerText = 'Sửa dịch vụ';
    document.getElementById('svcId').value = s.id;
    document.getElementById('svcName').value = s.name;
    document.getElementById('svcCat').value = s.cat;
    document.getElementById('svcDesc').value = s.desc;
    document.getElementById('svcPrice').value = s.price;
    document.getElementById('svcTime').value = s.time;
    document.getElementById('svcPerUnit').value = s.perUnit ? 'true' : 'false';
    document.getElementById('svcUnit').value = s.unit || '';
    document.getElementById('svcStatus').value = s.status;
    document.getElementById('serviceModal').style.display = 'flex';
}

function closeServiceModal() { document.getElementById('serviceModal').style.display = 'none'; }

function saveService() {
    var name = document.getElementById('svcName').value.trim();
    var price = parseInt(document.getElementById('svcPrice').value);
    if (!name || !price) { showToast('Vui lòng điền đầy đủ tên và giá dịch vụ!', 'error'); return; }

    var data = {
        name: name,
        cat: document.getElementById('svcCat').value,
        desc: document.getElementById('svcDesc').value.trim(),
        price: price,
        time: document.getElementById('svcTime').value.trim() || 'Liên hệ',
        perUnit: document.getElementById('svcPerUnit').value === 'true',
        unit: document.getElementById('svcUnit').value.trim(),
        status: document.getElementById('svcStatus').value
    };

    if (editingSvcId) {
        var idx = services.findIndex(function(s){return s.id===editingSvcId;});
        if (idx > -1) { services[idx] = Object.assign({}, services[idx], data); showToast('Đã cập nhật dịch vụ'); }
    } else {
        var newId = Math.max.apply(null, services.map(function(s){return s.id;})) + 1;
        services.push(Object.assign({id: newId}, data));
        showToast('Đã thêm dịch vụ mới');
    }
    closeServiceModal();
    renderServices();
}

function deleteService(id) {
    if (!confirm('Bạn có chắc muốn xóa dịch vụ này?')) return;
    services = services.filter(function(s){return s.id!==id;});
    renderServices();
    showToast('Đã xóa dịch vụ');
}

function toggleServiceStatus(id) {
    var s = services.find(function(x){return x.id===id;});
    if (!s) return;
    s.status = s.status === 'active' ? 'inactive' : 'active';
    renderServices();
    showToast(s.status === 'active' ? 'Đã kích hoạt dịch vụ' : 'Đã tạm ngưng dịch vụ');
}

// ==================== NHÂN SỰ ====================
var roleConfig = {
    'doctor':       {label:'Bác sĩ',          badge:'badge-doctor',   icon:'fa-user-md'},
    'nurse':        {label:'Điều dưỡng',       badge:'badge-staff',    icon:'fa-user-nurse'},
    'receptionist': {label:'Lễ tân',           badge:'badge-customer', icon:'fa-concierge-bell'},
    'technician':   {label:'Kỹ thuật viên',    badge:'badge-pending',  icon:'fa-tools'}
};
var staffStatusConfig = {
    'active':   {label:'Đang làm việc', badge:'badge-active'},
    'inactive': {label:'Nghỉ việc',     badge:'badge-inactive'},
    'leave':    {label:'Nghỉ phép',     badge:'badge-pending'}
};

function renderStaff() {
    var search = document.getElementById('staffSearch').value.toLowerCase();
    var filtered = staffList.filter(function(s) {
        return (staffFilter === 'all' || s.role === staffFilter) &&
               (!search || s.name.toLowerCase().indexOf(search) > -1 || s.phone.indexOf(search) > -1 || s.email.toLowerCase().indexOf(search) > -1);
    });

    var total = filtered.length;
    var start = (staffPage - 1) * staffPerPage;
    var paged = filtered.slice(start, start + staffPerPage);

    var tbody = document.getElementById('staffTableBody');
    if (paged.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;padding:40px;color:var(--text-sub)">Không có dữ liệu nhân sự</td></tr>';
    } else {
        tbody.innerHTML = paged.map(function(s) {
            var rc = roleConfig[s.role] || {label:s.role, badge:'badge-staff', icon:'fa-user'};
            var sc = staffStatusConfig[s.status] || {label:s.status, badge:'badge-active'};
            return '<tr>' +
                '<td><span style="font-weight:700;color:var(--primary-color)">#' + s.id + '</span></td>' +
                '<td style="font-weight:600"><i class="fas ' + rc.icon + '" style="margin-right:8px;color:var(--primary-color)"></i>' + escapeHtml(s.name) + '</td>' +
                '<td><span class="badge ' + rc.badge + '">' + rc.label + '</span></td>' +
                '<td>' + (s.specialty ? escapeHtml(s.specialty) : '<span style="color:var(--text-sub)">—</span>') + (s.degree ? ' <small style="color:var(--text-sub)">(' + escapeHtml(s.degree) + ')</small>' : '') + '</td>' +
                '<td>' + s.phone + '</td>' +
                '<td style="color:var(--text-sub);font-size:0.82rem">' + (s.email || '—') + '</td>' +
                '<td><span class="badge ' + sc.badge + '">' + sc.label + '</span></td>' +
                '<td><div class="action-btns">' +
                    '<button class="btn-action btn-edit" onclick="editStaff(' + s.id + ')" title="Sửa"><i class="fas fa-edit"></i></button>' +
                    '<button class="btn-action btn-delete" onclick="deleteStaff(' + s.id + ')" title="Xóa"><i class="fas fa-trash"></i></button>' +
                '</div></td>' +
            '</tr>';
        }).join('');
    }

    renderPagination('staffPagination', total, staffPerPage, staffPage, 'goStaffPage');
    updateStaffStats();
}

function updateStaffStats() {
    document.getElementById('statTotalStaff').innerText = staffList.length;
    document.getElementById('statDoctors').innerText = staffList.filter(function(s){return s.role==='doctor';}).length;
    document.getElementById('statNurses').innerText = staffList.filter(function(s){return s.role==='nurse'||s.role==='receptionist'||s.role==='technician';}).length;
    document.getElementById('statActiveStaff').innerText = staffList.filter(function(s){return s.status==='active';}).length;
}

function goStaffPage(p) { staffPage = p; renderStaff(); }

function openStaffModal() {
    editingStaffId = null;
    document.getElementById('staffModalTitle').innerText = 'Thêm nhân sự mới';
    document.getElementById('staffId').value = '';
    document.getElementById('staffName').value = '';
    document.getElementById('staffRole').value = 'doctor';
    document.getElementById('staffSpecialty').value = '';
    document.getElementById('staffDegree').value = '';
    document.getElementById('staffPhone').value = '';
    document.getElementById('staffStartDate').value = '';
    document.getElementById('staffStatus').value = 'active';
    document.getElementById('staffModal').style.display = 'flex';
}

function editStaff(id) {
    var s = staffList.find(function(x){return x.id===id;});
    if (!s) return;
    editingStaffId = id;
    document.getElementById('staffModalTitle').innerText = 'Sửa thông tin nhân sự';
    document.getElementById('staffId').value = s.id;
    document.getElementById('staffName').value = s.name;
    document.getElementById('staffRole').value = s.role;
    document.getElementById('staffSpecialty').value = s.specialty || '';
    document.getElementById('staffDegree').value = s.degree || '';
    document.getElementById('staffPhone').value = s.phone;
    document.getElementById('staffStartDate').value = s.startDate || '';
    document.getElementById('staffStatus').value = s.status;
    document.getElementById('staffModal').style.display = 'flex';
}

function closeStaffModal() { document.getElementById('staffModal').style.display = 'none'; }

function saveStaff() {
    var name = document.getElementById('staffName').value.trim();
    var phone = document.getElementById('staffPhone').value.trim();
    if (!name || !phone) { showToast('Vui lòng điền đủ họ tên và số điện thoại!', 'error'); return; }

    var data = {
        name: name, role: document.getElementById('staffRole').value,
        specialty: document.getElementById('staffSpecialty').value,
        degree: document.getElementById('staffDegree').value.trim(),
        phone: phone,
        startDate: document.getElementById('staffStartDate').value,
        status: document.getElementById('staffStatus').value
    };

    if (editingStaffId) {
        var idx = staffList.findIndex(function(s){return s.id===editingStaffId;});
        if (idx > -1) { staffList[idx] = Object.assign({}, staffList[idx], data); showToast('Đã cập nhật thông tin nhân sự'); }
    } else {
        var newId = Math.max.apply(null, staffList.map(function(s){return s.id;})) + 1;
        staffList.push(Object.assign({id: newId}, data));
        showToast('Đã thêm nhân sự mới');
    }
    closeStaffModal();
    renderStaff();
}

function deleteStaff(id) {
    if (!confirm('Bạn có chắc muốn xóa nhân sự này?')) return;
    staffList = staffList.filter(function(s){return s.id!==id;});
    renderStaff();
    showToast('Đã xóa nhân sự');
}

// ==================== TÀI KHOẢN ====================
var accRoleConfig = {
    'customer': {label:'Khách hàng', badge:'badge-customer'},
    'doctor':   {label:'Bác sĩ',    badge:'badge-doctor'},
    'staff':    {label:'Nhân viên',  badge:'badge-staff'},
    'admin':    {label:'Admin',      badge:'badge-admin'}
};

function renderAccounts() {
    var search = document.getElementById('accSearch').value.toLowerCase();
    var filtered = accounts.filter(function(a) {
        return (accFilter === 'all' || a.role === accFilter) &&
               (!search || a.name.toLowerCase().indexOf(search) > -1 ||
                a.username.toLowerCase().indexOf(search) > -1 ||
                a.phone.indexOf(search) > -1);
    });

    var total = filtered.length;
    var start = (accPage - 1) * accPerPage;
    var paged = filtered.slice(start, start + accPerPage);

    var tbody = document.getElementById('accTableBody');
    if (paged.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;padding:40px;color:var(--text-sub)">Không có tài khoản nào</td></tr>';
    } else {
        tbody.innerHTML = paged.map(function(a) {
            var rc = accRoleConfig[a.role] || {label:a.role, badge:'badge-staff'};
            var statusBadge = a.status === 'active'
                ? '<span class="badge badge-active"><i class="fas fa-circle"></i> Hoạt động</span>'
                : '<span class="badge badge-inactive"><i class="fas fa-ban"></i> Bị khóa</span>';
            return '<tr>' +
                '<td><span style="font-weight:700;color:var(--primary-color)">#' + a.id + '</span></td>' +
                '<td style="font-weight:600">' + escapeHtml(a.name) + '</td>' +
                '<td>' + (a.phone || '—') + '</td>' +
                '<td><span class="badge ' + rc.badge + '">' + rc.label + '</span></td>' +
                '<td style="color:var(--text-sub);font-size:0.82rem">' + formatDate(a.createdDate) + '</td>' +
                '<td>' + statusBadge + '</td>' +
                '<td><div class="action-btns">' +
                    '<button class="btn-action btn-edit" onclick="editAccount(' + a.id + ')" title="Sửa"><i class="fas fa-edit"></i></button>' +
                    '<button class="btn-action ' + (a.status==='active' ? 'btn-toggle-active' : 'btn-toggle-inactive') + '" onclick="toggleAccStatus(' + a.id + ')" title="' + (a.status==='active' ? 'Khóa' : 'Mở khóa') + '">' +
                        '<i class="fas ' + (a.status==='active' ? 'fa-lock' : 'fa-unlock') + '"></i>' +
                    '</button>' +
                    '<button class="btn-action btn-delete" onclick="deleteAccount(' + a.id + ')" title="Xóa"><i class="fas fa-trash"></i></button>' +
                '</div></td>' +
            '</tr>';
        }).join('');
    }

    renderPagination('accPagination', total, accPerPage, accPage, 'goAccPage');
    updateAccStats();
}

function updateAccStats() {
    document.getElementById('statTotalAccounts').innerText = accounts.length;
    document.getElementById('statCustomers').innerText = accounts.filter(function(a){return a.role==='customer';}).length;
    document.getElementById('statStaffAcc').innerText = accounts.filter(function(a){return a.role==='doctor'||a.role==='staff'||a.role==='admin';}).length;
    document.getElementById('statLocked').innerText = accounts.filter(function(a){return a.status==='inactive';}).length;
}

function goAccPage(p) { accPage = p; renderAccounts(); }

function openAccountModal() {
    editingAccId = null;
    document.getElementById('accModalTitle').innerText = 'Thêm tài khoản mới';
    document.getElementById('accId').value = '';
    document.getElementById('accName').value = '';
    document.getElementById('accRole').value = 'customer';
    document.getElementById('accPassword').value = '';
    document.getElementById('accPhone').value = '';
    document.getElementById('accStatus').value = 'active';
    document.getElementById('accountModal').style.display = 'flex';
}

function editAccount(id) {
    var a = accounts.find(function(x){return x.id===id;});
    if (!a) return;
    editingAccId = id;
    document.getElementById('accModalTitle').innerText = 'Sửa tài khoản';
    document.getElementById('accId').value = a.id;
    document.getElementById('accName').value = a.name;
    document.getElementById('accRole').value = a.role;
    document.getElementById('accPassword').value = '';
    document.getElementById('accPhone').value = a.phone || '';
    document.getElementById('accStatus').value = a.status;
    document.getElementById('accountModal').style.display = 'flex';
}

function closeAccountModal() { document.getElementById('accountModal').style.display = 'none'; }

function saveAccount() {
    var name = document.getElementById('accName').value.trim();
    var phone = document.getElementById('accPhone').value.trim();
    if (!name || !phone) { showToast('Vui lòng điền đủ họ tên và số điện thoại!', 'error'); return; }
    if (!editingAccId && !document.getElementById('accPassword').value) {
        showToast('Vui lòng nhập mật khẩu!', 'error'); return;
    }
    var data = {
        name: name,
        role: document.getElementById('accRole').value,
        phone: phone,
        status: document.getElementById('accStatus').value
    };
    if (editingAccId) {
        var idx = accounts.findIndex(function(a){return a.id===editingAccId;});
        if (idx > -1) { accounts[idx] = Object.assign({}, accounts[idx], data); showToast('Đã cập nhật tài khoản'); }
    } else {
        var newId = Math.max.apply(null, accounts.map(function(a){return a.id;})) + 1;
        var today = new Date().toISOString().split('T')[0];
        accounts.push(Object.assign({id:newId, createdDate:today}, data));
        showToast('Đã thêm tài khoản mới');
    }
    closeAccountModal();
    renderAccounts();
}

function toggleAccStatus(id) {
    var a = accounts.find(function(x){return x.id===id;});
    if (!a) return;
    a.status = a.status === 'active' ? 'inactive' : 'active';
    renderAccounts();
    showToast(a.status === 'active' ? 'Đã mở khóa tài khoản' : 'Đã khóa tài khoản');
}

function deleteAccount(id) {
    if (!confirm('Bạn có chắc muốn xóa tài khoản này?')) return;
    accounts = accounts.filter(function(a){return a.id!==id;});
    renderAccounts();
    showToast('Đã xóa tài khoản');
}

// ==================== DOANH THU ====================
var revenueData = [
    {month:'T1/2024', revenue:48500000, appointments:42, services:68},
    {month:'T2/2024', revenue:52300000, appointments:47, services:74},
    {month:'T3/2024', revenue:61200000, appointments:55, services:89},
    {month:'T4/2024', revenue:58700000, appointments:51, services:82},
    {month:'T5/2024', revenue:73400000, appointments:64, services:103},
    {month:'T6/2024', revenue:69800000, appointments:60, services:97},
    {month:'T7/2024', revenue:81200000, appointments:72, services:116},
    {month:'T8/2024', revenue:76500000, appointments:68, services:109},
    {month:'T9/2024', revenue:88900000, appointments:79, services:128},
    {month:'T10/2024', revenue:92100000, appointments:83, services:134},
    {month:'T11/2024', revenue:105600000, appointments:94, services:152},
    {month:'T12/2024', revenue:118300000, appointments:106, services:171}
];

var revenueDetails = [
    {id:1, date:'15/12/2024', patient:'Nguyễn Văn Hiển', service:'Bọc sứ Zirconia x2', amount:12000000, method:'Chuyển khoản'},
    {id:2, date:'14/12/2024', patient:'Trần Thị Thảo', service:'Niềng răng Invisalign', amount:80000000, method:'Trả góp'},
    {id:3, date:'13/12/2024', patient:'Lê Anh Nam', service:'Implant cao cấp x1', amount:30000000, method:'Tiền mặt'},
    {id:4, date:'12/12/2024', patient:'Phạm Thu Hà', service:'Tẩy trắng Laser', amount:2500000, method:'Chuyển khoản'},
    {id:5, date:'11/12/2024', patient:'Hoàng Văn Tuấn', service:'Khám + Cạo vôi', amount:300000, method:'Tiền mặt'},
    {id:6, date:'10/12/2024', patient:'Đặng Thị Hoa', service:'Bọc sứ Cercon x3', amount:15000000, method:'Chuyển khoản'},
    {id:7, date:'09/12/2024', patient:'Bùi Minh Quân', service:'Nhổ răng khôn x2', amount:6000000, method:'Tiền mặt'},
    {id:8, date:'08/12/2024', patient:'Ngô Thị Lan', service:'Veneer x4', amount:28000000, method:'Trả góp'}
];

function renderRevenue() {
    // Stats
    var totalRevenue = revenueData.reduce(function(a,r){return a+r.revenue;},0);
    var totalAppt = revenueData.reduce(function(a,r){return a+r.appointments;},0);
    var lastMonth = revenueData[revenueData.length-1].revenue;
    var prevMonth = revenueData[revenueData.length-2].revenue;
    var growth = Math.round((lastMonth-prevMonth)/prevMonth*100);

    document.getElementById('revTotalYear').innerText = formatRevenue(totalRevenue);
    document.getElementById('revLastMonth').innerText = formatRevenue(lastMonth);
    document.getElementById('revGrowth').innerText = (growth>0?'+':'')+growth+'%';
    document.getElementById('revTotalAppt').innerText = totalAppt;

    // Chart bars
    var maxRev = Math.max.apply(null, revenueData.map(function(r){return r.revenue;}));
    var chart = document.getElementById('revenueChart');
    chart.innerHTML = revenueData.map(function(r) {
        var pct = Math.round(r.revenue/maxRev*100);
        return '<div class="rev-bar-group">' +
            '<div class="rev-bar-wrap">' +
                '<div class="rev-bar-label-top">' + formatRevenue(r.revenue) + '</div>' +
                '<div class="rev-bar" style="height:' + pct + '%"></div>' +
            '</div>' +
            '<div class="rev-bar-month">' + r.month + '</div>' +
        '</div>';
    }).join('');

    // Table
    var tbody = document.getElementById('revTableBody');
    tbody.innerHTML = revenueDetails.map(function(r) {
        var methodClass = r.method==='Tiền mặt'?'method-cash':r.method==='Chuyển khoản'?'method-transfer':'method-card';
        return '<tr>' +
            '<td>#'+r.id+'</td>' +
            '<td>'+r.date+'</td>' +
            '<td style="font-weight:600">'+r.patient+'</td>' +
            '<td>'+r.service+'</td>' +
            '<td><span class="payment-badge '+methodClass+'">'+r.method+'</span></td>' +
            '<td style="font-weight:700;color:var(--success)">'+r.amount.toLocaleString('vi-VN')+'đ</td>' +
        '</tr>';
    }).join('');
}

function formatRevenue(n) {
    if (n >= 1000000000) return (n/1000000000).toFixed(1)+'Tỷ';
    if (n >= 1000000) return (n/1000000).toFixed(0)+'Tr';
    return (n/1000).toFixed(0)+'K';
}

// ==================== ĐÓNG MODAL KHI CLICK NGOÀI ====================
window.onclick = function(e) {
    if (e.target === document.getElementById('serviceModal')) closeServiceModal();
    if (e.target === document.getElementById('staffModal')) closeStaffModal();
    if (e.target === document.getElementById('accountModal')) closeAccountModal();
};

// ==================== KHỞI TẠO ====================
document.addEventListener('DOMContentLoaded', function() {
    renderServices();
    renderStaff();
    renderAccounts();
    renderRevenue();

    // Filter dịch vụ
    document.querySelectorAll('[data-svc-filter]').forEach(function(btn) {
        btn.onclick = function() {
            svcFilter = this.getAttribute('data-svc-filter');
            svcPage = 1;
            document.querySelectorAll('[data-svc-filter]').forEach(function(b){b.classList.remove('active');});
            this.classList.add('active');
            renderServices();
        };
    });

    document.getElementById('svcSearch').oninput = function() { svcPage = 1; renderServices(); };

    // Filter nhân sự
    document.querySelectorAll('[data-staff-filter]').forEach(function(btn) {
        btn.onclick = function() {
            staffFilter = this.getAttribute('data-staff-filter');
            staffPage = 1;
            document.querySelectorAll('[data-staff-filter]').forEach(function(b){b.classList.remove('active');});
            this.classList.add('active');
            renderStaff();
        };
    });

    document.getElementById('staffSearch').oninput = function() { staffPage = 1; renderStaff(); };

    // Filter tài khoản
    document.querySelectorAll('[data-acc-filter]').forEach(function(btn) {
        btn.onclick = function() {
            accFilter = this.getAttribute('data-acc-filter');
            accPage = 1;
            document.querySelectorAll('[data-acc-filter]').forEach(function(b){b.classList.remove('active');});
            this.classList.add('active');
            renderAccounts();
        };
    });

    document.getElementById('accSearch').oninput = function() { accPage = 1; renderAccounts(); };
});
