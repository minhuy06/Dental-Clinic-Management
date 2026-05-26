/**
 * ==================== NHA KHOA 5AE - ADMIN.JS ====================
 */

// ==================== DỮ LIỆU ====================
var services = [];

var staffList = [
    {id:1, name:'BS. Nguyễn Hải', role:'doctor', specialty:'Răng tổng quát', degree:'CKI', phone:'0901234561', startDate:'2020-01-10', status:'active'},
    {id:2, name:'BS. Trần Tâm', role:'doctor', specialty:'Chỉnh nha', degree:'Thạc sĩ', phone:'0901234562', startDate:'2019-06-01', status:'active'},
    {id:3, name:'BS. Lê Quang', role:'doctor', specialty:'Phục hình', degree:'Tiến sĩ', phone:'0901234563', startDate:'2018-03-15', status:'active'},
    {id:4, name:'BS. Phạm Hương', role:'doctor', specialty:'Thẩm mỹ nha', degree:'CKI', phone:'0901234564', startDate:'2021-09-01', status:'active'},
    {id:5, name:'BS. Hoàng Quân', role:'doctor', specialty:'Phẫu thuật miệng', degree:'CKII', phone:'0901234565', startDate:'2017-11-20', status:'active'},
    {id:6, name:'Trần Văn Hùng', role:'receptionist', specialty:'Tiếp nhận bệnh nhân', degree:'', phone:'0912345602', startDate:'2022-04-10', status:'active'}
];

var accounts = [
    {id:1, name:'Admin Hệ Thống', username:'admin', role:'admin', phone:'0900000001', dob:'1985-03-10', gender:'male', specialty:'', degree:'', createdDate:'2020-01-01', status:'active'},
    {id:2, name:'BS. Nguyễn Hải', username:'bs.nguyenhai', role:'doctor', phone:'0901234561', dob:'1982-07-15', gender:'male', specialty:'Răng tổng quát', degree:'CKI', createdDate:'2020-01-10', status:'active'},
    {id:3, name:'BS. Trần Tâm', username:'bs.trantam', role:'doctor', phone:'0901234562', dob:'1985-11-20', gender:'female', specialty:'Chỉnh nha', degree:'Thạc sĩ', createdDate:'2019-06-01', status:'active'},
    {id:4, name:'BS. Lê Quang', username:'bs.lequang', role:'doctor', phone:'0901234563', dob:'1979-05-08', gender:'male', specialty:'Phục hình', degree:'Tiến sĩ', createdDate:'2018-03-15', status:'active'},
    {id:5, name:'BS. Phạm Hương', username:'bs.phamhuong', role:'doctor', phone:'0901234564', dob:'1988-02-14', gender:'female', specialty:'Thẩm mỹ nha', degree:'CKI', createdDate:'2021-09-01', status:'active'},
    {id:6, name:'BS. Hoàng Quân', username:'bs.hoangquan', role:'doctor', phone:'0901234565', dob:'1975-09-30', gender:'male', specialty:'Phẫu thuật miệng', degree:'CKII', createdDate:'2017-11-20', status:'active'},
    {id:7, name:'Trần Văn Hùng', username:'nv.hung', role:'staff', phone:'0912345602', dob:'1995-04-22', gender:'male', specialty:'', degree:'', createdDate:'2022-04-10', status:'active'},
    {id:9, name:'Nguyễn Văn Hiển', username:'0908123456', role:'customer', phone:'0908123456', dob:'1990-06-12', gender:'male', specialty:'', degree:'', createdDate:'2025-06-12', status:'active'},
    {id:10, name:'Trần Thị Thảo', username:'0918765432', role:'customer', phone:'0918765432', dob:'1993-09-20', gender:'female', specialty:'', degree:'', createdDate:'2025-09-20', status:'active'},
    {id:11, name:'Lê Anh Nam', username:'0987654321', role:'customer', phone:'0987654321', dob:'1988-03-08', gender:'male', specialty:'', degree:'', createdDate:'2025-03-08', status:'inactive'},
    {id:12, name:'Phạm Thu Hà', username:'0978123456', role:'customer', phone:'0978123456', dob:'1997-01-15', gender:'female', specialty:'', degree:'', createdDate:'2026-01-15', status:'active'}
];

// Ca làm việc demo - Tháng 4 & 5/2026
var pendingShiftBookings = [];
var shifts = [
    // ---- Tuần 20–25/4 ----
    {id:1,  staffId:2, staffName:'BS. Nguyễn Hải',  shiftType:'morning',   date:'2026-04-20', note:''},
    {id:2,  staffId:5, staffName:'BS. Phạm Hương',  shiftType:'morning',   date:'2026-04-20', note:''},
    {id:3,  staffId:3, staffName:'BS. Trần Tâm',    shiftType:'afternoon', date:'2026-04-20', note:''},
    {id:4,  staffId:7, staffName:'Trần Văn Hùng',   shiftType:'morning',   date:'2026-04-20', note:''},
    {id:5,  staffId:4, staffName:'BS. Lê Quang',    shiftType:'afternoon', date:'2026-04-21', note:''},
    {id:6,  staffId:2, staffName:'BS. Nguyễn Hải',  shiftType:'afternoon', date:'2026-04-21', note:''},
    {id:7,  staffId:6, staffName:'BS. Hoàng Quân',  shiftType:'morning',   date:'2026-04-21', note:'Phẫu thuật'},
    {id:8,  staffId:7, staffName:'Trần Văn Hùng',   shiftType:'morning',   date:'2026-04-21', note:''},
    {id:9,  staffId:5, staffName:'BS. Phạm Hương',  shiftType:'morning',   date:'2026-04-22', note:''},
    {id:10, staffId:3, staffName:'BS. Trần Tâm',    shiftType:'morning',   date:'2026-04-22', note:''},
    {id:11, staffId:4, staffName:'BS. Lê Quang',    shiftType:'morning',   date:'2026-04-22', note:''},
    {id:12, staffId:2, staffName:'BS. Nguyễn Hải',  shiftType:'afternoon', date:'2026-04-22', note:''},
    {id:13, staffId:6, staffName:'BS. Hoàng Quân',  shiftType:'afternoon', date:'2026-04-23', note:''},
    {id:14, staffId:7, staffName:'Trần Văn Hùng',   shiftType:'morning',   date:'2026-04-23', note:''},
    {id:15, staffId:5, staffName:'BS. Phạm Hương',  shiftType:'afternoon', date:'2026-04-23', note:''},
    // CN 26/4 - chỉ sáng
    {id:16, staffId:2, staffName:'BS. Nguyễn Hải',  shiftType:'morning',   date:'2026-04-26', note:''},
    {id:17, staffId:7, staffName:'Trần Văn Hùng',   shiftType:'morning',   date:'2026-04-26', note:''},
    // ---- Tuần 27–30/4 ----
    {id:18, staffId:4, staffName:'BS. Lê Quang',    shiftType:'morning',   date:'2026-04-27', note:''},
    {id:19, staffId:5, staffName:'BS. Phạm Hương',  shiftType:'morning',   date:'2026-04-27', note:''},
    {id:20, staffId:3, staffName:'BS. Trần Tâm',    shiftType:'afternoon', date:'2026-04-27', note:''},
    {id:21, staffId:2, staffName:'BS. Nguyễn Hải',  shiftType:'morning',   date:'2026-04-28', note:''},
    {id:22, staffId:6, staffName:'BS. Hoàng Quân',  shiftType:'morning',   date:'2026-04-28', note:'Phẫu thuật'},
    {id:23, staffId:3, staffName:'BS. Trần Tâm',    shiftType:'afternoon', date:'2026-04-28', note:''},
    {id:24, staffId:7, staffName:'Trần Văn Hùng',   shiftType:'morning',   date:'2026-04-28', note:''},
    {id:25, staffId:5, staffName:'BS. Phạm Hương',  shiftType:'morning',   date:'2026-04-29', note:''},
    {id:26, staffId:4, staffName:'BS. Lê Quang',    shiftType:'afternoon', date:'2026-04-29', note:''},
    {id:27, staffId:2, staffName:'BS. Nguyễn Hải',  shiftType:'afternoon', date:'2026-04-29', note:''},
    {id:28, staffId:6, staffName:'BS. Hoàng Quân',  shiftType:'morning',   date:'2026-04-30', note:''},
    {id:29, staffId:3, staffName:'BS. Trần Tâm',    shiftType:'morning',   date:'2026-04-30', note:''},
    {id:30, staffId:7, staffName:'Trần Văn Hùng',   shiftType:'afternoon', date:'2026-04-30', note:''},
    // ---- Tháng 5 ----
    {id:31, staffId:2, staffName:'BS. Nguyễn Hải',  shiftType:'morning',   date:'2026-05-04', note:''},
    {id:32, staffId:5, staffName:'BS. Phạm Hương',  shiftType:'afternoon', date:'2026-05-04', note:''},
    {id:33, staffId:4, staffName:'BS. Lê Quang',    shiftType:'morning',   date:'2026-05-05', note:''},
    {id:34, staffId:6, staffName:'BS. Hoàng Quân',  shiftType:'morning',   date:'2026-05-05', note:''},
    {id:35, staffId:3, staffName:'BS. Trần Tâm',    shiftType:'afternoon', date:'2026-05-06', note:''},
    {id:36, staffId:7, staffName:'Trần Văn Hùng',   shiftType:'morning',   date:'2026-05-06', note:''},
    {id:37, staffId:2, staffName:'BS. Nguyễn Hải',  shiftType:'morning',   date:'2026-05-07', note:''},
    {id:38, staffId:5, staffName:'BS. Phạm Hương',  shiftType:'morning',   date:'2026-05-07', note:''}
];

// ==================== BIẾN TOÀN CỤC ====================
var svcFilter = 'all', svcPage = 1, svcPerPage = 6, editingSvcId = null;
var accFilter = 'all', accPage = 1, accPerPage = 8, editingAccId = null;
var staffInfoCurrentId = null;
var ADMIN_LAST_TAB_KEY = 'admin:lastTab';

// Schedule state
var schCurrentDate = new Date();
var editingShiftId = null;

// Doanh thu — mặc định rỗng, được ghi đè từ __ADMIN_REVENUE__ (JSP/DB)
var revenueMonths = [];
var revenueByCat = [];
var revenueTopServices = [];
var revenuePayments = [];
var revenueTxns = [];
var allTxns = [];
var revMode = 'day';

(function initEmptyRevenueMonths() {
    for (var i = 1; i <= 12; i++) {
        revenueMonths.push({ month: 'T' + i, revenue: 0, appointments: 0 });
    }
})();

// ==================== OVERRIDE DATA TỪ BACKEND ====================
(function initAdminBootstrap() {
    if (typeof AppBootstrap !== 'undefined') {
        var cp = AppBootstrap.getMetaContent('context-path');
        if (cp) window.ADMIN_CONTEXT_PATH = cp;
        var servicesSeed = AppBootstrap.readJsonScript('adminServicesJson', null);
        var accountsSeed = AppBootstrap.readJsonScript('adminAccountsJson', null);
        var shiftsSeed = AppBootstrap.readJsonScript('adminShiftsJson', null);
        var revenueSeed = AppBootstrap.readJsonScript('adminRevenueJson', null);
        var pendingSeed = AppBootstrap.readJsonScript('adminPendingBookingsJson', null);
        if (servicesSeed !== null) window.__ADMIN_SERVICES__ = servicesSeed;
        if (accountsSeed !== null) window.__ADMIN_ACCOUNTS__ = accountsSeed;
        if (shiftsSeed !== null) window.__ADMIN_SHIFTS__ = shiftsSeed;
        if (revenueSeed !== null) window.__ADMIN_REVENUE__ = revenueSeed;
        if (pendingSeed !== null) window.__ADMIN_PENDING_BOOKINGS__ = pendingSeed;
    }
})();

// Nếu có dữ liệu từ server thì dùng, không thì giữ demo data ở trên.
(function() {
    if (Array.isArray(window.__ADMIN_SERVICES__)) {
        services = window.__ADMIN_SERVICES__;
        console.info('[admin] services loaded from DB:', services.length);
    }
    if (Array.isArray(window.__ADMIN_ACCOUNTS__)) {
        accounts = window.__ADMIN_ACCOUNTS__;
        console.info('[admin] accounts loaded from DB:', accounts.length);
    }
    if (window.__ADMIN_SHIFTS__ && window.__ADMIN_SHIFTS__.length > 0) {
        shifts = window.__ADMIN_SHIFTS__;
        console.info('[admin] shifts loaded from DB:', shifts.length);
    }
    if (Array.isArray(window.__ADMIN_PENDING_BOOKINGS__)) {
        pendingShiftBookings = window.__ADMIN_PENDING_BOOKINGS__;
        console.info('[admin] pending shift bookings:', pendingShiftBookings.length);
        renderAdminBellNotifications();
    }
    if (window.__ADMIN_REVENUE__) {
        var r = window.__ADMIN_REVENUE__;
        if (r.months && r.months.length)      revenueMonths      = r.months;
        if (r.byCat)                          revenueByCat       = r.byCat;
        if (r.topServices)                    revenueTopServices = r.topServices;
        if (r.payments)                       revenuePayments    = r.payments;
        if (r.txns)                           revenueTxns        = r.txns;
        if (r.allTxns)                        allTxns            = r.allTxns;
        window.__ADMIN_REVENUE_SEEDED__ = true;
        console.info('[admin] revenue loaded from DB:', (allTxns.length || revenueTxns.length) + ' giao dịch');
        initRevYearSelects();
    }
    schCurrentDate = new Date();
})();

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

// ==================== CỜ HIỆU MOCK/REAL + DATA SOURCE ====================
var ADMIN_CONFIG = {
    // true: dùng dữ liệu giả (mảng local), false: gọi backend thật
    USE_MOCK: true,
    API_BASE: '/api',
    MOCK_DELAY_MS: 120
};

function waitMs(ms) {
    return new Promise(function(resolve) { setTimeout(resolve, ms); });
}

function cloneData(data) {
    return JSON.parse(JSON.stringify(data));
}

async function requestApi(path, options) {
    var opts = options || {};
    var method = opts.method || 'GET';
    var body = opts.body;
    var headers = opts.headers || {};
    var fetchOptions = {
        method: method,
        headers: Object.assign({'Content-Type': 'application/json'}, headers)
    };
    if (body !== undefined && body !== null) fetchOptions.body = JSON.stringify(body);

    var response = await fetch(ADMIN_CONFIG.API_BASE + '/' + path, fetchOptions);
    var json = null;
    try { json = await response.json(); } catch (e) { json = null; }
    if (!response.ok) {
        var msg = (json && json.message) ? json.message : ('Lỗi HTTP: ' + response.status);
        throw new Error(msg);
    }
    return json;
}

function normalizeListRes(res) {
    if (!res) return [];
    if (Array.isArray(res)) return res;
    if (Array.isArray(res.data)) return res.data;
    return [];
}

var mockDataSource = {
    services: {
        list: async function() {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            return cloneData(services);
        },
        create: async function(payload) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            var newId = services.length ? Math.max.apply(null, services.map(function(s){ return s.id; })) + 1 : 1;
            services.push(Object.assign({id:newId}, payload));
            return {success:true};
        },
        update: async function(payload) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            var idx = services.findIndex(function(s){ return s.id === payload.id; });
            if (idx === -1) return {success:false, message:'Không tìm thấy dịch vụ'};
            services[idx] = Object.assign({}, services[idx], payload);
            return {success:true};
        },
        remove: async function(id) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            services = services.filter(function(s){ return s.id !== id; });
            return {success:true};
        },
        toggleStatus: async function(id, status) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            var s = services.find(function(x){ return x.id === id; });
            if (!s) return {success:false};
            s.status = status;
            return {success:true};
        }
    },
    accounts: {
        list: async function() {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            return cloneData(accounts);
        },
        create: async function(payload) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            var newId = accounts.length ? Math.max.apply(null, accounts.map(function(a){ return a.id; })) + 1 : 1;
            accounts.push(Object.assign({id:newId}, payload));
            return {success:true};
        },
        update: async function(payload) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            var idx = accounts.findIndex(function(a){ return a.id === payload.id; });
            if (idx === -1) return {success:false, message:'Không tìm thấy tài khoản'};
            accounts[idx] = Object.assign({}, accounts[idx], payload);
            return {success:true};
        },
        remove: async function(id) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            accounts = accounts.filter(function(a){ return a.id !== id; });
            return {success:true};
        },
        toggleStatus: async function(id, status) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            var a = accounts.find(function(x){ return x.id === id; });
            if (!a) return {success:false};
            a.status = status;
            return {success:true};
        }
    },
    shifts: {
        list: async function() {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            return cloneData(shifts);
        },
        create: async function(payload) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            var newId = shifts.length ? Math.max.apply(null, shifts.map(function(s){ return s.id; })) + 1 : 1;
            shifts.push(Object.assign({id:newId}, payload));
            return {success:true};
        },
        update: async function(payload) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            var idx = shifts.findIndex(function(s){ return s.id === payload.id; });
            if (idx === -1) return {success:false, message:'Không tìm thấy ca làm'};
            shifts[idx] = Object.assign({}, shifts[idx], payload);
            return {success:true};
        },
        remove: async function(id) {
            await waitMs(ADMIN_CONFIG.MOCK_DELAY_MS);
            shifts = shifts.filter(function(s){ return s.id !== id; });
            return {success:true};
        }
    },
};

var apiDataSource = {
    services: {
        list: async function() { return normalizeListRes(await requestApi('services')); },
        create: async function(payload) { return requestApi('services/add', {method:'POST', body:payload}); },
        update: async function(payload) { return requestApi('services/update', {method:'PUT', body:payload}); },
        remove: async function(id) { return requestApi('services/delete?id=' + id, {method:'DELETE'}); },
        toggleStatus: async function(id, status) { return requestApi('services/toggle-status', {method:'PUT', body:{id:id, status:status}}); }
    },
    accounts: {
        list: async function() { return normalizeListRes(await requestApi('accounts')); },
        create: async function(payload) { return requestApi('accounts/add', {method:'POST', body:payload}); },
        update: async function(payload) { return requestApi('accounts/update', {method:'PUT', body:payload}); },
        remove: async function(id) { return requestApi('accounts/delete?id=' + id, {method:'DELETE'}); },
        toggleStatus: async function(id, status) { return requestApi('accounts/toggle-status', {method:'PUT', body:{id:id, status:status}}); }
    },
    shifts: {
        list: async function() { return normalizeListRes(await requestApi('shifts')); },
        create: async function(payload) { return requestApi('shifts/add', {method:'POST', body:payload}); },
        update: async function(payload) { return requestApi('shifts/update', {method:'PUT', body:payload}); },
        remove: async function(id) { return requestApi('shifts/delete?id=' + id, {method:'DELETE'}); }
    },
    revenue: {
        summary: async function() { return requestApi('revenue/summary'); }
    }
};

var dataSource = ADMIN_CONFIG.USE_MOCK ? mockDataSource : apiDataSource;

async function withApiGuard(action, successMsg) {
    try {
        var res = await action();
        if (res && res.success === false) {
            showToast(res.message || 'Thao tác thất bại!', 'error');
            return null;
        }
        if (successMsg) showToast(successMsg);
        return res || {success:true};
    } catch (error) {
        console.error('Admin data error:', error);
        showToast(error.message || 'Có lỗi xảy ra khi kết nối server!', 'error');
        return null;
    }
}

async function loadServicesFromServer() {
    var list = await withApiGuard(function() { return dataSource.services.list(); });
    if (!list) return;
    services = normalizeListRes(list);
    renderServices();
    updateSvcStats();
}

async function loadAccountsFromServer() {
    var list = await withApiGuard(function() { return dataSource.accounts.list(); });
    if (!list) return;
    accounts = normalizeListRes(list);
    renderAccounts();
    updateAccStats();
}

async function loadShiftsFromServer() {
    var list = await withApiGuard(function() { return dataSource.shifts.list(); });
    if (!list) return;
    shifts = normalizeListRes(list);
    renderSchedule();
}

async function loadRevenueFromServer() {
    if (window.__ADMIN_REVENUE_SEEDED__) {
        initRevYearSelects();
        renderRevenue();
        return;
    }
    if (ADMIN_CONFIG.USE_MOCK || !apiDataSource.revenue) {
        initRevYearSelects();
        renderRevenue();
        return;
    }
    var r = await withApiGuard(function() { return apiDataSource.revenue.summary(); });
    if (!r) return;
    if (r.months) revenueMonths = r.months;
    if (r.byCat) revenueByCat = r.byCat;
    if (r.topServices) revenueTopServices = r.topServices;
    if (r.payments) revenuePayments = r.payments;
    if (r.txns) revenueTxns = r.txns;
    if (r.allTxns) allTxns = r.allTxns;
    initRevYearSelects();
    renderRevenue();
}

// ==================== TAB SWITCHING ====================
function switchTab(tab, el) {
    document.querySelectorAll('.tab-panel').forEach(function(p) { p.classList.remove('active'); });
    document.querySelectorAll('.nav-menu a').forEach(function(a) { a.classList.remove('active'); });
    document.getElementById('panel-' + tab).classList.add('active');
    if (el) el.classList.add('active');
    try { localStorage.setItem(ADMIN_LAST_TAB_KEY, tab); } catch (e) {}
    if (tab === 'services')  renderServices();
    if (tab === 'schedule')  renderSchedule();
    if (tab === 'accounts')  renderAccounts();
    if (tab === 'revenue')   renderRevenue();
    return false;
}

// ==================== FILTER HELPERS ====================
function setSvcFilter(val, btn) {
    svcFilter = val; svcPage = 1;
    document.querySelectorAll('#panel-services .filter-btn').forEach(function(b){b.classList.remove('active');});
    btn.classList.add('active');
    renderServices();
}
function setAccFilter(val, btn) {
    accFilter = val; accPage = 1;
    document.querySelectorAll('#panel-accounts .filter-btn').forEach(function(b){b.classList.remove('active');});
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
            return '<div class="service-card">' +
                '<div class="service-card-header">' +
                    '<div class="service-icon" style="background:' + cat.bg + ';color:' + cat.color + '"><i class="fas ' + cat.icon + '"></i></div>' +
                '</div>' +
                '<span class="service-cat" style="background:' + cat.bg + ';color:' + cat.color + '">' + cat.label + '</span>' +
                '<div class="service-name">' + escapeHtml(s.name) + '</div>' +
                '<div class="service-meta">' +
                    '<div class="service-price">' + formatPrice(s.price) + (s.perUnit ? ' / ' + s.unit : '') + '</div>' +
                    '<div class="service-time"><i class="fas fa-clock"></i> ' + s.time + '</div>' +
                '</div>' +
                '<div class="service-actions">' +
                    '<button class="btn-action btn-edit" onclick="editService(' + s.id + ')" title="Sửa"><i class="fas fa-edit"></i></button>' +
                    '<button class="btn-action btn-delete" onclick="deleteService(' + s.id + ')" title="Xóa"><i class="fas fa-trash"></i></button>' +
                '</div>' +
            '</div>';
        }).join('');
    }

    renderPagination('svcPagination', total, svcPerPage, svcPage, 'goSvcPage');
    updateSvcStats();
}

function updateSvcStats() {
    document.getElementById('statTotalServices').innerText = services.length;
    var total = services.reduce(function(a,s){return a+s.price;}, 0);
    var avg = services.length ? Math.round(total/services.length) : 0;
    document.getElementById('statAvgPrice').innerText = formatPrice(avg);
    var maxPrice = services.length ? Math.max.apply(null, services.map(function(s){return s.price;})) : 0;
    document.getElementById('statMaxPrice').innerText = formatPrice(maxPrice);
}

function goSvcPage(p) { svcPage = p; renderServices(); }

function toggleSvcUnit() {
    var show = document.getElementById('svcPerUnit').value === 'true';
    document.getElementById('svcUnitGroup').style.display = show ? '' : 'none';
    if (!show) document.getElementById('svcUnit').value = '';
}

function openServiceModal() {
    editingSvcId = null;
    document.getElementById('serviceModalTitle').innerText = 'Thêm dịch vụ mới';
    document.getElementById('svcId').value = '';
    document.getElementById('svcName').value = '';
    document.getElementById('svcCat').value = 'kham';
    document.getElementById('svcPrice').value = '';
    document.getElementById('svcTime').value = '';
    document.getElementById('svcPerUnit').value = 'false';
    document.getElementById('svcUnit').value = '';
    document.getElementById('svcUnitGroup').style.display = 'none';
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
    document.getElementById('svcPrice').value = s.price;
    document.getElementById('svcTime').value = s.time;
    document.getElementById('svcPerUnit').value = s.perUnit ? 'true' : 'false';
    document.getElementById('svcUnit').value = s.unit || '';
    document.getElementById('svcUnitGroup').style.display = s.perUnit ? '' : 'none';
    document.getElementById('serviceModal').style.display = 'flex';
}

function closeServiceModal() { document.getElementById('serviceModal').style.display = 'none'; }

async function saveService() {
    var name = document.getElementById('svcName').value.trim();
    var price = parseInt(document.getElementById('svcPrice').value);
    
    if (!name || !price) { 
        showToast('Vui lòng điền đầy đủ tên và giá dịch vụ!', 'error'); 
        return; 
    }
    
    var perUnit = document.getElementById('svcPerUnit').value === 'true';
    var data = {
        name: name,
        cat: document.getElementById('svcCat').value,
        desc: '', 
        price: price,
        time: document.getElementById('svcTime').value.trim() || 'Liên hệ',
        perUnit: perUnit,
        unit: perUnit ? document.getElementById('svcUnit').value.trim() : '',
        status: editingSvcId ? services.find(s => s.id === editingSvcId).status : 'active'
    };

    if (editingSvcId) {
        data.id = editingSvcId;
        var updateRes = await withApiGuard(async function() {
            var params = new URLSearchParams();
            params.append('action', 'update');
            params.append('id', String(data.id));
            params.append('name', data.name);
            params.append('cat', data.cat);
            params.append('price', String(data.price));
            params.append('time', data.time);
            params.append('perUnit', String(data.perUnit));
            const res = await fetch((window.ADMIN_CONTEXT_PATH || '') + '/admin/services', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: params.toString()
            });
            var json = null;
            try { json = await res.json(); } catch (e) { json = null; }
            if (!res.ok || (json && json.success === false)) {
                throw new Error((json && json.message) ? json.message : 'Không cập nhật được dịch vụ');
            }
            return { success: true };
        }, 'Đã cập nhật dịch vụ');
        if (updateRes) window.location.reload();
    } else {
        var createRes = await withApiGuard(async function() {
            var params = new URLSearchParams();
            params.append('name', data.name);
            params.append('cat', data.cat);
            params.append('price', String(data.price));
            params.append('time', data.time);
            params.append('perUnit', String(data.perUnit));
            const res = await fetch((window.ADMIN_CONTEXT_PATH || '') + '/admin/services', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: params.toString()
            });
            var json = null;
            try { json = await res.json(); } catch (e) { json = null; }
            if (!res.ok || (json && json.success === false)) {
                throw new Error((json && json.message) ? json.message : 'Không thêm được dịch vụ');
            }
            return { success: true };
        }, 'Đã thêm dịch vụ mới');
        if (createRes) window.location.reload();
    }
    
    closeServiceModal();
}

async function deleteService(id) {
    const ok = await AppNotify.confirm({ message: 'Bạn có chắc muốn xóa dịch vụ này?' });
    if (!ok) return;
    var res = await withApiGuard(async function() {
        var params = new URLSearchParams();
        params.append('action', 'delete');
        params.append('id', String(id));
        const response = await fetch((window.ADMIN_CONTEXT_PATH || '') + '/admin/services', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: params.toString()
        });
        var json = null;
        try { json = await response.json(); } catch (e) { json = null; }
        if (!response.ok || (json && json.success === false)) {
            throw new Error((json && json.message) ? json.message : 'Không xóa được dịch vụ');
        }
        return { success: true };
    }, 'Đã xóa dịch vụ');
    if (res) window.location.reload();
}

async function toggleServiceStatus(id) {
    var s = services.find(x => x.id === id);
    if (!s) return;
    
    var newStatus = s.status === 'active' ? 'inactive' : 'active';
    
    var msg = newStatus === 'active' ? 'Đã kích hoạt dịch vụ' : 'Đã tạm ngưng dịch vụ';
    var res = await withApiGuard(function() { return dataSource.services.toggleStatus(id, newStatus); }, msg);
    if (res) await loadServicesFromServer();
}

// ==================== LỊCH LÀM VIỆC ====================
var shiftConfig = {
    morning:   {label:'Ca Sáng',  time:'8:00–12:00',  color:'#1d4ed8', bg:'#dbeafe', lightBg:'#eff6ff'},
    afternoon: {label:'Ca Chiều', time:'12:00–17:00', color:'#047857', bg:'#d1fae5', lightBg:'#f0fdf4'}
};

// CN (0) chỉ có Ca Sáng; T2–T7 có cả 2 ca
function schGetAvailableShifts(dateStr) {
    var dow = new Date(dateStr + 'T00:00:00').getDay();
    return dow === 0 ? ['morning'] : ['morning', 'afternoon'];
}

var SCH_DOW    = ['CN','T2','T3','T4','T5','T6','T7'];
var SCH_MONTHS = ['Tháng 1','Tháng 2','Tháng 3','Tháng 4','Tháng 5','Tháng 6',
                  'Tháng 7','Tháng 8','Tháng 9','Tháng 10','Tháng 11','Tháng 12'];

function toYMD(d) {
    var y = d.getFullYear();
    var m = d.getMonth() + 1;
    var dd = d.getDate();
    return y + '-' + (m < 10 ? '0' : '') + m + '-' + (dd < 10 ? '0' : '') + dd;
}

function renderSchedule() {
    updateSchStats();
    renderAdminBellNotifications();
    renderMonthCalendar();
}

function formatPendingDate(dateStr) {
    if (!dateStr) return '';
    var p = dateStr.split('-');
    if (p.length !== 3) return dateStr;
    return p[2] + '/' + p[1] + '/' + p[0];
}

function buildPendingBellItemHtml(b) {
    var svcs = b.services ? escapeHtml(b.services) : '—';
    var noteRow = b.note ? ('<div class="form-group bell-pending-full"><label>Ghi chú</label><div class="bell-readonly">' + escapeHtml(b.note) + '</div></div>') : '';
    return '<div class="bell-pending-card">' +
        '<div class="bell-pending-card-head"><strong>' + escapeHtml(b.patientName || 'Bệnh nhân') + '</strong><span class="bell-pending-tag">Chờ gán BS</span></div>' +
        '<div class="form-row">' +
            '<div class="form-group"><label>Số điện thoại</label><div class="bell-readonly">' + escapeHtml(b.phone || '—') + '</div></div>' +
            '<div class="form-group"><label>Ngày · Giờ khám</label><div class="bell-readonly">' + formatPendingDate(b.date) + ' · ' + escapeHtml(b.time || '') + '</div></div>' +
        '</div>' +
        '<div class="form-group bell-pending-full"><label>Dịch vụ</label><div class="bell-readonly">' + svcs + '</div></div>' +
        noteRow +
        '<button type="button" class="btn-save bell-pending-btn" data-pending-date="' + escapeHtml(b.date || '') + '" data-pending-time="' + escapeHtml(b.time || '') + '" onclick="openScheduleFromBellItem(this)"><i class="fas fa-plus-circle"></i> Phân công ca</button>' +
        '</div>';
}

function renderAdminBellNotifications() {
    var badge = document.getElementById('adminBellBadge');
    var list = document.getElementById('adminBellList');
    var btn = document.getElementById('adminBellBtn');
    var count = (pendingShiftBookings && pendingShiftBookings.length) || 0;
    if (badge) {
        badge.textContent = count > 99 ? '99+' : String(count);
        badge.classList.toggle('is-visible', count > 0);
        badge.setAttribute('aria-hidden', count > 0 ? 'false' : 'true');
    }
    if (btn) {
        btn.classList.toggle('has-notify', count > 0);
        btn.setAttribute('aria-label', count > 0
            ? ('Thông báo: ' + count + ' lịch đã xác nhận, chờ gán bác sĩ')
            : 'Thông báo lịch chờ gán bác sĩ');
    }
    if (list) {
        list.innerHTML = count === 0
            ? '<p class="bell-empty-msg"><i class="fas fa-check-circle"></i> Không có lịch chờ gán bác sĩ.</p>'
            : '<div class="bell-pending-list">' + pendingShiftBookings.map(buildPendingBellItemHtml).join('') + '</div>';
    }
}

function toggleAdminBellPanel(e) {
    if (e) { e.preventDefault(); e.stopPropagation(); }
    var modal = document.getElementById('adminBellModal');
    var btn = document.getElementById('adminBellBtn');
    if (!modal) return;
    if (modal.style.display === 'flex') {
        closeAdminBellPanel();
    } else {
        renderAdminBellNotifications();
        modal.style.display = 'flex';
        if (btn) btn.setAttribute('aria-expanded', 'true');
    }
}

function closeAdminBellPanel() {
    var modal = document.getElementById('adminBellModal');
    var btn = document.getElementById('adminBellBtn');
    if (modal) modal.style.display = 'none';
    if (btn) btn.setAttribute('aria-expanded', 'false');
}

function openScheduleFromBellItem(el) {
    if (!el) return;
    openScheduleFromBell(el.getAttribute('data-pending-date') || '', el.getAttribute('data-pending-time') || '');
}

function inferShiftTypeFromTime(timeStr) {
    if (!timeStr) return 'morning';
    var h = parseInt(String(timeStr).split(':')[0], 10);
    return (isNaN(h) || h < 12) ? 'morning' : 'afternoon';
}

function openScheduleFromBell(dateStr, timeStr) {
    closeAdminBellPanel();
    if (dateStr) {
        var p = dateStr.split('-');
        if (p.length === 3) {
            schCurrentDate = new Date(parseInt(p[0], 10), parseInt(p[1], 10) - 1, parseInt(p[2], 10));
        }
    }
    var link = document.querySelector('.nav-menu a[onclick*="switchTab(\'schedule\'"]');
    switchTab('schedule', link || null);
    if (dateStr) {
        var shiftType = inferShiftTypeFromTime(timeStr || '');
        setTimeout(function() { schOpenModalFor(dateStr, shiftType); }, 200);
    }
}

function updateSchStats() {
    var staff = accounts.filter(function(a) { return a.role === 'doctor' || a.role === 'staff'; });
    document.getElementById('schStatTotal').innerText = staff.length;

    var today = toYMD(new Date());
    document.getElementById('schStatToday').innerText = shifts.filter(function(s) { return s.date === today; }).length;

    var y = schCurrentDate.getFullYear();
    var m = schCurrentDate.getMonth() + 1;
    var prefix = y + '-' + (m < 10 ? '0' : '') + m;
    document.getElementById('schStatWeek').innerText = shifts.filter(function(s) { return s.date.indexOf(prefix) === 0; }).length;

    var daysInMonth = new Date(y, m, 0).getDate();
    var empty = 0;
    for (var d = 1; d <= daysInMonth; d++) {
        var dStr = y + '-' + (m < 10 ? '0' : '') + m + '-' + (d < 10 ? '0' : '') + d;
        if (!shifts.some(function(s) { return s.date === dStr; })) empty++;
    }
    document.getElementById('schStatUnassigned').innerText = empty;
}

function schNavigate(dir) {
    var y = schCurrentDate.getFullYear();
    var m = schCurrentDate.getMonth() + dir;
    schCurrentDate = new Date(y, m, 1);
    renderSchedule();
}
function schGoToday() { schCurrentDate = new Date(); renderSchedule(); }

function renderMonthCalendar() {
    var y = schCurrentDate.getFullYear();
    var m = schCurrentDate.getMonth();
    document.getElementById('schNavLabel').innerText = SCH_MONTHS[m] + ' ' + y;

    var firstDow    = new Date(y, m, 1).getDay();
    var daysInMonth = new Date(y, m + 1, 0).getDate();
    var todayStr    = toYMD(new Date());
    var cells       = [];

    for (var b = 0; b < firstDow; b++) {
        cells.push('<div class="sch-cell sch-cell-blank"></div>');
    }

    for (var d = 1; d <= daysInMonth; d++) {
        cells.push((function(day) {
            var mm      = m + 1;
            var dStr    = y + '-' + (mm < 10 ? '0' : '') + mm + '-' + (day < 10 ? '0' : '') + day;
            var dow     = new Date(y, m, day).getDay();
            var isSun   = dow === 0;
            var isToday = dStr === todayStr;
            var isPast  = dStr < todayStr;
            var avail   = schGetAvailableShifts(dStr);

            var cls = 'sch-cell' + (isToday ? ' sch-cell-today' : '') + (isSun ? ' sch-cell-sun' : '') + (isPast ? ' sch-cell-past' : '');
            var html = '<div class="' + cls + '">';

            // Header
            html += '<div class="sch-cell-head">';
            html += '<span class="sch-cell-date' + (isToday ? ' sch-cell-date-today' : '') + (isSun ? ' sch-cell-date-sun' : '') + '">' + day + '</span>';
            if (isSun)   html += '<span class="sch-sun-tag">CN</span>';
            if (isToday) html += '<span class="sch-today-tag">Hôm nay</span>';
            html += '</div>';

            // Từng ca dùng vòng for thường (không dùng forEach để tránh closure)
            for (var ci = 0; ci < avail.length; ci++) {
                var type = avail[ci];
                var sc   = shiftConfig[type];

                // Thu thập ca của ngày này (không dùng filter/closure)
                var typeShifts = [];
                for (var si = 0; si < shifts.length; si++) {
                    if (shifts[si].date === dStr && shifts[si].shiftType === type) {
                        typeShifts.push(shifts[si]);
                    }
                }

                html += '<div class="sch-shift-block">';
                html += '<div class="sch-shift-title" style="background:' + sc.bg + ';color:' + sc.color + '">';
                html += sc.label + '<small> ' + sc.time + '</small>';
                html += '</div>';
                html += '<div class="sch-shift-body">';

                for (var ki = 0; ki < typeShifts.length; ki++) {
                    var sh = typeShifts[ki];
                    // Tên hiển thị: "BS. Hải" hoặc "Hùng"
                    var nameParts = sh.staffName.split(' ');
                    var shortName = nameParts[nameParts.length - 1];
                    var isDoc = sh.staffName.indexOf('BS.') === 0;
                    var displayName = (isDoc ? 'BS. ' : '') + shortName;
                    var roomTip = sh.room ? ' · ' + sh.room : '';

                    // Chip click → detail popup (encode shiftId)
                    html += '<div class="sch-chip" style="background:' + sc.bg + ';border:1px solid ' + sc.color + '20;color:' + sc.color + '"'
                          + ' onclick="event.stopPropagation();showShiftDetail(' + sh.id + ')"'
                          + ' title="' + escapeHtml(sh.staffName) + roomTip + ' — click để xem chi tiết">'
                          + '<span class="sch-chip-name">' + escapeHtml(displayName) + '</span>'
                          + (sh.room ? '<span class="sch-chip-room">' + escapeHtml(sh.room.replace('Phòng ', 'P')) + '</span>' : '')
                          + '</div>';
                }

                // Nút + thêm (chỉ tương lai)
                if (!isPast) {
                    html += '<span class="sch-add-btn"'
                          + ' onclick="event.stopPropagation();schOpenModalFor(\'' + dStr + '\',\'' + type + '\')"'
                          + ' title="Thêm ' + sc.label + '"><i class="fas fa-plus"></i></span>';
                }
                html += '</div></div>';
            }

            // Tổng ca trong ngày
            var dayTotal = 0;
            for (var ti = 0; ti < shifts.length; ti++) {
                if (shifts[ti].date === dStr) dayTotal++;
            }
            if (dayTotal > 0) {
                html += '<div class="sch-day-count">' + dayTotal + ' ca</div>';
            }

            html += '</div>';
            return html;
        })(d));
    }

    document.getElementById('schMonthGrid').innerHTML = cells.join('');
}

// ---- Modal ----
function schOpenModal() {
    editingShiftId = null;
    document.getElementById('shiftModalTitle').innerText = 'Phân công ca làm';
    document.getElementById('shiftId').value = '';
    // Đặt min date = hôm nay để chặn quá khứ
    var todayStr = toYMD(new Date());
    var dateInput = document.getElementById('shiftDate');
    dateInput.min = todayStr;
    dateInput.value = todayStr;
    document.getElementById('shiftRoom').value = 'Phòng 1';
    schUpdateShiftTypeOptions();
    schPopulateStaff(null);
    document.getElementById('shiftModal').style.display = 'flex';
}
// Alias để nút "Thêm ca" trên toolbar vẫn hoạt động
function openShiftModal() { schOpenModal(); }

function schOpenModalFor(date, type) {
    var todayStr = toYMD(new Date());
    if (date < todayStr) {
        showToast('Chỉ có thể phân công cho ngày hôm nay trở đi!', 'error');
        return;
    }
    // Mở modal SAU KHI biết date hợp lệ
    editingShiftId = null;
    document.getElementById('shiftModalTitle').innerText = 'Phân công ca làm';
    document.getElementById('shiftId').value = '';
    var dateInput = document.getElementById('shiftDate');
    dateInput.min = todayStr;
    dateInput.value = date;
    document.getElementById('shiftRoom').value = 'Phòng 1';
    schUpdateShiftTypeOptions();
    // Set đúng ca sau khi update options
    document.getElementById('shiftType').value = type;
    schPopulateStaff(null);
    document.getElementById('shiftModal').style.display = 'flex';
}
function openShiftModalFor(date, type) { schOpenModalFor(date, type); }

function updateShiftTypeOptions() { schUpdateShiftTypeOptions(); }
function schUpdateShiftTypeOptions() {
    var date  = document.getElementById('shiftDate').value;
    var avail = date ? schGetAvailableShifts(date) : ['morning', 'afternoon'];
    var cur   = document.getElementById('shiftType').value;
    var sel   = document.getElementById('shiftType');
    sel.innerHTML = avail.map(function(t) {
        return '<option value="' + t + '"' + (t === cur ? ' selected' : '') + '>' +
               shiftConfig[t].label + ' (' + shiftConfig[t].time + ')</option>';
    }).join('');
}

function schPopulateStaff(selectedId) {
    var staff = accounts.filter(function(a) { return a.role === 'doctor' || a.role === 'staff'; });
    document.getElementById('shiftStaff').innerHTML = staff.map(function(a) {
        var lbl = a.role === 'doctor' ? 'BS' : 'NV';
        return '<option value="' + a.id + '"' + (a.id === selectedId ? ' selected' : '') + '>' +
               escapeHtml(a.name) + ' (' + lbl + ')</option>';
    }).join('');
}
// Alias cũ
function populateShiftStaff(id) { schPopulateStaff(id); }

function closeShiftModal() { document.getElementById('shiftModal').style.display = 'none'; }

async function saveShift() {
    var staffId  = parseInt(document.getElementById('shiftStaff').value);
    var date     = document.getElementById('shiftDate').value;
    var type     = document.getElementById('shiftType').value;
    var room     = document.getElementById('shiftRoom').value;
    var staffAcc = accounts.find(function(a) { return a.id === staffId; });

    if (!staffAcc || !date) { showToast('Vui lòng chọn đủ thông tin!', 'error'); return; }

    // Chặn ngày quá khứ
    if (date < toYMD(new Date())) { showToast('Chỉ được phân công cho ngày hôm nay trở đi!', 'error'); return; }

    // CN không có ca chiều
    var avail = schGetAvailableShifts(date);
    var typeOk = false;
    for (var i = 0; i < avail.length; i++) { if (avail[i] === type) { typeOk = true; break; } }
    if (!typeOk) { showToast('Chủ nhật không có ca chiều!', 'error'); return; }

    // Trùng ca: cùng nhân viên + ngày + ca (không cho trùng đúng 3 cái này)
    var dup = null;
    for (var di = 0; di < shifts.length; di++) {
        var s = shifts[di];
        if (s.staffId === staffId && s.date === date && s.shiftType === type && s.id !== editingShiftId) {
            dup = s; break;
        }
    }
    if (dup) { showToast(staffAcc.name + ' đã có ' + shiftConfig[type].label + ' ngày này rồi!', 'error'); return; }

    var data = { staffId: staffId, staffName: staffAcc.name, shiftType: type, date: date, room: room, note: '' };

    async function saveShiftToServer(payload, isUpdate) {
        var params = new URLSearchParams();
        params.append('action', isUpdate ? 'update' : 'create');
        if (isUpdate) params.append('id', String(payload.id));
        params.append('staffId', String(payload.staffId));
        params.append('shiftType', payload.shiftType);
        params.append('date', payload.date);
        params.append('room', payload.room || '');
        var response = await fetch((window.ADMIN_CONTEXT_PATH || '') + '/admin/shifts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: params.toString()
        });
        var json = null;
        try { json = await response.json(); } catch (e) { json = null; }
        if (!response.ok || (json && json.success === false)) {
            throw new Error((json && json.message) ? json.message : 'Không thể lưu ca làm việc');
        }
        return json || {success:true};
    }

    var res = null;
    if (editingShiftId) {
        data.id = editingShiftId;
        res = await withApiGuard(function() { return saveShiftToServer(data, true); }, 'Đã cập nhật ca làm');
    } else {
        res = await withApiGuard(function() { return saveShiftToServer(data, false); }, 'Đã phân công ca làm');
    }
    if (!res) return;
    var assigned = res.assignedBookings || 0;
    if (assigned > 0) {
        showToast('Đã phân ca và gán bác sĩ/phòng cho ' + assigned + ' lịch hẹn.', 'success');
    }
    closeShiftModal();
    try { localStorage.setItem(ADMIN_LAST_TAB_KEY, 'schedule'); } catch (e) {}
    window.location.reload();
}

// ===== CHI TIẾT CA LÀM =====
var viewingShiftId = null;

function showShiftDetail(id) {
    var sh = null;
    for (var i = 0; i < shifts.length; i++) {
        if (shifts[i].id === id) { sh = shifts[i]; break; }
    }
    if (!sh) return;
    viewingShiftId = id;
    var sc = shiftConfig[sh.shiftType] || shiftConfig.morning;
    var acc = accounts.find(function(a) { return a.id === sh.staffId; });
    var roleLabel = acc ? (accRoleConfig[acc.role] || {label:''}).label : '';
    var isPast = sh.date < toYMD(new Date());

    var html = '<div class="shift-detail-wrap">';
    // Avatar + tên
    html += '<div class="shift-detail-header" style="border-left:4px solid ' + sc.color + '">';
    if (acc && acc.avatar) {
        html += '<img src="' + acc.avatar + '" class="shift-detail-avatar">';
    } else {
        html += '<div class="shift-detail-avatar-icon" style="background:' + sc.bg + ';color:' + sc.color + '"><i class="fas fa-user-md"></i></div>';
    }
    html += '<div>';
    html += '<div class="shift-detail-name">' + escapeHtml(sh.staffName) + '</div>';
    html += '<span class="badge ' + (acc ? (accRoleConfig[acc.role]||{badge:'badge-staff'}).badge : 'badge-staff') + '">' + roleLabel + '</span>';
    html += '</div></div>';

    // Thông tin ca
    html += '<div class="shift-detail-grid">';
    html += '<div class="shift-detail-item"><i class="fas fa-calendar" style="color:' + sc.color + '"></i><div><div class="sdi-label">Ngày</div><div class="sdi-val">' + formatDate(sh.date) + (isPast ? ' <span class="sch-past-tag">Đã qua</span>' : '') + '</div></div></div>';
    html += '<div class="shift-detail-item"><i class="fas fa-clock" style="color:' + sc.color + '"></i><div><div class="sdi-label">Ca làm</div><div class="sdi-val" style="color:' + sc.color + ';font-weight:700">' + sc.label + ' · ' + sc.time + '</div></div></div>';
    html += '<div class="shift-detail-item"><i class="fas fa-door-open" style="color:' + sc.color + '"></i><div><div class="sdi-label">Phòng</div><div class="sdi-val">' + escapeHtml(sh.room || '—') + '</div></div></div>';
    if (acc && acc.specialty) {
        html += '<div class="shift-detail-item"><i class="fas fa-stethoscope" style="color:' + sc.color + '"></i><div><div class="sdi-label">Chuyên khoa</div><div class="sdi-val">' + escapeHtml(acc.specialty) + '</div></div></div>';
    }
    html += '</div>';

    // Cảnh báo nếu ngày đã qua
    if (isPast) {
        html += '<div class="shift-detail-warning"><i class="fas fa-info-circle"></i> Ca này đã qua, không thể sửa hoặc xóa.</div>';
    }
    html += '</div>';

    document.getElementById('shiftDetailBody').innerHTML = html;
    document.getElementById('shiftDetailTitle').innerHTML = '<span style="color:' + sc.color + '">' + sc.label + '</span> — ' + formatDate(sh.date);

    // Ẩn/hiện nút sửa xóa nếu đã qua
    var btnDel = document.querySelector('#shiftDetailModal .btn-danger');
    var btnEdit = document.querySelector('#shiftDetailModal .btn-save');
    if (btnDel)  btnDel.style.display  = isPast ? 'none' : '';
    if (btnEdit) btnEdit.style.display = isPast ? 'none' : '';

    document.getElementById('shiftDetailModal').style.display = 'flex';
}

function closeShiftDetailModal() {
    document.getElementById('shiftDetailModal').style.display = 'none';
    viewingShiftId = null;
}

async function deleteShiftFromDetail() {
    if (!viewingShiftId) return;
    const ok = await AppNotify.confirm({ message: 'Bạn có chắc muốn xóa ca làm này?' });
    if (!ok) return;
    async function deleteShiftOnServer(id) {
        var params = new URLSearchParams();
        params.append('action', 'delete');
        params.append('id', String(id));
        var response = await fetch((window.ADMIN_CONTEXT_PATH || '') + '/admin/shifts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                'X-Requested-With': 'XMLHttpRequest'
            },
            body: params.toString()
        });
        var json = null;
        try { json = await response.json(); } catch (e) { json = null; }
        if (!response.ok || (json && json.success === false)) {
            throw new Error((json && json.message) ? json.message : 'Không thể xóa ca làm');
        }
        return json || {success:true};
    }

    var res = await withApiGuard(function() { return deleteShiftOnServer(viewingShiftId); }, 'Đã xóa ca làm');
    if (!res) return;
    closeShiftDetailModal();
    try { localStorage.setItem(ADMIN_LAST_TAB_KEY, 'schedule'); } catch (e) {}
    window.location.reload();
}

function editShiftFromDetail() {
    if (!viewingShiftId) return;
    var sh = null;
    for (var i = 0; i < shifts.length; i++) {
        if (shifts[i].id === viewingShiftId) { sh = shifts[i]; break; }
    }
    if (!sh) return;
    closeShiftDetailModal();
    // Mở modal sửa với dữ liệu ca
    editingShiftId = sh.id;
    document.getElementById('shiftModalTitle').innerText = 'Sửa ca làm';
    document.getElementById('shiftId').value = sh.id;
    var todayStr = toYMD(new Date());
    var dateInput = document.getElementById('shiftDate');
    dateInput.min = todayStr;
    dateInput.value = sh.date;
    schUpdateShiftTypeOptions();
    document.getElementById('shiftType').value = sh.shiftType;
    schPopulateStaff(sh.staffId);
    document.getElementById('shiftRoom').value = sh.room || 'Phòng 1';
    document.getElementById('shiftModal').style.display = 'flex';
}

// ==================== TÀI KHOẢN ====================
var accRoleConfig = {
    'customer': {label:'Khách hàng', badge:'badge-customer'},
    'doctor':   {label:'Bác sĩ',    badge:'badge-doctor'},
    'staff':    {label:'Nhân viên',  badge:'badge-staff'},
    'admin':    {label:'Admin',      badge:'badge-admin'}
};
var genderMap = {male:'Nam', female:'Nữ', other:'Khác'};

function renderAccounts() {
    var search = document.getElementById('accSearch').value.toLowerCase();
    var filtered = accounts.filter(function(a) {
        return (accFilter === 'all' || a.role === accFilter) &&
               (!search || a.name.toLowerCase().indexOf(search) > -1 || a.phone.indexOf(search) > -1);
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
            var specialty = (a.role==='doctor' && a.specialty) ? escapeHtml(a.specialty) : '<span style="color:var(--text-sub)">—</span>';
            return '<tr class="acc-row-clickable" onclick="showStaffInfo(' + a.id + ')" title="Xem thông tin">' +
                '<td><span style="font-weight:700;color:var(--primary-color)">#' + a.id + '</span></td>' +
                '<td><span style="font-weight:600">' + escapeHtml(a.name) + '</span></td>' +
                '<td><span class="badge ' + rc.badge + '">' + rc.label + '</span></td>' +
                '<td style="font-size:0.82rem">' + specialty + '</td>' +
                '<td>' + (a.phone || '—') + '</td>' +
                '<td>' + statusBadge + '</td>' +
                '<td onclick="event.stopPropagation()"><div class="action-btns">' +
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

function onAccRoleChange() {
    var role  = document.getElementById('accRole').value;
    var sg    = document.getElementById('accSpecialtyGroup');
    var dg    = document.getElementById('accDegreeGroup');
    var ag = document.getElementById('accAvatarUpload') || document.querySelector('.acc-avatar-upload');
    var isDoc = role === 'doctor';

    sg.style.opacity       = isDoc ? '1' : '0.35';
    sg.style.pointerEvents = isDoc ? ''  : 'none';
    sg.querySelector('select').disabled = !isDoc;
    if (!isDoc) sg.querySelector('select').value = '';

    dg.style.opacity       = isDoc ? '1' : '0.35';
    dg.style.pointerEvents = isDoc ? ''  : 'none';
    dg.querySelector('input').disabled = !isDoc;
    if (!isDoc) dg.querySelector('input').value = '';

    if (ag) {
        ag.style.opacity       = isDoc ? '1' : '0.35';
        ag.style.pointerEvents = isDoc ? ''  : 'none';
        var fileInput = ag.querySelector('input[type="file"]');
        if (fileInput) fileInput.disabled = !isDoc;
        if (!isDoc) {
            var prev = document.getElementById('accAvatarPreview');
            if (prev) { prev.src = ''; prev.style.display = 'none'; }
            if (fileInput) fileInput.value = '';
        }
    }
}

function openAccountModal() {
    editingAccId = null;
    document.getElementById('accModalTitle').innerText = 'Thêm tài khoản mới';
    document.getElementById('accId').value = '';
    document.getElementById('accName').value = '';
    document.getElementById('accRole').value = 'doctor';
    document.getElementById('accDob').value = '';
    document.getElementById('accGender').value = '';
    document.getElementById('accSpecialty').value = '';
    document.getElementById('accDegree').value = '';
    document.getElementById('accPhone').value = '';
    document.getElementById('accPassword').value = '';
    document.getElementById('accAvatarFile').value = '';
    document.getElementById('accAvatarPreview').style.display = 'none';
    document.getElementById('accAvatarPreview').src = '';
    document.getElementById('accStatusGroup').style.display = 'none';
    onAccRoleChange();
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
    document.getElementById('accDob').value = a.dob || '';
    document.getElementById('accGender').value = a.gender || '';
    document.getElementById('accSpecialty').value = a.specialty || '';
    document.getElementById('accDegree').value = a.degree || '';
    document.getElementById('accPhone').value = a.phone || '';
    document.getElementById('accPassword').value = '';
    document.getElementById('accAvatarFile').value = '';
    var prev = document.getElementById('accAvatarPreview');
    if (a.avatar) { prev.src = a.avatar; prev.style.display = 'block'; }
    else { prev.src = ''; prev.style.display = 'none'; }
    document.getElementById('accStatus').value = a.status;
    document.getElementById('accStatusGroup').style.display = '';
    onAccRoleChange();
    document.getElementById('accountModal').style.display = 'flex';
}

function onAvatarChange(input) {
    var file = input.files[0];
    if (!file) return;
    var reader = new FileReader();
    reader.onload = function(e) {
        var prev = document.getElementById('accAvatarPreview');
        prev.src = e.target.result;
        prev.style.display = 'block';
    };
    reader.readAsDataURL(file);
}

function closeAccountModal() { document.getElementById('accountModal').style.display = 'none'; }

function reloadAdminPage() {
    window.location.reload();
}

async function postAdminAccount(params, fallbackError) {
    var response = await fetch((window.ADMIN_CONTEXT_PATH || '') + '/admin/accounts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
            'X-Requested-With': 'XMLHttpRequest'
        },
        body: params.toString()
    });
    var json = null;
    try { json = await response.json(); } catch (e) { json = null; }
    if (!response.ok || (json && json.success === false)) {
        throw new Error((json && json.message) ? json.message : fallbackError);
    }
    return { success: true };
}

async function saveAccount() {
    var name = document.getElementById('accName').value.trim();
    var phone = document.getElementById('accPhone').value.trim();
    if (!name || !phone) { showToast('Vui lòng điền đủ họ tên và số điện thoại!', 'error'); return; }
    if (!editingAccId && !document.getElementById('accPassword').value) {
        showToast('Vui lòng nhập mật khẩu!', 'error'); return;
    }
    var role = document.getElementById('accRole').value;
    var prevSrc = document.getElementById('accAvatarPreview').src;
    var avatar = (prevSrc && prevSrc.indexOf('data:') === 0) ? prevSrc : (editingAccId ? (accounts.find(function(a){return a.id===editingAccId;})||{}).avatar||'' : '');
    var data = {
        name: name, role: role, phone: phone,
        dob: document.getElementById('accDob').value,
        gender: document.getElementById('accGender').value,
        specialty: role === 'doctor' ? document.getElementById('accSpecialty').value : '',
        degree: role === 'doctor' ? document.getElementById('accDegree').value.trim() : '',
        avatar: avatar,
        status: editingAccId ? document.getElementById('accStatus').value : 'active'
    };
    var password = document.getElementById('accPassword').value;
    var res = null;
    if (editingAccId) {
        res = await withApiGuard(async function() {
            var params = new URLSearchParams();
            params.append('action', 'update');
            params.append('id', String(editingAccId));
            params.append('name', data.name);
            params.append('phone', data.phone);
            params.append('password', password || '');
            params.append('role', data.role);
            params.append('status', data.status || 'active');
            params.append('dob', data.dob || '');
            params.append('gender', data.gender || '');
            params.append('specialty', data.specialty || '');
            params.append('degree', data.degree || '');
            params.append('avatar', data.avatar || '');
            return postAdminAccount(params, 'Không cập nhật được tài khoản');
        }, 'Đã cập nhật tài khoản');
    } else {
        res = await withApiGuard(async function() {
            var params = new URLSearchParams();
            params.append('action', 'create');
            params.append('name', data.name);
            params.append('phone', data.phone);
            params.append('password', password);
            params.append('role', data.role);
            params.append('dob', data.dob || '');
            params.append('gender', data.gender || '');
            params.append('specialty', data.specialty || '');
            params.append('degree', data.degree || '');
            params.append('avatar', data.avatar || '');
            return postAdminAccount(params, 'Không thêm được tài khoản');
        }, 'Đã thêm tài khoản mới');
    }
    if (!res) return;
    closeAccountModal();
    reloadAdminPage();
}

// ===== POPUP THÔNG TIN NHÂN SỰ =====
function showStaffInfo(id) {
    var a = accounts.find(function(x){return x.id===id;});
    if (!a) return;
    staffInfoCurrentId = id;
    var rc = accRoleConfig[a.role] || {label:a.role, badge:'badge-staff'};
    var iconMap = {doctor:'fa-user-md', staff:'fa-user-tie', admin:'fa-user-shield', customer:'fa-user'};
    var icon = iconMap[a.role] || 'fa-user';
    var dobStr = '—';
    if (a.dob) {
        var age = new Date().getFullYear() - new Date(a.dob + 'T00:00:00').getFullYear();
        dobStr = formatDate(a.dob) + ' (' + age + ' tuổi)';
    }
    // Tính tuần hiện tại không cần getWeekStart
    var now = new Date();
    var dow = now.getDay();
    var wsDate = new Date(now.getFullYear(), now.getMonth(), now.getDate() - dow);
    var weDate = new Date(wsDate.getFullYear(), wsDate.getMonth(), wsDate.getDate() + 6);
    var wsStr = toYMD(wsDate), weStr = toYMD(weDate);
    var weekShifts = shifts.filter(function(s) {
        return s.staffId === id && s.date >= wsStr && s.date <= weStr;
    });
    var shiftsHtml = weekShifts.length > 0
        ? weekShifts.map(function(s) {
            var sc = shiftConfig[s.shiftType] || shiftConfig.morning;
            return '<span class="sch-chip" style="background:'+sc.bg+';color:'+sc.color+'">'+formatDate(s.date)+' – '+sc.label+'</span>';
          }).join('')
        : '<span style="color:var(--text-sub);font-size:0.85rem">Chưa có ca tuần này</span>';
    var avatarHtml = a.avatar
        ? '<img src="'+a.avatar+'" style="width:56px;height:56px;border-radius:50%;object-fit:cover">'
        : '<div class="staff-info-avatar"><i class="fas '+icon+'"></i></div>';
    document.getElementById('staffInfoBody').innerHTML =
        '<div class="staff-info-header">' +
            avatarHtml +
            '<div><div class="staff-info-name">'+escapeHtml(a.name)+'</div><span class="badge '+rc.badge+'">'+rc.label+'</span></div>' +
        '</div>' +
        '<div class="staff-info-grid">' +
            '<div class="staff-info-item"><i class="fas fa-phone"></i><span>'+escapeHtml(a.phone||'—')+'</span></div>' +
            '<div class="staff-info-item"><i class="fas fa-birthday-cake"></i><span>'+dobStr+'</span></div>' +
            '<div class="staff-info-item"><i class="fas fa-venus-mars"></i><span>'+(genderMap[a.gender]||'—')+'</span></div>' +
            '<div class="staff-info-item"><i class="fas fa-graduation-cap"></i><span>'+escapeHtml(a.degree||'—')+'</span></div>' +
            (a.role==='doctor' ? '<div class="staff-info-item" style="grid-column:1/-1"><i class="fas fa-stethoscope"></i><span>'+escapeHtml(a.specialty||'—')+'</span></div>' : '') +
        '</div>' +
        '<div class="staff-info-section">' +
            '<div class="staff-info-sec-title"><i class="fas fa-calendar-week"></i> Ca làm việc tuần này</div>' +
            '<div style="display:flex;flex-wrap:wrap;gap:4px;margin-top:8px">'+shiftsHtml+'</div>' +
        '</div>';
    document.getElementById('staffInfoModal').style.display = 'flex';
}
function closeStaffInfoModal() { document.getElementById('staffInfoModal').style.display = 'none'; }
function editAccountFromInfo() { closeStaffInfoModal(); if (staffInfoCurrentId) editAccount(staffInfoCurrentId); }



async function toggleAccStatus(id) {
    var a = accounts.find(function(x){return x.id===id;});
    if (!a) return;
    var newStatus = a.status === 'active' ? 'inactive' : 'active';
    var msg = newStatus === 'active' ? 'Đã mở khóa tài khoản' : 'Đã khóa tài khoản';
    var res = await withApiGuard(async function() {
        var params = new URLSearchParams();
        params.append('action', 'toggle-status');
        params.append('id', String(id));
        params.append('status', newStatus);
        return postAdminAccount(params, 'Không cập nhật được trạng thái');
    }, msg);
    if (res) reloadAdminPage();
}

async function deleteAccount(id) {
    const ok = await AppNotify.confirm({ message: 'Bạn có chắc muốn xóa tài khoản này?' });
    if (!ok) return;
    var res = await withApiGuard(async function() {
        var params = new URLSearchParams();
        params.append('action', 'delete');
        params.append('id', String(id));
        return postAdminAccount(params, 'Không xóa được tài khoản');
    }, 'Đã xóa tài khoản');
    if (res) reloadAdminPage();
}

// ==================== DOANH THU ====================

function revMethodBadge(method) {
    var m = (method || '').toLowerCase();
    if (m === 'transfer' || m.indexOf('chuy') >= 0) return { label: method || 'Chuyển khoản', cls: 'rev-transfer' };
    if (m === 'installment' || m.indexOf('góp') >= 0 || m.indexOf('gop') >= 0) return { label: method || 'Trả góp', cls: 'rev-installment' };
    if (m === 'card' || m.indexOf('thẻ') >= 0 || m.indexOf('the') >= 0) return { label: method || 'Thẻ ngân hàng', cls: 'rev-card' };
    if (m === 'cash' || m.indexOf('mặt') >= 0 || m.indexOf('mat') >= 0) return { label: method || 'Tiền mặt', cls: 'rev-cash' };
    return { label: method || 'Khác', cls: 'rev-cash' };
}

function formatRevTxnDate(dateStr) {
    if (!dateStr) return '';
    if (dateStr.indexOf('-') >= 0) return formatDate(dateStr);
    return dateStr;
}

// ==================== CHẾ ĐỘ LỌC ====================

/** Điền danh sách năm cho lọc tháng/năm (từ dữ liệu DB + khoảng mở rộng). */
function initRevYearSelects() {
    var now = new Date();
    var currentYear = now.getFullYear();
    var minY = currentYear;
    var maxY = currentYear;

    allTxns.forEach(function(t) {
        if (!t.date || t.date.length < 4) return;
        var y = parseInt(String(t.date).substring(0, 4), 10);
        if (!isNaN(y)) {
            if (y < minY) minY = y;
            if (y > maxY) maxY = y;
        }
    });

    minY = Math.min(minY, currentYear - 15);
    maxY = Math.max(maxY, currentYear + 2);

    var years = [];
    for (var y = maxY; y >= minY; y--) years.push(y);

    function fillSelect(elId, selectedYear) {
        var el = document.getElementById(elId);
        if (!el) return;
        el.innerHTML = years.map(function(y) {
            return '<option value="' + y + '"' + (y === selectedYear ? ' selected' : '') + '>' + y + '</option>';
        }).join('');
    }

    fillSelect('revMonthYear', currentYear);
    fillSelect('revYearSel', currentYear);

    var monthSel = document.getElementById('revMonthSel');
    if (monthSel) monthSel.value = String(now.getMonth() + 1);

    var fromEl = document.getElementById('revDateFrom');
    var toEl = document.getElementById('revDateTo');
    if (fromEl && toEl) {
        var firstDay = new Date(currentYear, now.getMonth(), 1);
        var lastDay = new Date(currentYear, now.getMonth() + 1, 0);
        fromEl.value = firstDay.toISOString().slice(0, 10);
        toEl.value = lastDay.toISOString().slice(0, 10);
    }
}

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
    document.getElementById('revFrTableBody').innerHTML = filtered.length === 0
        ? '<tr><td colspan="7" style="text-align:center;padding:30px;color:var(--text-sub)">Không có giao dịch nào trong khoảng thời gian này</td></tr>'
        : filtered.sort(function(a,b){return b.date.localeCompare(a.date);}).map(function(t) {
            var m = revMethodBadge(t.method);
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
        return '<div class="rev-bc-col" title="' + r.month + '/' + new Date().getFullYear() + ': ' + (hasData ? r.revenue.toLocaleString('vi-VN') + 'đ' : 'Chưa có dữ liệu') + '">' +
            '<div class="rev-bc-val">' + valM + '</div>' +
            '<div class="rev-bc-bar" style="height:' + pct + '%;background:' + barColor + '"></div>' +
            '<div class="rev-bc-month">' + r.month + '</div>' +
        '</div>';
    }).join('');

    // --- Donut chart ---
    var catTotal = revenueByCat.reduce(function(a,c){return a+c.revenue;},0);
    if (catTotal <= 0) {
        document.getElementById('donutSvg').innerHTML = '';
        document.getElementById('donutTotal').innerText = '0';
        document.getElementById('donutLegend').innerHTML = '<p style="color:var(--text-sub);font-size:0.85rem;padding:8px 0">Chưa có dữ liệu doanh thu theo chuyên khoa</p>';
    } else {
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
    }

    // --- Top services ---
    var rankClasses = ['gold','silver','bronze','',''];
    document.getElementById('revTopServices').innerHTML = revenueTopServices.length === 0
        ? '<p style="color:var(--text-sub);font-size:0.85rem;padding:12px 0">Chưa có dữ liệu</p>'
        : revenueTopServices.map(function(s,i) {
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
    document.getElementById('revPaymentMethods').innerHTML = revenuePayments.length === 0
        ? '<p style="color:var(--text-sub);font-size:0.85rem;padding:12px 0">Chưa có dữ liệu</p>'
        : revenuePayments.map(function(p) {
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
        var pct = maxQ > 0 ? Math.round(qAmts[i]/maxQ*100) : 0;
        var appts = q.months.reduce(function(a,m){return a+revenueMonths[m].appointments;},0);
        return '<div class="rev-q-item">' +
            '<div class="rev-q-header"><span class="rev-q-label">'+q.label+'</span><span class="rev-q-amt">'+fmtRev(qAmts[i])+'</span></div>' +
            '<div class="rev-q-track"><div class="rev-q-fill" style="width:'+pct+'%"></div></div>' +
            '<div class="rev-q-sub">'+appts+' lượt khám &nbsp;·&nbsp; Trung bình '+fmtRev(Math.round(qAmts[i]/3))+'/tháng</div>' +
        '</div>';
    }).join('');

    // --- Transactions table ---
    document.getElementById('revTableBody').innerHTML = revenueTxns.length === 0
        ? '<tr><td colspan="7" style="text-align:center;padding:24px;color:var(--text-sub)">Chưa có giao dịch thanh toán</td></tr>'
        : revenueTxns.map(function(t) {
        var m = revMethodBadge(t.method);
        return '<tr>' +
            '<td><span class="rev-txn-id">#'+t.id+'</span></td>' +
            '<td style="color:var(--text-sub)">'+formatRevTxnDate(t.date)+'</td>' +
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
    if (e.target === document.getElementById('shiftModal')) closeShiftModal();
    if (e.target === document.getElementById('shiftDetailModal')) closeShiftDetailModal();
    if (e.target === document.getElementById('accountModal')) closeAccountModal();
    if (e.target === document.getElementById('staffInfoModal')) closeStaffInfoModal();
};

// ==================== KHỞI TẠO ====================
document.addEventListener('keydown', function(e) {
    if (e.key === 'Escape') closeAdminBellPanel();
});

document.addEventListener('DOMContentLoaded', async function() {
    if (ADMIN_CONFIG.USE_MOCK) {
        console.info('[admin] mode: MOCK data');
    } else {
        console.info('[admin] mode: REAL API');
    }
    renderAdminBellNotifications();
    await loadServicesFromServer();
    await loadShiftsFromServer();
    await loadAccountsFromServer();
    await loadRevenueFromServer();
    try {
        var lastTab = localStorage.getItem(ADMIN_LAST_TAB_KEY);
        if (lastTab && ['services', 'schedule', 'accounts', 'revenue'].indexOf(lastTab) !== -1) {
            var targetLink = document.querySelector('.nav-menu a[onclick*="switchTab(\'' + lastTab + '\'"]');
            switchTab(lastTab, targetLink || null);
        }
    } catch (e) {}
});