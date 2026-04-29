// ==================== DỮ LIỆU MẪU ====================

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
    {id:1, name:'BS. Nguyễn Hải', role:'doctor', specialty:'Răng tổng quát', degree:'CKI', phone:'0901234561', startDate:'2020-01-10', status:'active'},
    {id:2, name:'BS. Trần Tâm', role:'doctor', specialty:'Chỉnh nha', degree:'Thạc sĩ', phone:'0901234562', startDate:'2019-06-01', status:'active'},
    {id:3, name:'BS. Lê Quang', role:'doctor', specialty:'Phục hình', degree:'Tiến sĩ', phone:'0901234563', startDate:'2018-03-15', status:'active'},
    {id:4, name:'BS. Phạm Hương', role:'doctor', specialty:'Thẩm mỹ nha', degree:'CKI', phone:'0901234564', startDate:'2021-09-01', status:'active'},
    {id:5, name:'BS. Hoàng Quân', role:'doctor', specialty:'Phẫu thuật miệng', degree:'CKII', phone:'0901234565', startDate:'2017-11-20', status:'active'},
    {id:6, name:'Trần Văn Hùng', role:'receptionist', specialty:'Tiếp nhận bệnh nhân', degree:'', phone:'0912345602', startDate:'2022-04-10', status:'active'}
];

var accounts = [
    {id:1, name:'Admin Hệ Thống', username:'admin', role:'admin', phone:'0900000001', email:'admin@5ae.com', createdDate:'2020-01-01', status:'active'},
    {id:2, name:'BS. Nguyễn Hải', username:'bs.nguyenhai', role:'doctor', phone:'0901234561', email:'nguyen.hai@5ae.com', createdDate:'2020-01-10', status:'active'},
    {id:3, name:'BS. Trần Tâm', username:'bs.trantam', role:'doctor', phone:'0901234562', email:'tran.tam@5ae.com', createdDate:'2019-06-01', status:'active'},
    {id:4, name:'BS. Lê Quang', username:'bs.lequang', role:'doctor', phone:'0901234563', email:'le.quang@5ae.com', createdDate:'2018-03-15', status:'active'},
    {id:5, name:'BS. Phạm Hương', username:'bs.phamhuong', role:'doctor', phone:'0901234564', email:'pham.huong@5ae.com', createdDate:'2021-09-01', status:'active'},
    {id:6, name:'BS. Hoàng Quân', username:'bs.hoangquan', role:'doctor', phone:'0901234565', email:'hoang.quan@5ae.com', createdDate:'2017-11-20', status:'active'},
    {id:7, name:'Trần Văn Hùng', username:'nv.hung', role:'staff', phone:'0912345602', email:'hung.tv@5ae.com', createdDate:'2022-04-10', status:'active'},
    {id:8, name:'Nguyễn Thị Bích', username:'nv.bich', role:'staff', phone:'0912345605', email:'bich.nt@5ae.com', createdDate:'2023-02-01', status:'active'},
    {id:9, name:'Nguyễn Văn Hiển', username:'0908123456', role:'customer', phone:'0908123456', email:'', createdDate:'2025-06-12', status:'active'},
    {id:10, name:'Trần Thị Thảo', username:'0918765432', role:'customer', phone:'0918765432', email:'', createdDate:'2025-09-20', status:'active'},
    {id:11, name:'Lê Anh Nam', username:'0987654321', role:'customer', phone:'0987654321', email:'', createdDate:'2025-03-08', status:'inactive'},
    {id:12, name:'Phạm Thu Hà', username:'0978123456', role:'customer', phone:'0978123456', email:'', createdDate:'2026-01-15', status:'active'}
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
    document.querySelectorAll('.tab-panel').forEach(function(p) { p.classList.remove('active'); });
    document.querySelectorAll('.nav-menu a').forEach(function(a) { a.classList.remove('active'); });
    document.getElementById('panel-' + tab).classList.add('active');
    if (el) el.classList.add('active');
    // Re-render khi switch tab để đảm bảo dữ liệu hiển thị đúng
    if (tab === 'services')  renderServices();
    if (tab === 'staff')     renderStaff();
    if (tab === 'accounts')  renderAccounts();
    if (tab === 'revenue')   renderRevenue();
    return false;
}

// ==================== FILTER HELPERS (dùng cho onclick inline) ====================
function setSvcFilter(val, btn) {
    svcFilter = val; svcPage = 1;
    document.querySelectorAll('.filter-btn').forEach(function(b){
        if(b.closest('#panel-services')) b.classList.remove('active');
    });
    btn.classList.add('active');
    renderServices();
}
function setStaffFilter(val, btn) {
    staffFilter = val; staffPage = 1;
    document.querySelectorAll('.filter-btn').forEach(function(b){
        if(b.closest('#panel-staff')) b.classList.remove('active');
    });
    btn.classList.add('active');
    renderStaff();
}
function setAccFilter(val, btn) {
    accFilter = val; accPage = 1;
    document.querySelectorAll('.filter-btn').forEach(function(b){
        if(b.closest('#panel-accounts')) b.classList.remove('active');
    });
    btn.classList.add('active');
    renderAccounts();
}


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

function openServiceModal() {
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
               (!search || s.name.toLowerCase().indexOf(search) > -1 || s.phone.indexOf(search) > -1);
    });

    var total = filtered.length;
    var start = (staffPage - 1) * staffPerPage;
    var paged = filtered.slice(start, start + staffPerPage);

    var tbody = document.getElementById('staffTableBody');
    if (paged.length === 0) {
        tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;padding:40px;color:var(--text-sub)">Không có dữ liệu nhân sự</td></tr>';
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
    document.getElementById('statNurses').innerText = staffList.filter(function(s){return s.role==='receptionist';}).length;
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
        specialty: document.getElementById('staffSpecialty').value.trim(),
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
        tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;padding:40px;color:var(--text-sub)">Không có tài khoản nào</td></tr>';
    } else {
        tbody.innerHTML = paged.map(function(a) {
            var rc = accRoleConfig[a.role] || {label:a.role, badge:'badge-staff'};
            var statusBadge = a.status === 'active'
                ? '<span class="badge badge-active"><i class="fas fa-circle"></i> Hoạt động</span>'
                : '<span class="badge badge-inactive"><i class="fas fa-ban"></i> Bị khóa</span>';
            return '<tr>' +
                '<td><span style="font-weight:700;color:var(--primary-color)">#' + a.id + '</span></td>' +
                '<td style="font-weight:600">' + escapeHtml(a.name) + '</td>' +
                '<td style="font-family:monospace;color:var(--text-sub)">' + escapeHtml(a.username) + '</td>' +
                '<td><span class="badge ' + rc.badge + '">' + rc.label + '</span></td>' +
                '<td>' + (a.phone || '—') + '</td>' +
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
        username: document.getElementById('accPhone').value.trim() || name.toLowerCase().replace(/\s+/g,'.'),
        role: document.getElementById('accRole').value,
        phone: document.getElementById('accPhone').value.trim(),
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

// Dữ liệu tháng năm 2026 (tháng 4 là tháng hiện tại)
var revenueMonths = [
    {month:'T1', revenue:125400000, appointments:112},
    {month:'T2', revenue:118700000, appointments:105},
    {month:'T3', revenue:138900000, appointments:124},
    {month:'T4', revenue:142600000, appointments:128},
    {month:'T5', revenue:0,         appointments:0},
    {month:'T6', revenue:0,         appointments:0},
    {month:'T7', revenue:0,         appointments:0},
    {month:'T8', revenue:0,         appointments:0},
    {month:'T9', revenue:0,         appointments:0},
    {month:'T10', revenue:0,        appointments:0},
    {month:'T11', revenue:0,        appointments:0},
    {month:'T12', revenue:0,        appointments:0}
];

var revenueByCat = [
    {name:'Chỉnh nha',        color:'#2563eb', revenue:198000000},
    {name:'Phục hình',        color:'#8b5cf6', revenue:156000000},
    {name:'Phẫu thuật',       color:'#ef4444', revenue:112000000},
    {name:'Thẩm mỹ',          color:'#f59e0b', revenue:89000000},
    {name:'Khám & Điều trị',  color:'#10b981', revenue:70600000}
];

var revenueTopServices = [
    {name:'Niềng răng Invisalign',    cat:'Chỉnh nha',  amount:112000000},
    {name:'Cấy ghép Implant cao cấp', cat:'Phẫu thuật', amount:90000000},
    {name:'Bọc sứ Zirconia',          cat:'Phục hình',  amount:72000000},
    {name:'Niềng mắc cài sứ',         cat:'Chỉnh nha',  amount:56000000},
    {name:'Mặt dán sứ Veneer',        cat:'Thẩm mỹ',    amount:42000000}
];

var revenuePayments = [
    {method:'Chuyển khoản', icon:'fa-building-columns', color:'#2563eb', bg:'#dbeafe', pct:50, amount:313300000},
    {method:'Tiền mặt',     icon:'fa-money-bills',      color:'#10b981', bg:'#d1fae5', pct:28, amount:175448000},
    {method:'Trả góp',      icon:'fa-calendar-days',    color:'#f59e0b', bg:'#fef3c7', pct:16, amount:100256000},
    {method:'Thẻ ngân hàng',icon:'fa-credit-card',      color:'#8b5cf6', bg:'#ede9fe', pct:6,  amount:37593000}
];

var revenueTxns = [
    {id:'GD001', date:'25/04/2026', patient:'Nguyễn Văn Hiển',  service:'Bọc sứ Zirconia x2',     doctor:'BS. Lê Quang',   method:'transfer',    amount:12000000},
    {id:'GD002', date:'23/04/2026', patient:'Trần Thị Thảo',    service:'Niềng Invisalign',        doctor:'BS. Trần Tâm',   method:'installment', amount:80000000},
    {id:'GD003', date:'21/04/2026', patient:'Lê Anh Nam',        service:'Implant cao cấp x1',     doctor:'BS. Hoàng Quân', method:'cash',        amount:30000000},
    {id:'GD004', date:'19/04/2026', patient:'Phạm Thu Hà',       service:'Tẩy trắng Laser',        doctor:'BS. Phạm Hương', method:'transfer',    amount:2500000},
    {id:'GD005', date:'17/04/2026', patient:'Hoàng Văn Tuấn',    service:'Khám + Cạo vôi',         doctor:'BS. Nguyễn Hải', method:'cash',        amount:300000},
    {id:'GD006', date:'15/04/2026', patient:'Đặng Thị Hoa',      service:'Bọc sứ Cercon x3',       doctor:'BS. Lê Quang',   method:'transfer',    amount:15000000},
    {id:'GD007', date:'12/04/2026', patient:'Bùi Minh Quân',     service:'Nhổ răng khôn x2',       doctor:'BS. Hoàng Quân', method:'cash',        amount:6000000},
    {id:'GD008', date:'10/04/2026', patient:'Ngô Thị Lan',       service:'Veneer x4',              doctor:'BS. Phạm Hương', method:'installment', amount:28000000},
    {id:'GD009', date:'08/04/2026', patient:'Vũ Minh Đức',       service:'Niềng kim loại',         doctor:'BS. Trần Tâm',   method:'transfer',    amount:25000000},
    {id:'GD010', date:'05/04/2026', patient:'Đinh Bảo Ngọc',     service:'Lấy tủy x3 răng',       doctor:'BS. Nguyễn Hải', method:'card',        amount:4500000}
];

// ==================== DỮ LIỆU GIAO DỊCH THEO NGÀY (demo - năm 2025 & 2026) ====================
var allTxns = [
    // ===== NĂM 2026 =====
    // Tháng 4/2026
    {id:'GD001',date:'2026-04-25',patient:'Nguyễn Văn Hiển',  service:'Bọc sứ Zirconia x2',     doctor:'BS. Lê Quang',   method:'transfer',    amount:12000000, appointments:1},
    {id:'GD002',date:'2026-04-23',patient:'Trần Thị Thảo',    service:'Niềng Invisalign',        doctor:'BS. Trần Tâm',   method:'installment', amount:80000000, appointments:1},
    {id:'GD003',date:'2026-04-21',patient:'Lê Anh Nam',        service:'Implant cao cấp x1',     doctor:'BS. Hoàng Quân', method:'cash',        amount:30000000, appointments:1},
    {id:'GD004',date:'2026-04-19',patient:'Phạm Thu Hà',       service:'Tẩy trắng Laser',        doctor:'BS. Phạm Hương', method:'transfer',    amount:2500000,  appointments:1},
    {id:'GD005',date:'2026-04-17',patient:'Hoàng Văn Tuấn',    service:'Khám + Cạo vôi',         doctor:'BS. Nguyễn Hải', method:'cash',        amount:300000,   appointments:1},
    {id:'GD006',date:'2026-04-15',patient:'Đặng Thị Hoa',      service:'Bọc sứ Cercon x3',       doctor:'BS. Lê Quang',   method:'transfer',    amount:15000000, appointments:1},
    {id:'GD007',date:'2026-04-12',patient:'Bùi Minh Quân',     service:'Nhổ răng khôn x2',       doctor:'BS. Hoàng Quân', method:'cash',        amount:6000000,  appointments:1},
    {id:'GD008',date:'2026-04-10',patient:'Ngô Thị Lan',       service:'Veneer x4',              doctor:'BS. Phạm Hương', method:'installment', amount:28000000, appointments:1},
    {id:'GD009',date:'2026-04-08',patient:'Vũ Minh Đức',       service:'Niềng kim loại',         doctor:'BS. Trần Tâm',   method:'transfer',    amount:25000000, appointments:1},
    {id:'GD010',date:'2026-04-05',patient:'Đinh Bảo Ngọc',     service:'Lấy tủy x3 răng',       doctor:'BS. Nguyễn Hải', method:'card',        amount:4500000,  appointments:1},
    {id:'GD011',date:'2026-04-03',patient:'Trương Quốc Bảo',   service:'Implant TC x1',          doctor:'BS. Hoàng Quân', method:'transfer',    amount:15000000, appointments:1},
    {id:'GD012',date:'2026-04-01',patient:'Lý Thị Nhung',      service:'Tẩy trắng tại nhà',     doctor:'BS. Phạm Hương', method:'cash',        amount:1500000,  appointments:1},
    // Tháng 3/2026
    {id:'GD013',date:'2026-03-28',patient:'Cao Minh Khoa',     service:'Niềng Invisalign',       doctor:'BS. Trần Tâm',   method:'installment', amount:80000000, appointments:1},
    {id:'GD014',date:'2026-03-25',patient:'Phan Thị Kiều',     service:'Bọc sứ Zirconia x3',    doctor:'BS. Lê Quang',   method:'transfer',    amount:18000000, appointments:1},
    {id:'GD015',date:'2026-03-20',patient:'Nguyễn Thành Long', service:'Veneer x5',              doctor:'BS. Phạm Hương', method:'installment', amount:35000000, appointments:1},
    {id:'GD016',date:'2026-03-18',patient:'Trần Khánh Ly',     service:'Implant cao cấp x2',    doctor:'BS. Hoàng Quân', method:'transfer',    amount:60000000, appointments:1},
    {id:'GD017',date:'2026-03-15',patient:'Lê Văn Sơn',        service:'Niềng mắc cài sứ',      doctor:'BS. Trần Tâm',   method:'installment', amount:35000000, appointments:1},
    {id:'GD018',date:'2026-03-10',patient:'Hoàng Thu Hà',      service:'Tẩy trắng Laser',       doctor:'BS. Phạm Hương', method:'cash',        amount:2500000,  appointments:1},
    {id:'GD019',date:'2026-03-05',patient:'Phạm Bình An',      service:'Lấy tủy x2',            doctor:'BS. Nguyễn Hải', method:'transfer',    amount:1600000,  appointments:1},
    {id:'GD020',date:'2026-03-02',patient:'Đỗ Thị Minh',       service:'Cạo vôi + Khám',        doctor:'BS. Nguyễn Hải', method:'cash',        amount:300000,   appointments:1},
    // Tháng 2/2026
    {id:'GD021',date:'2026-02-25',patient:'Võ Minh Tuấn',      service:'Implant TC x1',          doctor:'BS. Hoàng Quân', method:'transfer',    amount:15000000, appointments:1},
    {id:'GD022',date:'2026-02-20',patient:'Bùi Thị Nga',       service:'Bọc sứ Cercon x4',      doctor:'BS. Lê Quang',   method:'transfer',    amount:20000000, appointments:1},
    {id:'GD023',date:'2026-02-15',patient:'Nguyễn Anh Dũng',   service:'Niềng Invisalign',      doctor:'BS. Trần Tâm',   method:'installment', amount:80000000, appointments:1},
    {id:'GD024',date:'2026-02-10',patient:'Trần Thị Bích',     service:'Veneer x3',             doctor:'BS. Phạm Hương', method:'card',        amount:21000000, appointments:1},
    {id:'GD025',date:'2026-02-05',patient:'Lê Quốc Huy',       service:'Tẩy trắng tại nhà',    doctor:'BS. Phạm Hương', method:'cash',        amount:1500000,  appointments:1},
    // Tháng 1/2026
    {id:'GD026',date:'2026-01-28',patient:'Phạm Lan Anh',      service:'Implant cao cấp x1',   doctor:'BS. Hoàng Quân', method:'transfer',    amount:30000000, appointments:1},
    {id:'GD027',date:'2026-01-20',patient:'Hoàng Văn Nam',     service:'Niềng mắc cài kim loại',doctor:'BS. Trần Tâm',   method:'installment', amount:25000000, appointments:1},
    {id:'GD028',date:'2026-01-15',patient:'Đặng Thu Trang',    service:'Bọc sứ Zirconia x2',   doctor:'BS. Lê Quang',   method:'transfer',    amount:12000000, appointments:1},
    {id:'GD029',date:'2026-01-10',patient:'Nguyễn Minh Châu',  service:'Tẩy trắng Laser',      doctor:'BS. Phạm Hương', method:'cash',        amount:2500000,  appointments:1},
    {id:'GD030',date:'2026-01-05',patient:'Trần Hải Đăng',     service:'Lấy tủy + Trám x2',   doctor:'BS. Nguyễn Hải', method:'card',        amount:1500000,  appointments:1},
    // ===== NĂM 2025 =====
    // Tháng 12/2025
    {id:'GD031',date:'2025-12-22',patient:'Lê Thị Thu',        service:'Implant TC x2',         doctor:'BS. Hoàng Quân', method:'transfer',    amount:30000000, appointments:1},
    {id:'GD032',date:'2025-12-18',patient:'Nguyễn Quang Vinh', service:'Niềng Invisalign',      doctor:'BS. Trần Tâm',   method:'installment', amount:80000000, appointments:1},
    {id:'GD033',date:'2025-12-12',patient:'Phạm Thị Hồng',     service:'Veneer x4',             doctor:'BS. Phạm Hương', method:'transfer',    amount:28000000, appointments:1},
    {id:'GD034',date:'2025-12-08',patient:'Trương Minh Hiếu',  service:'Bọc sứ Zirconia x3',   doctor:'BS. Lê Quang',   method:'card',        amount:18000000, appointments:1},
    {id:'GD035',date:'2025-12-03',patient:'Bùi Thanh Tú',      service:'Khám + Cạo vôi',       doctor:'BS. Nguyễn Hải', method:'cash',        amount:300000,   appointments:1},
    // Tháng 11/2025
    {id:'GD036',date:'2025-11-25',patient:'Cao Thị Lan',        service:'Implant cao cấp x1',   doctor:'BS. Hoàng Quân', method:'transfer',    amount:30000000, appointments:1},
    {id:'GD037',date:'2025-11-18',patient:'Đỗ Văn Mạnh',       service:'Niềng mắc cài sứ',     doctor:'BS. Trần Tâm',   method:'installment', amount:35000000, appointments:1},
    {id:'GD038',date:'2025-11-10',patient:'Vũ Thị Ngọc',       service:'Tẩy trắng Laser',      doctor:'BS. Phạm Hương', method:'cash',        amount:2500000,  appointments:1},
    // Tháng 9/2025
    {id:'GD039',date:'2025-09-20',patient:'Lý Văn Đức',        service:'Bọc sứ Cercon x2',     doctor:'BS. Lê Quang',   method:'transfer',    amount:10000000, appointments:1},
    {id:'GD040',date:'2025-09-10',patient:'Phan Thị Mai',      service:'Veneer x3',             doctor:'BS. Phạm Hương', method:'installment', amount:21000000, appointments:1},
    // Tháng 6/2025
    {id:'GD041',date:'2025-06-18',patient:'Ngô Văn Hùng',      service:'Niềng Invisalign',      doctor:'BS. Trần Tâm',   method:'installment', amount:80000000, appointments:1},
    {id:'GD042',date:'2025-06-05',patient:'Đinh Thị Phương',   service:'Implant TC x1',         doctor:'BS. Hoàng Quân', method:'transfer',    amount:15000000, appointments:1},
    // Tháng 3/2025
    {id:'GD043',date:'2025-03-22',patient:'Trần Văn Khoa',     service:'Bọc sứ Zirconia x4',   doctor:'BS. Lê Quang',   method:'transfer',    amount:24000000, appointments:1},
    {id:'GD044',date:'2025-03-10',patient:'Nguyễn Thị Hằng',  service:'Tẩy trắng Laser',      doctor:'BS. Phạm Hương', method:'card',        amount:2500000,  appointments:1},
    // Tháng 1/2025
    {id:'GD045',date:'2025-01-15',patient:'Lê Minh Quân',      service:'Niềng mắc cài kim loại',doctor:'BS. Trần Tâm',   method:'installment', amount:25000000, appointments:1},
    {id:'GD046',date:'2025-01-08',patient:'Phạm Hoàng Anh',    service:'Implant cao cấp x1',   doctor:'BS. Hoàng Quân', method:'transfer',    amount:30000000, appointments:1}
];

// ==================== CHẾ ĐỘ LỌC ====================
var revMode = 'day';

function setRevMode(mode) {
    revMode = mode;
    document.querySelectorAll('.rev-mode-btn').forEach(function(b){b.classList.remove('active');});
    document.getElementById('revMode' + mode.charAt(0).toUpperCase() + mode.slice(1)).classList.add('active');
    document.getElementById('revInputDay').style.display   = mode === 'day'   ? 'flex' : 'none';
    document.getElementById('revInputMonth').style.display = mode === 'month' ? 'flex' : 'none';
    document.getElementById('revInputYear').style.display  = mode === 'year'  ? 'flex' : 'none';
}

function applyRevFilter() {
    var filtered = [];
    var label = '';

    if (revMode === 'day') {
        var from = document.getElementById('revDateFrom').value; // yyyy-mm-dd
        var to   = document.getElementById('revDateTo').value;
        if (!from || !to) { showToast('Vui lòng chọn đủ ngày bắt đầu và kết thúc!', 'error'); return; }
        if (from > to) { showToast('Ngày bắt đầu phải nhỏ hơn ngày kết thúc!', 'error'); return; }
        filtered = allTxns.filter(function(t){ return t.date >= from && t.date <= to; });
        label = '<i class="fas fa-calendar-day"></i> Thống kê từ ' + formatDate(from) + ' đến ' + formatDate(to);
    } else if (revMode === 'month') {
        var mon  = parseInt(document.getElementById('revMonthSel').value);
        var yr   = parseInt(document.getElementById('revMonthYear').value);
        filtered = allTxns.filter(function(t){
            var d = new Date(t.date);
            return d.getFullYear() === yr && (d.getMonth()+1) === mon;
        });
        label = '<i class="fas fa-calendar-alt"></i> Thống kê Tháng ' + mon + ' / ' + yr;
    } else {
        var yr2 = parseInt(document.getElementById('revYearSel').value);
        filtered = allTxns.filter(function(t){ return t.date.startsWith(yr2 + ''); });
        label = '<i class="fas fa-calendar"></i> Thống kê Năm ' + yr2;
    }

    // Tính kết quả
    var totalRev  = filtered.reduce(function(a,t){return a+t.amount;},0);
    var totalAppt = filtered.reduce(function(a,t){return a+t.appointments;},0);
    var avgRev    = filtered.length ? Math.round(totalRev / filtered.length) : 0;

    document.getElementById('revFilterResult').style.display = 'block';
    document.getElementById('revFilterLabel').innerHTML = label;
    document.getElementById('revFrTotal').innerText = fmtRev(totalRev);
    document.getElementById('revFrTxns').innerText  = filtered.length;
    document.getElementById('revFrAppts').innerText = totalAppt;
    document.getElementById('revFrAvg').innerText   = fmtRev(avgRev);

    // --- Mini bar chart ---
    var chartData = buildChartData(filtered, revMode);
    var maxC = Math.max.apply(null, chartData.map(function(c){return c.val;})) || 1;
    document.getElementById('revFrChart').innerHTML = chartData.length === 0
        ? '<div style="margin:auto;color:var(--text-sub);font-size:0.85rem">Không có dữ liệu</div>'
        : chartData.map(function(c) {
            var h = Math.max(4, Math.round(c.val / maxC * 90));
            return '<div class="rev-fr-col" title="' + c.lbl + ': ' + c.val.toLocaleString('vi-VN') + 'đ">' +
                '<div class="rev-fr-col-val">' + (c.val > 0 ? fmtRev(c.val) : '') + '</div>' +
                '<div class="rev-fr-col-bar" style="height:' + h + 'px"></div>' +
                '<div class="rev-fr-col-lbl">' + c.lbl + '</div>' +
            '</div>';
        }).join('');

    // --- Bảng giao dịch ---
    var methodMap = {cash:{label:'Tiền mặt',cls:'rev-cash'}, transfer:{label:'Chuyển khoản',cls:'rev-transfer'}, installment:{label:'Trả góp',cls:'rev-installment'}, card:{label:'Thẻ NH',cls:'rev-card'}};
    document.getElementById('revFrTableBody').innerHTML = filtered.length === 0
        ? '<tr><td colspan="7" style="text-align:center;padding:30px;color:var(--text-sub)">Không có giao dịch nào trong khoảng thời gian này</td></tr>'
        : filtered.sort(function(a,b){return b.date.localeCompare(a.date);}).map(function(t) {
            var m = methodMap[t.method] || {label:t.method, cls:'rev-cash'};
            return '<tr>' +
                '<td><span class="rev-txn-id">#'+t.id+'</span></td>' +
                '<td style="color:var(--text-sub)">'+formatDate(t.date)+'</td>' +
                '<td style="font-weight:600">'+t.patient+'</td>' +
                '<td style="color:var(--text-sub);font-size:0.8rem">'+t.service+'</td>' +
                '<td style="color:var(--text-sub);font-size:0.8rem">'+t.doctor+'</td>' +
                '<td><span class="rev-method-badge '+m.cls+'">'+m.label+'</span></td>' +
                '<td class="rev-txn-amt">'+t.amount.toLocaleString('vi-VN')+'đ</td>' +
            '</tr>';
        }).join('');

    // Scroll đến kết quả
    document.getElementById('revFilterResult').scrollIntoView({behavior:'smooth', block:'start'});
}

function buildChartData(txns, mode) {
    if (txns.length === 0) return [];
    var map = {};
    txns.forEach(function(t) {
        var d = new Date(t.date);
        var key, lbl;
        if (mode === 'day') {
            key = t.date;
            lbl = d.getDate() + '/' + (d.getMonth()+1);
        } else if (mode === 'month') {
            key = t.date.substr(0,7);
            lbl = 'T' + (d.getDate());
        } else {
            key = 'T' + (d.getMonth()+1);
            lbl = key;
        }
        map[key] = (map[key] || {lbl:lbl, val:0});
        map[key].val += t.amount;
    });
    var keys = Object.keys(map).sort();
    return keys.map(function(k){return map[k];});
}


function fmtRev(n) {
    if (n >= 1000000000) return (n/1000000000).toFixed(1) + ' Tỷ';
    if (n >= 1000000)    return (n/1000000).toFixed(0) + ' Tr';
    return (n/1000).toFixed(0) + 'K';
}

function renderRevenue() {
    var activeMonths = revenueMonths.filter(function(r){return r.revenue > 0;});
    var totalYear  = activeMonths.reduce(function(a,r){return a+r.revenue;}, 0);
    var totalAppt  = activeMonths.reduce(function(a,r){return a+r.appointments;}, 0);

    // Tháng gần nhất có dữ liệu
    var lastMon = activeMonths.length > 0 ? activeMonths[activeMonths.length-1] : {revenue:0, appointments:0};
    var prevMon = activeMonths.length > 1 ? activeMonths[activeMonths.length-2] : null;
    var growth = (prevMon && prevMon.revenue > 0) ? Math.round((lastMon.revenue - prevMon.revenue) / prevMon.revenue * 100) : 0;

    document.getElementById('revTotalYear').innerText  = fmtRev(totalYear);
    document.getElementById('revLastMonth').innerText  = fmtRev(lastMon.revenue);
    document.getElementById('revGrowth').innerText     = (growth > 0 ? '+' : '') + growth + '%';
    document.getElementById('revTotalAppt').innerText  = totalAppt;

    // --- Bar chart: luôn hiển thị đủ 12 tháng ---
    var maxRev = Math.max.apply(null, revenueMonths.map(function(r){return r.revenue;})) || 1;
    document.getElementById('revenueBarChart').innerHTML = revenueMonths.map(function(r) {
        var hasData = r.revenue > 0;
        var pct = hasData ? Math.max(4, Math.round(r.revenue / maxRev * 100)) : 3;
        var valM = hasData ? (r.revenue/1000000).toFixed(0) + 'Tr' : '';
        var barColor = hasData ? 'var(--primary-color)' : 'var(--border-color)';
        return '<div class="rev-bc-col" title="' + r.month + '/2026: ' + (hasData ? r.revenue.toLocaleString('vi-VN') + 'đ' : 'Chưa có dữ liệu') + '">' +
            '<div class="rev-bc-val">' + valM + '</div>' +
            '<div class="rev-bc-bar" style="height:' + pct + '%;background:' + barColor + '"></div>' +
            '<div class="rev-bc-month">' + r.month + '</div>' +
        '</div>';
    }).join('');

    // --- Donut chart ---
    var catTotal = revenueByCat.reduce(function(a,c){return a+c.revenue;},0);
    var cx=60, cy=60, r=50, stroke=14, dashTotal=2*Math.PI*r;
    var offset=0;
    var paths = revenueByCat.map(function(c) {
        var pct   = c.revenue/catTotal;
        var dash  = pct * dashTotal;
        var gap   = dashTotal - dash;
        var path  = '<circle cx="'+cx+'" cy="'+cy+'" r="'+r+'"' +
            ' fill="none" stroke="'+c.color+'" stroke-width="'+stroke+'"' +
            ' stroke-dasharray="'+dash.toFixed(2)+' '+gap.toFixed(2)+'"' +
            ' stroke-dashoffset="'+(-offset).toFixed(2)+'"' +
            ' transform="rotate(-90 '+cx+' '+cy+')">' +
            '<title>'+c.name+': '+fmtRev(c.revenue)+'</title></circle>';
        offset += dash;
        return path;
    }).join('');
    document.getElementById('donutSvg').innerHTML = paths;
    document.getElementById('donutTotal').innerText = fmtRev(catTotal);

    document.getElementById('donutLegend').innerHTML = revenueByCat.map(function(c) {
        var pct = Math.round(c.revenue/catTotal*100);
        return '<div class="rev-dl-item">' +
            '<div class="rev-dl-left">' +
                '<span class="rev-dl-dot" style="background:'+c.color+'"></span>' +
                '<span class="rev-dl-name">'+c.name+'</span>' +
            '</div>' +
            '<div class="rev-dl-bar-wrap"><div class="rev-dl-bar-fill" style="width:'+pct+'%;background:'+c.color+'"></div></div>' +
            '<span class="rev-dl-pct">'+pct+'%</span>' +
        '</div>';
    }).join('');

    // --- Top services ---
    var rankClasses = ['gold','silver','bronze','',''];
    document.getElementById('revTopServices').innerHTML = revenueTopServices.map(function(s,i) {
        return '<div class="rev-top-item">' +
            '<div class="rev-top-rank '+(rankClasses[i]||'')+'">'+(i+1)+'</div>' +
            '<div class="rev-top-info">' +
                '<div class="rev-top-name">'+s.name+'</div>' +
                '<div class="rev-top-cat">'+s.cat+'</div>' +
            '</div>' +
            '<div class="rev-top-amt">'+fmtRev(s.amount)+'</div>' +
        '</div>';
    }).join('');

    // --- Payment methods ---
    document.getElementById('revPaymentMethods').innerHTML = revenuePayments.map(function(p) {
        return '<div class="rev-pay-item">' +
            '<div class="rev-pay-row">' +
                '<div class="rev-pay-left">' +
                    '<div class="rev-pay-icon" style="background:'+p.bg+';color:'+p.color+'"><i class="fas '+p.icon+'"></i></div>' +
                    p.method +
                '</div>' +
                '<div style="text-align:right">' +
                    '<div class="rev-pay-pct">'+p.pct+'%</div>' +
                    '<div class="rev-pay-sub">'+fmtRev(p.amount)+'</div>' +
                '</div>' +
            '</div>' +
            '<div class="rev-pay-track"><div class="rev-pay-fill" style="width:'+p.pct+'%;background:'+p.color+'"></div></div>' +
        '</div>';
    }).join('');

    // --- Quarters ---
    var quarters = [
        {label:'Quý I (T1–T3)',  months:[0,1,2]},
        {label:'Quý II (T4–T6)', months:[3,4,5]},
        {label:'Quý III (T7–T9)',months:[6,7,8]},
        {label:'Quý IV (T10–T12)',months:[9,10,11]}
    ];
    var maxQ = 0;
    var qAmts = quarters.map(function(q) {
        var amt = q.months.reduce(function(a,i){return a+revenueMonths[i].revenue;},0);
        if (amt>maxQ) maxQ=amt;
        return amt;
    });
    document.getElementById('revQuarters').innerHTML = quarters.map(function(q,i) {
        var pct = Math.round(qAmts[i]/maxQ*100);
        var appts = q.months.reduce(function(a,m){return a+revenueMonths[m].appointments;},0);
        return '<div class="rev-q-item">' +
            '<div class="rev-q-header"><span class="rev-q-label">'+q.label+'</span><span class="rev-q-amt">'+fmtRev(qAmts[i])+'</span></div>' +
            '<div class="rev-q-track"><div class="rev-q-fill" style="width:'+pct+'%"></div></div>' +
            '<div class="rev-q-sub">'+appts+' lượt khám &nbsp;·&nbsp; Trung bình '+fmtRev(Math.round(qAmts[i]/3))+'/tháng</div>' +
        '</div>';
    }).join('');

    // --- Transactions table ---
    var methodMap = {
        'cash':        {label:'Tiền mặt',      cls:'rev-cash'},
        'transfer':    {label:'Chuyển khoản',   cls:'rev-transfer'},
        'installment': {label:'Trả góp',         cls:'rev-installment'},
        'card':        {label:'Thẻ ngân hàng',  cls:'rev-card'}
    };
    document.getElementById('revTableBody').innerHTML = revenueTxns.map(function(t) {
        var m = methodMap[t.method] || {label:t.method, cls:'rev-cash'};
        return '<tr>' +
            '<td><span class="rev-txn-id">#'+t.id+'</span></td>' +
            '<td style="color:var(--text-sub)">'+t.date+'</td>' +
            '<td style="font-weight:600">'+t.patient+'</td>' +
            '<td style="color:var(--text-sub);font-size:0.8rem">'+t.service+'</td>' +
            '<td style="color:var(--text-sub);font-size:0.8rem">'+t.doctor+'</td>' +
            '<td><span class="rev-method-badge '+m.cls+'">'+m.label+'</span></td>' +
            '<td class="rev-txn-amt">'+t.amount.toLocaleString('vi-VN')+'đ</td>' +
        '</tr>';
    }).join('');
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
});